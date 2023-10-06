package com.giangnam.vn.Ecommerce.Website.Service.Iml;

import com.giangnam.vn.Ecommerce.Website.DTO.ShoppingCartDTO;
import com.giangnam.vn.Ecommerce.Website.Entity.*;
import com.giangnam.vn.Ecommerce.Website.Repository.PaymentRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Shopping_CartRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.UserRepository;
import com.giangnam.vn.Ecommerce.Website.Request.PaymentRequest;
import com.giangnam.vn.Ecommerce.Website.Service.EncryptService;
import com.nimbusds.jose.shaded.gson.Gson;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class EncryptIml implements EncryptService {
    public static String publicKeyCustomer = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAxV+ZiHoJceSzQ+voEjL75AFUHaiDF+4q2de8RcDJuP7jyy79wUB7etB2Dgfi8MY2YJ2TVdZ+5MeJ3NctSejtj6elQnzWzJvD8hkBcKJQsDYpPP+LlbRgSyj4IUTEB9u3kj70PwjDez+8C8c6dCkQKtxEKJSzZz4iFRAuE/WzotkfrmwiqOvDBXdZYbutNeyHkba3ll7UrmCze87tnqTSiKi542F6ThzSy7Ypx+n86OD4yciqhzRD2TwzXWZeABuufq53azHTpx7qsHiwKQLESyUBl27U/bxw+Z/3S+F5qOcTaHe7Hp2ZytOowKiSjgWgg6Cj2QloQ0DnR4oi+SfPHm2dHCB63fhyX4alHP9NM3LYBvnRA3LB66UpLnpucLy6eiOwvZEchc+UoUk8UmOHM2oEZGZOhXSkz4zdzr85ghCjVvIBU46t4FUO5z++odDpg494UMRJsbq4Y1+JvPWR1cLpDmljcZxrfWPHAaXdoyhYeZGnnN7+DH9+reqJ7viGKWl1YNh2xYbM2l8OkZ/NntknfUzGncHmfxeh0R69qgh2pTadbcXpbJfsHGHxahkPdtfNwpAsWGvfHO9UwmlPdL/MHNOloYU5TnWj4dh9UXRFzvozCPcavuC9BPa5cKsy8cZr4N8Qs0Gk72A2ZyQwjs6MdzSeGIYXjcwLgjPakcMCAwEAAQ==";
    public static String privateKeyCustomer = "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";
    public static String publicKeyMerchant = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAwd/raceCQDSwm77U3//Jkw2D/53BHNhSgXZkswRK4d1cntQ6ujY2o7QFZDfLYE/Ru9W3EXCaLLeN90zfYb3SDsXsCr4JYBJmDD502Km13Trw/p04Sq0JzFHz4t7o69kqbMJYkd954WRCw4mJTZQsxA/iw9efJHEJFANHFwzhcNoR4GzS7nZA1d79pk+395u2nbnjlxtIACdgM43L0ymqKat2Nb80jpJCY1p+GTBgwNUBGm+pnqHz1Cye5Ui9ybS1X0gCvEPAk+GA1+6wv6kr6csvgnXUvjJJE+ua+65xSsoevjpE4eQWV77GFVfLl8/OvjNYNobtw2Hof8ez/3L6yLTWG/xLE5gm46Xkd6iy/9PqwesEBT5kIy22pHbDTMBqjlZjioNvzOF7Jf+V28CbJIDyaa6sMFWYSDg2aj7oENobnV8pdFD/396iTEsIyrqurfvgsz3sfUo32fr+vZWZBguZNeg3OZsonbIj39aJziJ9XvCIhugWPO3bOdM+/s1I9F1SFVgta7HL9XcHQK+zsPycUZQ2bWcyrE/eO3KXO6AmjPxkxfqd2zBW5AfniFba3F6QF5lEFD5kNOy/rsoQIx/0WCQ75ozo0JDBjk2nqDmTauLJ4CNqnX22KCfd07v1c6+GiNRDDrKLdHDXwsFZOSSdE5kedVmEc62QNm3hUhMCAwEAAQ==";
    public static String privateKeyMerchant = "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";
    public static String publicKeyAcquired = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA6JQGoW2fE18QI+6A8/IO/BSxMTxjWOTh+E/UO/QcGEfjJ6X/N+USTaiYkn3Na5m3YTDZCiuYRITVLPHLMX/hYo+ZNXI1E4FR/rPf3s2WVkE9GRUj10pZgTiSbuSuvHxYRIB6CQr9kipaiPPMyiTZ13HslMhL0tZQefo4PMguvo6S1TPOzYz4YHlkcMvEOCBgC3eUUSRG8EgAVlJufvnpNXVvqZVtMHUbobkfgqlpdV2tXBhalamOuqRuxlwUDqeZspsCiL7QrxKUN2DZiDPmfU8NSLoqc8mEl8EhEnn6c0bHdXqaJQ+i8XaN3NrO0g1XW3ocGWd3NxJBQrVBIhcepRlss+duya5Eb6X3UGr61i3t+/+T43eI+55Mgg+9hvIBHjLh6jjGRVrm0+hTn1Gdi40tcGZ6srn3sovwFc41Nl5YAxonEqzoCu24/x1gy4AOfWF8QqEwW+c53QVXbIx08rzIuZbepIl16iLb0mwFoRGy0MasL1PBuKR6zZasKBiJ//w/Ek3Y64NDGsS0x61xdaaMKw6fzo3fkqeNADR2mXZBHbZqIF4QEhQjCk0wN/TbzaLUlfPg4f9fjLggGvBDc08v1ZiNJD0wz4aI+QQdmB5QJzymHcQDJSFPniHl1eg2zB6LmN1lS5Zpp8TXLjpcU1BuxTl01YI/rfWb7Cu+k78CAwEAAQ==";
    public static String privateKeyAcquired = "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";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Shopping_CartRepository shoppingCartRepository;

    @Override
    public Transaction addNewTransactionId(Integer id) {
        try {
            Optional<Shopping_Cart> shoppingCartOptional = shoppingCartRepository.findById(id);
            if (!shoppingCartOptional.isPresent()) {
                return null;
            }

            int idCus = random();
            int idMer = random();
            Transaction transaction = new Transaction();
            transaction.setTransactionIdCustomer(String.valueOf(idCus));
            transaction.setTransactionIdMerchant(String.valueOf(idMer));

            Shopping_Cart shopping_cart = shoppingCartOptional.get();
            shopping_cart.setTransactionIdCustomer(Integer.toString(idCus));
            shopping_cart.setTransactionIdMerchant(Integer.toString(idMer));
            shoppingCartRepository.save(shopping_cart);

            return transaction;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<EncryptCusToMer> sendCusToMer(PaymentRequest paymentRequest) {
        try {
            //tạo mã công khai merchant
            byte[] plkMerchantByte = Base64.getDecoder().decode(publicKeyMerchant);
            X509EncodedKeySpec keySpecMer = new X509EncodedKeySpec(plkMerchantByte);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey plkMerchant = keyFactory.generatePublic(keySpecMer);

            // tạo mã công khai acquired
            byte[] plkAcquiredByte = Base64.getDecoder().decode(publicKeyAcquired);
            X509EncodedKeySpec keySpecAcq = new X509EncodedKeySpec(plkAcquiredByte);
            KeyFactory keyFactoryAcq = KeyFactory.getInstance("RSA");
            PublicKey plkAcquired = keyFactoryAcq.generatePublic(keySpecAcq);

            //tạo mã bí mật cho customer
            byte[] privateKeyCustomerBytes = Base64.getDecoder().decode(privateKeyCustomer);
            PKCS8EncodedKeySpec keySpecCustomer = new PKCS8EncodedKeySpec(privateKeyCustomerBytes);
            KeyFactory keyFactoryCustomer = KeyFactory.getInstance("RSA");
            PrivateKey privateKeyCustomer = keyFactoryCustomer.generatePrivate(keySpecCustomer);

            //tạo mã công khai customer
            byte[] plkCustomerByte = Base64.getDecoder().decode(publicKeyCustomer);
            X509EncodedKeySpec keySpecCus = new X509EncodedKeySpec(plkCustomerByte);
            KeyFactory keyFactoryCus = KeyFactory.getInstance("RSA");
            PublicKey plkCustomer = keyFactoryCus.generatePublic(keySpecCus);


            Cipher cipherMer = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipherMer.init(Cipher.ENCRYPT_MODE, plkMerchant);

            Cipher cipherAcquired = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipherAcquired.init(Cipher.ENCRYPT_MODE, plkAcquired);

            Optional<Shopping_Cart> shoppingCartOptional = shoppingCartRepository.findById(paymentRequest.getShoppingCartId());
            if (shoppingCartOptional.isPresent()) {
                Shopping_Cart shopping_cart = shoppingCartOptional.get();

                ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
                shoppingCartDTO.setId(shopping_cart.getId());
                shoppingCartDTO.setTransactionIdMerchant(shopping_cart.getTransactionIdMerchant());
                shoppingCartDTO.setTransactionIdCustomer(shopping_cart.getTransactionIdCustomer());

                SLIP slip = new SLIP();
                slip.setTransactionIdMerchant(shoppingCartDTO.getTransactionIdMerchant());
                slip.setTransactionIdCustomer(shoppingCartDTO.getTransactionIdCustomer());
                slip.setPinCode(paymentRequest.getPinCode());
                slip.setCreditCardNumber(paymentRequest.getCreditCardNumber());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(shoppingCartDTO);
                byte[] orderByte = byteArrayOutputStream.toByteArray(); // m hóa
                objectOutputStream.close();

                ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(byteArrayOutputStream1);
                objectOutputStream1.writeObject(slip);
                byte[] slipByte = byteArrayOutputStream1.toByteArray(); // m hóa
                objectOutputStream1.close();

                byte[] encryptOrder = cipherMer.doFinal(orderByte);
                byte[] encryptSlip = cipherAcquired.doFinal(slipByte);

                String encryptShoppingCart = Base64.getEncoder().encodeToString(encryptOrder);
                String encryptSlipS = Base64.getEncoder().encodeToString(encryptSlip);

                Signature signatureCustomer = Signature.getInstance("SHA256withRSA");
                signatureCustomer.initSign(privateKeyCustomer);

                String dataToVerify = encryptSlipS + "\\|" + encryptShoppingCart;
                signatureCustomer.update(dataToVerify.getBytes(StandardCharsets.UTF_8));

                // ký chữ ký và lấy dữ liệu
                byte[] signatureBytes = signatureCustomer.sign();
                String signatureString = Base64.getEncoder().encodeToString(signatureBytes);

                //lưu certC
                X509Certificate cert = generateSelfSignedCertificate(plkCustomer, privateKeyCustomer);
                String filePath = "CERTC.txt";
                saveCertificateToTxtFile(cert, filePath);

                // bản chứng thực CertC
                String certC = Base64.getEncoder().encodeToString(cert.getEncoded());

                EncryptCusToMer encryptCusToMer = new EncryptCusToMer();
                encryptCusToMer.setSignature(signatureString);
                encryptCusToMer.setCertC(certC);
                encryptCusToMer.setDataToVerify(dataToVerify);

                return new ResponseEntity<>(encryptCusToMer, HttpStatus.OK);

            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<MerToAcq> sendMerToAcq(String signature, String certC, String dataToVerify) {
        try {
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            byte[] certCBytes = Base64.getDecoder().decode(certC);

            //lấy ra khóa công khai của custem dựa vào certC
            X509Certificate publicKeyCert = getCertificateFromBytes(certCBytes);

            //Tạo đối tượng Signature và cấu hình để xác thực chữ ký
            Signature signature1 = Signature.getInstance("SHA256withRSA");
            signature1.initVerify(publicKeyCert);

            //tạo mã bí mật merchant
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyMerchant);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKeyMer = keyFactory.generatePrivate(keySpec);

            //tọa mã công khai của merchant
            byte[] pkMerchantByte = Base64.getDecoder().decode(publicKeyMerchant);
            X509EncodedKeySpec keySpec1 = new X509EncodedKeySpec(pkMerchantByte);
            KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
            PublicKey pbulicKeyMerchant = keyFactory1.generatePublic(keySpec1);


            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKeyMer);

            signature1.update(dataToVerify.getBytes("UTF-8"));

            if (signature1.verify(signatureBytes)) {
                String eaSlipAndEmOrder = dataToVerify;
                String[] part = eaSlipAndEmOrder.split("\\|");
                if (part.length == 2) {
                    String eaSlip = part[0];
                    String emOrder = part[1]; //base64

                    byte[] encryptByte = Base64.getDecoder().decode(emOrder);
                    int t = encryptByte.length;
                    byte[] decryptByte = cipher.doFinal(encryptByte);

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptByte);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    ShoppingCartDTO shoppingCartDTO = (ShoppingCartDTO) objectInputStream.readObject();
                    objectInputStream.close();

                    Gson gson = new Gson();
                    String shoppingCartJson = gson.toJson(shoppingCartDTO);

                    //hash(Order) dạng StringBuilder.toString
                    String hashShoppingCart = calculateSHA256Hash(shoppingCartJson);
                    String hashShoppingCartBase64 = Base64.getEncoder().encodeToString(hashShoppingCart.getBytes());

                    String dataToCheck = eaSlip + "\\|" + hashShoppingCartBase64;

                    Signature signature2 = Signature.getInstance("SHA256withRSA");
                    signature2.initSign(privateKeyMer);
                    signature2.update(dataToCheck.getBytes(StandardCharsets.UTF_8));

                    byte[] signatureToMerBytes = signature2.sign();

                    // Chuyển đổi chữ ký số thành chuỗi Base64
                    String signatureBase64 = Base64.getEncoder().encodeToString(signatureToMerBytes);

                    //tạo certM
                    X509Certificate cert = generateSelfSignedCertificate(pbulicKeyMerchant, privateKeyMer);
                    String filePath = "CERTM.txt";
                    saveCertificateToTxtFile(cert, filePath);

                    // bản chứng thực String
                    String certM = Base64.getEncoder().encodeToString(cert.getEncoded());

                    MerToAcq merToAcq = new MerToAcq();
                    merToAcq.setSignature(signatureBase64);
                    merToAcq.setCertM(certM);
                    merToAcq.setDataToVerify(dataToCheck);

                    return new ResponseEntity<>(merToAcq, HttpStatus.OK);
                } else {
                    System.out.println("không thể tách");
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }
            } else {
                System.out.println("xác nhận chữ kí thất bại");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<AcqToMer> sendAcqToMer(String signature, String certM, String dataVerify) {
        try {
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            byte[] certBytes = Base64.getDecoder().decode(certM);

            //lấy khóa công khai từ certM
            X509Certificate publicKeyCert = getCertificateFromBytes(certBytes);

            //lấy khóa bí mật để tạo chữ kis số
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyMerchant);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKeyMer = keyFactory.generatePrivate(keySpec);

            //tạo khóa bí mật acquired giải mã eaSlip
            byte[] privateKeyBytesAcq = Base64.getDecoder().decode(privateKeyAcquired);
            PKCS8EncodedKeySpec keySpecAcq = new PKCS8EncodedKeySpec(privateKeyBytesAcq);
            KeyFactory keyFactoryAcq = KeyFactory.getInstance("RSA");
            PrivateKey privateKeyAcq = keyFactoryAcq.generatePrivate(keySpecAcq);

            //Tạo đối tượng Signature và cấu hình để xác thực chữ ký
            Signature signature1 = Signature.getInstance("SHA256withRSA");
            signature1.initVerify(publicKeyCert);

            signature1.update(dataVerify.getBytes("UTF-8"));
            Auth auth = new Auth();

            if (signature1.verify(signatureBytes)) {
                String dataCheck = dataVerify;
                String[] parts = dataCheck.split("\\|");
                if (parts.length == 2) {
                    String eaSlipResult = parts[0];
                    String eaSlip = eaSlipResult.replaceAll("\\\\","");
                    System.out.println(eaSlip);
                    String hashOrder = parts[1];

                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    cipher.init(Cipher.DECRYPT_MODE, privateKeyAcq);

                    byte[] eaSlipBytes = Base64.getDecoder().decode(eaSlip);
                    byte[] decryptedDataAcq = cipher.doFinal(eaSlipBytes);

                    SLIP slip = deserialize(decryptedDataAcq);

                    if (isValidSlip(slip)) {
                        auth.setReplyInfo("Confirmed successfully");
                        byte[] authBytes = convertAuthToBytes(auth);

                        //chuyển authbyte thành base64
                        String authBase64 = Base64.getEncoder().encodeToString(authBytes);
                        String dataToVerify = authBase64 + "\\|" + eaSlip + "\\|" + hashOrder;

                        //tạo signature cho tạo chữ kí số SA()
                        Signature signatureAcq = Signature.getInstance("SHA256withRSA");
                        signatureAcq.initSign(privateKeyMer);
                        signatureAcq.update(dataToVerify.getBytes(StandardCharsets.UTF_8));

                        //tạo chữ kí số
                        byte[] signatureAcqToMer = signatureAcq.sign();

                        //chuyển chữ kí soso thành base64
                        String signatureBase64 = Base64.getEncoder().encodeToString(signatureAcqToMer);
                        AcqToMer acqToMer = new AcqToMer();
                        acqToMer.setAuth("Confirmed successfully");
                        acqToMer.setSignature(signatureBase64);
                        acqToMer.setDataVerify(dataToVerify);
                        return new ResponseEntity<>(acqToMer, HttpStatus.OK);

                    } else {
                        System.out.println("thông tin slip không đúng");
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }

                }else {
                    System.out.println("không thể tách mã data");
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }
            } else {
                System.out.println("không giải mã được chữ ký số");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<MerToCus> sendMerToCus(String signature) {
        try {
            //tạo khóa bí mật merchant tạo chữ kí số
            byte[] privateKeyBytesMer = Base64.getDecoder().decode(privateKeyMerchant);
            PKCS8EncodedKeySpec keySpecMer = new PKCS8EncodedKeySpec(privateKeyBytesMer);
            KeyFactory keyFactoryMer = KeyFactory.getInstance("RSA");
            PrivateKey privateKeyMer = keyFactoryMer.generatePrivate(keySpecMer);
            String signatureMerchant = signature;

            //tạo signature tạo chữ kí số
            Signature signatureMer = Signature.getInstance("SHA256withRSA");
            signatureMer.initSign(privateKeyMer);
            signatureMer.update(signatureMerchant.getBytes(StandardCharsets.UTF_8));

            //tạo chữ kí số
            byte[] signatureMerToCus = signatureMer.sign();

            String signatureBase64 = Base64.getEncoder().encodeToString(signatureMerToCus);
            String dataCheck = signatureMerchant;
            MerToCus merToCus = new MerToCus();
            merToCus.setDataVerify(dataCheck);
            merToCus.setSignature(signatureBase64);
            return new ResponseEntity<>(merToCus, HttpStatus.OK);

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Cus> notifyCustomer(String signature, String dataVerify) {
        try {
            byte[] signatureBytes = Base64.getDecoder().decode(signature);

            //tạo khóa công khai merchant
            byte[] pkMerchantByte = Base64.getDecoder().decode(publicKeyMerchant);
            X509EncodedKeySpec keySpec1 = new X509EncodedKeySpec(pkMerchantByte);
            KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
            PublicKey pbulicKeyMerchant = keyFactory1.generatePublic(keySpec1);

            Signature signature1 = Signature.getInstance("SHA256withRSA");
            signature1.initVerify(pbulicKeyMerchant);
            signature1.update(dataVerify.getBytes("UTF-8"));

            if (signature1.verify(signatureBytes)) {
                Cus cus = new Cus();
                cus.setNotify("Confirmed successfully");
                return new ResponseEntity<>(cus, HttpStatus.OK);
            } else {
                System.out.println("xác thực chữ kí thất bại");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public static boolean isValidSlip(SLIP slip) {
        if (slip.getCreditCardNumber().length() != 16) {
            return false;
        }
        if (slip.getPinCode().length() != 4) {
            return false;
        }
        return true;
    }
    public static SLIP deserialize(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        SLIP slip = (SLIP) objectInputStream.readObject(); // Ép kiểu về SLIP
        objectInputStream.close();
        byteInputStream.close();
        return slip;
    }
    public static byte[] convertAuthToBytes(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            byte[] authByte = byteArrayOutputStream.toByteArray(); // m hóa
            objectOutputStream.close();
            return authByte;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String calculateSHA256Hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Chuyển đổi byte[] thành chuỗi hex
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    private static X509Certificate getCertificateFromBytes(byte[] certificateBytes) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bais = new ByteArrayInputStream(certificateBytes);
        return (X509Certificate) certificateFactory.generateCertificate(bais);
    }

    public static X509Certificate generateSelfSignedCertificate(PublicKey publicKey, PrivateKey privateKey)
            throws Exception {
        // Tạo một bản chứng thực X.509
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        X500Principal dnName = new X500Principal("CN=MyCert");

        // Cài đặt thông tin chứng thực
        certGen.setSerialNumber(new BigInteger(32, new SecureRandom()));
        certGen.setSubjectDN(dnName);
        certGen.setIssuerDN(dnName);
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30)); // Ngày hiện tại trừ 30 ngày
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)); // Ngày hiện tại cộng 1 năm
        certGen.setPublicKey(publicKey);
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        // Tạo chứng thực tự ký bằng cách ký bằng khóa bí mật
        X509Certificate cert = certGen.generate(privateKey, "BC");

        return cert;
    }

    public static void saveCertificateToTxtFile(X509Certificate certificate, String filePath) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Ghi chứng thực vào tệp văn bản
            writer.write("-----BEGIN CERTIFICATE-----");
            writer.newLine();
            writer.write(Base64.getEncoder().encodeToString(certificate.getEncoded()));
            writer.newLine();
            writer.write("-----END CERTIFICATE-----");
            writer.newLine();
        }
    }

    public Integer random() {
        Random random = new Random();
        return random.nextInt(999999 - 100000) + 100000;
    }

//    public static void main(String[] args) throws Exception {
//        int keySize = 4096;
//
//        // Tạo một đối tượng KeyPairGenerator với thuật toán RSA
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(keySize);
//
//        // Tạo cặp khóa công khai và bí mật cho Customer
//        KeyPair customerKeyPair = keyPairGenerator.generateKeyPair();
//        PublicKey customerPublicKey = customerKeyPair.getPublic();
//        PrivateKey customerPrivateKey = customerKeyPair.getPrivate();
//
//        // Tạo cặp khóa công khai và bí mật cho Merchant
//        KeyPair merchantKeyPair = keyPairGenerator.generateKeyPair();
//        PublicKey merchantPublicKey = merchantKeyPair.getPublic();
//        PrivateKey merchantPrivateKey = merchantKeyPair.getPrivate();
//
//        //tạo cặp khóa cho Acquired
//        KeyPair acquiredKeyPair = keyPairGenerator.generateKeyPair();
//        PublicKey acquiredPublicKey = acquiredKeyPair.getPublic();
//        PrivateKey acquiredPrivateKey = acquiredKeyPair.getPrivate();
//
//        //khóa công khai
//        String plkCustomer = Base64.getEncoder().encodeToString(customerPublicKey.getEncoded());
//        String plkMerchant = Base64.getEncoder().encodeToString(merchantPublicKey.getEncoded());
//        String plkAcquired = Base64.getEncoder().encodeToString(acquiredPublicKey.getEncoded());
//
//        //khóa bí mật
//        String prkCustomer = Base64.getEncoder().encodeToString(customerPrivateKey.getEncoded());
//        String prkMerchant = Base64.getEncoder().encodeToString(merchantPrivateKey.getEncoded());
//        String prkAcquired = Base64.getEncoder().encodeToString(acquiredPrivateKey.getEncoded());
//
//
//        saveKeyToFile("keyPairCustomer.txt", plkCustomer + "\n" + prkCustomer);
//        saveKeyToFile("keyPairMerchant.txt", plkMerchant + "\n" + prkMerchant);
//        saveKeyToFile("keyPairAcquired.txt", plkAcquired + "\n" + prkAcquired);
//    }
//
//    private static void saveKeyToFile(String fileName, String key) throws Exception {
//        try (FileWriter fileWriter = new FileWriter(fileName)) {
//            fileWriter.write(key);
//        }
//    }
}
