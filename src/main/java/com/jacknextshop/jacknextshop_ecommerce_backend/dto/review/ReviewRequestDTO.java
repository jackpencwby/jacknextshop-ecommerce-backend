package com.jacknextshop.jacknextshop_ecommerce_backend.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDTO {
    private String comment;
    @NotNull(message = "กรุณาใส่ Rating")
    @Min(value = 0, message = "Rating ต้องมากกว่าหรือเท่ากับ 0 ถึง 5")
    @Max(value = 5, message = "Rating ต้องมากกว่าหรือเท่ากับ 0 ถึง 5")
    private int rating;
    @NotNull(message = "กรุณาใส่ like")
    private boolean isLike;
}
