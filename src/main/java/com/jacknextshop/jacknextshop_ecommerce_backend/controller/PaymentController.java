package com.jacknextshop.jacknextshop_ecommerce_backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacknextshop.jacknextshop_ecommerce_backend.dto.payment.StripeResponse;
import com.jacknextshop.jacknextshop_ecommerce_backend.service.PaymentService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<StripeResponse> createSession(
            @PathVariable UUID orderId) {
        StripeResponse stripeResponse = paymentService.createCheckoutSession(orderId);

        return ResponseEntity.ok().body(stripeResponse);
    }

    @PostMapping("/webhook")
    public void handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        paymentService.handleStripeWebhook(payload, sigHeader);
    }
}
