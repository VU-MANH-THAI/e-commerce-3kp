package com.giangnam.vn.Ecommerce.Website.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {

	private int id;
	private String name;
	private String description;
	private String category;
	private double price;
	private int sold_quantity;
	private double total;
}
