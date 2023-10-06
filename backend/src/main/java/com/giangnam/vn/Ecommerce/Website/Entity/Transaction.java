package com.giangnam.vn.Ecommerce.Website.Entity;

import lombok.Data;

@Data
public class Transaction {
    private String transactionIdMerchant;
    private String transactionIdCustomer;

    private Integer orderId;
}
