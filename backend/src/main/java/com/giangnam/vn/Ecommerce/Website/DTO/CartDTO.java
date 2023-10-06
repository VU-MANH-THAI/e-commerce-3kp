package com.giangnam.vn.Ecommerce.Website.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

	private String address;
	private String phonenumber;
	private String shippingMethod;
	private String paymentMethod;
}
