package com.giangnam.vn.Ecommerce.Website.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

	private String newPassword;
	private String confirmNewPassword;
	private String confirmToken;
}
