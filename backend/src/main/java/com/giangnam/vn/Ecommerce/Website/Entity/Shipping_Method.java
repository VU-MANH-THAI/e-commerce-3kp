package com.giangnam.vn.Ecommerce.Website.Entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shipping_Method implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(orphanRemoval = true, mappedBy = "shippingMethod")
	@JsonManagedReference
	private List<Shop_Order> orderList;
	
	@NotNull
	private String name;
	
	@Min(value = 0)
	private double price;
	
	public void addShopOrder(Shop_Order shop_Order) {
		orderList.add(shop_Order);
		shop_Order.setShippingMethod(this);
	}
	
	public void removeShopOrder(Shop_Order shop_Order) {
		orderList.remove(shop_Order);
		shop_Order.setShippingMethod(null);
	}
}
