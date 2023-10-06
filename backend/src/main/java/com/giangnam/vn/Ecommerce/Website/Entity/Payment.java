package com.giangnam.vn.Ecommerce.Website.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.time.LocalDate;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate paymentDate;
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shopping_cart_id")
    @JsonBackReference(value = "payment")
    Shopping_Cart shopping_cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    @JsonBackReference(value = "payment")
    User user;
}
