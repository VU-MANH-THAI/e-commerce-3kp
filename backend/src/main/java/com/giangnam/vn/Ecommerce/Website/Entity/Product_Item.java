package com.giangnam.vn.Ecommerce.Website.Entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product_Item implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(orphanRemoval = true, mappedBy = "productItem")
	@JsonManagedReference
	private List<Shopping_Cart_Item> shoppingCartItemList;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Product product;
	
	@Min(value = 0)
	private int quantityStock;
	
	private String productImage;
	
	@Min(value = 0)
	private double price;
	
	public void addShoppingCartItem(Shopping_Cart_Item shopping_Cart_Item) {
		shoppingCartItemList.add(shopping_Cart_Item);
		shopping_Cart_Item.setProductItem(this);
	}
	
	public void removeShoppingCartItem(Shopping_Cart_Item shopping_Cart_Item) {
		shoppingCartItemList.remove(shopping_Cart_Item);
		shopping_Cart_Item.setProductItem(null);
	} 
}
