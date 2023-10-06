package com.giangnam.vn.Ecommerce.Website.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user1")
    private List<User_Payment_Method> paymentList;

    @OneToMany(orphanRemoval = true, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user2")
    private List<Shopping_Cart> cartList;

    @OneToMany(orphanRemoval = true, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user3")
    private List<Shop_Order> orderList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user")
    private List<Payment> payment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountid", referencedColumnName = "id")
    private Account account;

    @Email
    @Column(unique = true)
    @NotBlank
    private String email;

    @Size(min = 10, max = 11)
    private String phoneNumber;

    private String address;

    private String firstName;

    private String lastName;

    @Size(min = 8)
    private String password;

    private String verifyToken;

    private boolean isActive;

    private String role;

    private Date createdAt;

    public void addUserPaymentMethod(User_Payment_Method user_Payment_Method) {
        paymentList.add(user_Payment_Method);
        user_Payment_Method.setUser(this);
    }

    public void removeUserPaymentMethod(User_Payment_Method user_Payment_Method) {
        paymentList.remove(user_Payment_Method);
        user_Payment_Method.setUser(null);
    }

    public void addShoppingCart(Shopping_Cart shopping_Cart) {
        cartList.add(shopping_Cart);
        shopping_Cart.setUser(this);
    }

    public void removeShoppingCart(Shopping_Cart shopping_Cart) {
        cartList.remove(shopping_Cart);
        shopping_Cart.setUser(null);
    }

    public void addShopOrder(Shop_Order shop_Order) {
        orderList.add(shop_Order);
        shop_Order.setUser(this);
    }

    public void removeShopOrder(Shop_Order shop_Order) {
        orderList.remove(shop_Order);
        shop_Order.setUser(null);
    }

    public User(@Email @NotBlank String email, @Size(min = 10, max = 11) String phoneNumber, String address,
                String firstName, String lastName, String password, String role, String verifyToken, boolean isActive) {
        super();
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.verifyToken = verifyToken;
        this.createdAt = new Date();
    }

}
