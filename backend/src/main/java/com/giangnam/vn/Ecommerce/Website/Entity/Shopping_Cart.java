package com.giangnam.vn.Ecommerce.Website.Entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shopping_Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user2")
    private User user;

    @OneToMany(orphanRemoval = true, mappedBy = "shoppingCart")
    @JsonManagedReference
    private List<Shopping_Cart_Item> shoppingCartItemList;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "shopping_order_id", referencedColumnName = "id")
    private Shop_Order shop_Order;

    @OneToMany(mappedBy = "shopping_cart")
    @JsonManagedReference(value = "shopping_cart")
    List<Payment> shopping_cart;

    private String transactionIdMerchant;
    private String transactionIdCustomer;

    public void addShoppingCartItem(Shopping_Cart_Item shopping_Cart_Item) {
        shoppingCartItemList.add(shopping_Cart_Item);
        shopping_Cart_Item.setShoppingCart(this);
    }

    public void removeShoppingCartItem(Shopping_Cart_Item shopping_Cart_Item) {
        shoppingCartItemList.remove(shopping_Cart_Item);
        shopping_Cart_Item.setShoppingCart(null);
    }
}
