package com.giangnam.vn.Ecommerce.Website.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.giangnam.vn.Ecommerce.Website.Entity.CompositeKey.ShoppingProductKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shopping_Cart_Item implements Serializable {

	@EmbeddedId
	private ShoppingProductKey id;
	
	@ManyToOne
    @MapsId("shoppingId")
    @JoinColumn(name = "shopping_id")
	@JsonBackReference
    Shopping_Cart shoppingCart;
	
	@ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
	@JsonBackReference
    Product_Item productItem;
	
	@Min(value = 0)
	private int quantity;
}
