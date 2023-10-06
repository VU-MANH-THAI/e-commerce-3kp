package com.giangnam.vn.Ecommerce.Website.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.Product_Item;

@Repository
public interface Product_ItemRepository extends JpaRepository<Product_Item, Integer > {

}
