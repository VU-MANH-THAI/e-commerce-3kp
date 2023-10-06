package com.giangnam.vn.Ecommerce.Website.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer > {
 
	List<Product> findByName(String name);
	
	List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<Product> findByDescriptionContainingIgnoreCaseOrderByDescriptionAsc(String description);

    List<Product> findByDescriptionContainingIgnoreCaseOrderByDescriptionDesc(String description);
    
    List<Product> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCaseOrderByNameAsc(String name, String description);

}
