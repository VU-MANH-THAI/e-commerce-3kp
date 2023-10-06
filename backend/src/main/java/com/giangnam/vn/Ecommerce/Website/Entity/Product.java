package com.giangnam.vn.Ecommerce.Website.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(orphanRemoval = true, mappedBy = "product", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Product_Item> productItemList;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	@JsonBackReference
	private Product_Category category;
	
	@NotNull
	private String name;
	
	private String description;
	
	private String image;
	
	public Product() {
		super();
		productItemList = new ArrayList<>();
	}
	
	public void addProductItem(Product_Item product_Item) {
		productItemList.add(product_Item);
		product_Item.setProduct(this);
	}
	
	public void removeProductItem(Product_Item product_Item) {
		productItemList.add(product_Item);
		product_Item.setProduct(null);
	}
	
}
