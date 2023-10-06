package com.giangnam.vn.Ecommerce.Website.Entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shop_Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference(value = "user3")
	private User user;

	@ManyToOne
	@JoinColumn(name = "shipping_method_id")
	@JsonBackReference
	private Shipping_Method shippingMethod;

	@OneToOne(mappedBy = "shop_Order", cascade = CascadeType.ALL)
	private Shopping_Cart shopping_Cart;

	private Date orderDate;

	@NotNull
	private String shipping_address;

	@Min(value = 0)
	private int orderTotal;

	private String orderStatus;

	public Shop_Order(User user, Shipping_Method shippingMethod, Shopping_Cart shopping_Cart, Date orderDate,
			@NotNull String shipping_address, @Min(0) int orderTotal, String orderStatus) {
		super();
		this.user = user;
		this.shippingMethod = shippingMethod;
		this.shopping_Cart = shopping_Cart;
		this.orderDate = orderDate;
		this.shipping_address = shipping_address;
		this.orderTotal = orderTotal;
		this.orderStatus = orderStatus;
	}


}
