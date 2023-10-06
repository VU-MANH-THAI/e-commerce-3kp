package com.giangnam.vn.Ecommerce.Website.Entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment_Type implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(orphanRemoval = true,mappedBy = "paymentType")
	@JsonManagedReference
	private List<User_Payment_Method> userPaymentMethodList;
	
	@NotNull
	private String name;
	
	public void addUserPaymentMethod(User_Payment_Method userPaymentMethod) {
		userPaymentMethodList.add(userPaymentMethod);
		userPaymentMethod.setPaymentType(this);
	}
	
	public void removeUserPaymentMethod(User_Payment_Method userPaymentMethod) {
		userPaymentMethodList.remove(userPaymentMethod);
		userPaymentMethod.setPaymentType(null);
	}
}
