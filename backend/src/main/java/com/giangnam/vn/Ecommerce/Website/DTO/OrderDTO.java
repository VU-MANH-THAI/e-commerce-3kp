package com.giangnam.vn.Ecommerce.Website.DTO;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

	private int id;
	private String userEmail;
	private String shippingMethod;
	private String shippingAddress;
	private int total;
	private String orderStatus;
	private Date orderDate;
	private List<ProductDTO> productList;
	
}
