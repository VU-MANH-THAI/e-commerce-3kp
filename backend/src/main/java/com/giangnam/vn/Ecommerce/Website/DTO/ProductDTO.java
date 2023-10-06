package com.giangnam.vn.Ecommerce.Website.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	private int id;
	private String name;
	private String description;
	private Object image;
	private String categoryName;
	private double price;
	private int quantityStock;
	
}
