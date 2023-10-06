package com.giangnam.vn.Ecommerce.Website.Entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SLIP implements Serializable {
    private String transactionIdCustomer;
    private String transactionIdMerchant;
    private String creditCardNumber;
    private String pinCode;
}
