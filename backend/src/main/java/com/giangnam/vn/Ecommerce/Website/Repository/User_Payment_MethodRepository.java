package com.giangnam.vn.Ecommerce.Website.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.User_Payment_Method;

@Repository
public interface User_Payment_MethodRepository extends JpaRepository<User_Payment_Method, Integer >{

}
