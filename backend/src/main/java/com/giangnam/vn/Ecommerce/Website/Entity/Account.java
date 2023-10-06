package com.giangnam.vn.Ecommerce.Website.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String creditCardNumber = "2727-2890-0006-8094";
    private String pinCode = "1234";

    @OneToOne(mappedBy = "account")
    private User user;
}
