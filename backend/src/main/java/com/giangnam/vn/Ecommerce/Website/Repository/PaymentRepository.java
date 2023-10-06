package com.giangnam.vn.Ecommerce.Website.Repository;

import com.giangnam.vn.Ecommerce.Website.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
