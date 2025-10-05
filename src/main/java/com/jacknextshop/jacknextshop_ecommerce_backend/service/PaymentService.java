package com.jacknextshop.jacknextshop_ecommerce_backend.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.payment.StripeResponse;
import com.jacknextshop.jacknextshop_ecommerce_backend.entity.Order;
import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Value("${stripe.secret_key}")
	private String secretKey;

	@Value("${stripe.webhook_secret}")
	private String webhookSecret;

	@Value("${stripe.success_url}")
	private String successUrl;

	@Value("${stripe.cancel_url}")
	private String cancelUrl;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	public StripeResponse createCheckoutSession(UUID orderId) {
		Stripe.apiKey = secretKey;

		Order order = orderService.findByOrderId(orderId);

		SessionCreateParams params = SessionCreateParams.builder()
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(successUrl + order.getOrderId())
				.setCancelUrl(cancelUrl + order.getOrderId())
				.putMetadata("orderId", String.valueOf(order.getOrderId()))
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setQuantity(1L)
								.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
										.setCurrency("thb")
										.setUnitAmount(order.getTotalPrice().multiply(BigDecimal.valueOf(100)).longValue())
										.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
												.setName("Order #" + order.getOrderId())
												.build())
										.build())
								.build())
				.build();

		Session session = null;
		try {
			session = Session.create(params);
		} catch (StripeException e) {
			System.out.println(e.getMessage());
		}

		return StripeResponse.builder()
				.status("SUCCESS")
				.message("Payment session created successfully.")
				.sessionId(session.getId())
				.sessionUrl(session.getUrl())
				.build();
	}

	public void handleStripeWebhook(String payload, String sigHeader) {
		Event event = null;
		try {
			event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
		} catch (SignatureVerificationException e) {
			logger.error("Invalid signature");
			return;
		} catch (JsonSyntaxException e) {
			logger.error("Invalid JSON syntax in payload");
    		return;
		}

		EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
		if (deserializer.getObject().isEmpty()) {
			logger.error("Failed to deserialize Stripe object");
			return;
		} 

		StripeObject stripeObject = deserializer.getObject().get();

		if ("checkout.session.completed".equals(event.getType())) {
			Session session = (Session) stripeObject;
			String orderId = session.getMetadata().get("orderId");

			if(orderId == null) {
				logger.error("Order ID is missing in metadata");
			}

            System.out.println(orderId);
			
			orderService.markOrderAsPaid(UUID.fromString(orderId));
			productService.updateStockAndSoldForSuccessOrder(UUID.fromString(orderId));
		}
	}
}
