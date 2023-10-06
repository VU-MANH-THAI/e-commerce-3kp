package com.giangnam.vn.Ecommerce.Website.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO implements Serializable {
    private Integer id;
    private String transactionIdMerchant;
    private String transactionIdCustomer;
}
