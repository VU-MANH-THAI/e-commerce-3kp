package com.giangnam.vn.Ecommerce.Website.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.Shopping_Cart;

@Repository
public interface Shopping_CartRepository extends JpaRepository<Shopping_Cart, Integer >{

}
