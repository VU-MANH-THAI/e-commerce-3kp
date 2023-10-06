package com.giangnam.vn.Ecommerce.Website.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Integer shoppingCartId;
    private String creditCardNumber;
    private String pinCode;
    private LocalDate expirationCard;
}
