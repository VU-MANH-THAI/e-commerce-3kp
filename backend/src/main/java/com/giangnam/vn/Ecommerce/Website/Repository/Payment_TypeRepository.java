package com.giangnam.vn.Ecommerce.Website.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.Payment_Type;

@Repository
public interface Payment_TypeRepository extends JpaRepository<Payment_Type, Integer> {

}
