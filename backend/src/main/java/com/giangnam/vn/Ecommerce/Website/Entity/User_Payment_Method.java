package com.giangnam.vn.Ecommerce.Website.Entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User_Payment_Method implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	@JsonBackReference(value = "user1")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="payment_type_id")
	@JsonBackReference(value = "user2")
	private Payment_Type paymentType;
	
	private String provider;
	
	private String accountNumber;
	
	private Date expiryDate;
	
	private Boolean isDefault;
}
