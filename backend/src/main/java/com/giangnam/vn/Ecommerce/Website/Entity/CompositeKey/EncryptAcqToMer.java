package com.giangnam.vn.Ecommerce.Website.Entity.CompositeKey;

import lombok.Data;

@Data
public class EncryptAcqToMer {
    private String signatureDigital;
    private String auth;
    private String dataToSign;
}
