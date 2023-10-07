package com.giangnam.vn.Ecommerce.Website.Service;

import com.giangnam.vn.Ecommerce.Website.Entity.*;
import com.giangnam.vn.Ecommerce.Website.Request.PaymentRequest;
import org.springframework.http.ResponseEntity;

public interface EncryptService {
    public Transaction addNewTransactionId(Integer id);
    public ResponseEntity<EncryptCusToMer> sendCusToMer(PaymentRequest paymentRequest);

    public ResponseEntity<MerToAcq> sendMerToAcq(String signature, String certC, String dataToVerify);

    public ResponseEntity<AcqToMer> sendAcqToMer(String signature, String certM, String dataVerify);
    public ResponseEntity<MerToCus> sendMerToCus(String signature);
    public ResponseEntity<Cus> notifyCustomer(String signature, String dataVerify);

    Transaction getTransactionId(Integer id);
}
