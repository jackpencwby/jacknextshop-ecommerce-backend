package com.jacknextshop.jacknextshop_ecommerce_backend.dto.cart;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemDTO {
    @NotNull(message = "กรุณาใส่รหัสสินค้า")
    private UUID productId;

    @NotNull(message = "กรุณาใส่จำนวนสินค้า")
    @Min(value = 0, message = "จำนวนสินค้าต้องมากกว่าหรือเท่ากับ 0")
    private int amount;
}
