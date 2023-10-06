package com.giangnam.vn.Ecommerce.Website.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {

	private int id;
	private String name;
	private String description;
	private String categoryName;
	private double discount;
	private Date startDate;
	private Date endDate;
}
