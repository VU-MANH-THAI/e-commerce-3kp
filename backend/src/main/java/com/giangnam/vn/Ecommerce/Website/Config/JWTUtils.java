package com.giangnam.vn.Ecommerce.Website.Config;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Service
public class JWTUtils {

	 // Đoạn mã bí mật (Secret key) - Dùng để mã hóa và giải mã JWT token
    private static final String SECRET_KEY = "ecommercewebsitesecretkey1234567891234567";

    // Thời gian hết hạn của JWT token (miliseconds)
    private static final long EXPIRATION_TIME = 30*60 * 1000; // 30 phút

    // Phương thức xác thực JWT token
    public static String generateJwtToken(String subject) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        Date now = new Date();
        Date expire = new Date(now.getTime() + EXPIRATION_TIME);
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(expire)
                .sign(algorithm);
    }
    
    public static boolean validateJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            // Kiểm tra thời gian hết hạn
            Date expirationDate = decodedJWT.getExpiresAt();
            Date currentDate = new Date();
            if (expirationDate != null && expirationDate.after(currentDate)) {
                return true; // Token hợp lệ và chưa hết hạn
            } else {
                return false; // Token đã hết hạn
            }
        } catch (JWTVerificationException ex) {
            // Xác thực không thành công, có thể là token không hợp lệ hoặc đã hết hạn
            return false;
        }
    }
    
    // Xác thực và lấy thông tin từ JWT
    public static String parseJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            
            return jwt.getSubject();
        } catch (Exception ex) {
            // Xác thực không thành công, có thể là token không hợp lệ hoặc đã hết hạn
            return null;
        }
    }
}
