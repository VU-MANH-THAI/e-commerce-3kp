package com.giangnam.vn.Ecommerce.Website.Entity.CompositeKey;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ShoppingProductKey implements Serializable {

	@Column(name="shop_order_id")
	private Integer shoppingId;
	@Column(name="product_id")
	private Integer productId;
	
}
