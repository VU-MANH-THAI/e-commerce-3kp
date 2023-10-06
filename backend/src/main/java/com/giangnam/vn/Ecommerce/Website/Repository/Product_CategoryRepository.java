package com.giangnam.vn.Ecommerce.Website.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.Product_Category;

@Repository
public interface Product_CategoryRepository extends JpaRepository<Product_Category, Integer >{

	Product_Category findByCategoryName(String categoryName);
}
