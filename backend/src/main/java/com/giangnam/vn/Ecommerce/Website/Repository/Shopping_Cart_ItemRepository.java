package com.giangnam.vn.Ecommerce.Website.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.Shopping_Cart_Item;
import com.giangnam.vn.Ecommerce.Website.Entity.CompositeKey.ShoppingProductKey;

@Repository
public interface Shopping_Cart_ItemRepository extends JpaRepository<Shopping_Cart_Item, ShoppingProductKey >{

}
