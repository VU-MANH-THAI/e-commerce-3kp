package com.giangnam.vn.Ecommerce.Website.Entity;

import lombok.Data;

@Data
public class EncryptCusToMer {
    private String Signature;
    private String CertC;
    private String dataToVerify;
}
