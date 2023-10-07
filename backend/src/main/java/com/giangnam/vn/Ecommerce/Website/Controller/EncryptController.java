package com.giangnam.vn.Ecommerce.Website.Controller;

import com.giangnam.vn.Ecommerce.Website.Entity.*;
import com.giangnam.vn.Ecommerce.Website.Request.PaymentRequest;
import com.giangnam.vn.Ecommerce.Website.Service.EncryptService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/encrypt")

public class EncryptController {
    @Autowired
    EncryptService encryptService;

    @PostMapping("/new-transaction")
    @CrossOrigin
    public ResponseEntity<?> addNewTransaction(@RequestParam Integer id) {
        return new ResponseEntity<>(encryptService.addNewTransactionId(id), HttpStatus.OK);
    }

    @GetMapping("/new-transaction")
    @CrossOrigin
    public ResponseEntity<Transaction> get(@RequestParam Integer id) {
        return new ResponseEntity<>(encryptService.getTransactionId(id), HttpStatus.OK) ;
    }

    @PostMapping("/sendCusToMer")
    @CrossOrigin
    public ResponseEntity<EncryptCusToMer> sendCusToMer(@RequestBody PaymentRequest paymentRequest) {
        try {
            return encryptService.sendCusToMer(paymentRequest);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    @PostMapping("/sendMerToAcq")
    @CrossOrigin
    public ResponseEntity<MerToAcq> sendMerToAcq(@RequestBody MerchantToAcquired merchantToAcquired) {
        return encryptService.sendMerToAcq(merchantToAcquired.getSignature(),
                merchantToAcquired.getCert(),
                merchantToAcquired.getData());
    }

    @PostMapping("/sendAcqToMer")
    @CrossOrigin
    public ResponseEntity<AcqToMer> sendAcqToMer(@RequestBody AcquireToMerchant acquireToMerchant) {
        return encryptService.sendAcqToMer( acquireToMerchant.getSignature(),
                                            acquireToMerchant.getCertM(),
                                            acquireToMerchant.getData());
    }

    @PostMapping("/sendMerToCus")
    @CrossOrigin
    public ResponseEntity<MerToCus> sendMerToCus(@RequestBody MerchantToCustomer merchantToCustomer) {
        return encryptService.sendMerToCus(merchantToCustomer.getSignature());
    }

    @PostMapping("/customer-notify")
    @CrossOrigin
    public ResponseEntity<Cus> customerNotify(@RequestBody Customer customer) {
        return encryptService.notifyCustomer(customer.getSignature(), customer.getDataVerify());
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MerchantToAcquired {
    private String signature;
    private String cert;
    private String data;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AcquireToMerchant {
    private String signature;
    private String certM;
    private String data;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class MerchantToCustomer {
    private String signature;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {
    private String signature;
    private String dataVerify;
}