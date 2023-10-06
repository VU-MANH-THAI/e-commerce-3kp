package com.giangnam.vn.Ecommerce.Website.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer > {

	
	User findByEmail(String email);

    List<User> findByRole(String role);
    
    List<User> findByAddress(String address);
}
