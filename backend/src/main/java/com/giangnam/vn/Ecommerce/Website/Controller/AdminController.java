package com.giangnam.vn.Ecommerce.Website.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.giangnam.vn.Ecommerce.Website.DTO.CategoryDTO;
import com.giangnam.vn.Ecommerce.Website.DTO.LoginDTO;
import com.giangnam.vn.Ecommerce.Website.DTO.OrderDTO;
import com.giangnam.vn.Ecommerce.Website.DTO.OrderInfoDTO;
import com.giangnam.vn.Ecommerce.Website.DTO.ProductDTO;
import com.giangnam.vn.Ecommerce.Website.DTO.ResponseDTO;
import com.giangnam.vn.Ecommerce.Website.Service.ProductService;
import com.giangnam.vn.Ecommerce.Website.Service.UserService;

@RestController
public class AdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/admin/login")
	@CrossOrigin
	public ResponseEntity<ResponseDTO> loginAdmin(@RequestBody LoginDTO loginDTO){
		String result = userService.verifyUser(loginDTO, true);
		if (result.equals("Sai ten dang nhap hoac mat khau")) return new ResponseEntity<ResponseDTO>(new ResponseDTO("Sai ten dang nhap hoac mat khau", ""), HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<ResponseDTO>(new ResponseDTO("Dang nhap thanh cong!!!", result), HttpStatus.OK);
	}
	
	@GetMapping("/admin/total")
	@CrossOrigin
	public double getRevenue(@RequestHeader("Authorization") String token){
		return userService.getAllRevenue(token.substring(7));
	}
	
	@GetMapping("/admin/user-create/month")
	@CrossOrigin
	public double getUserCreateThisMonth(@RequestHeader("Authorization") String token){
		return userService.getAllNewUserThisMonth(token.substring(7));
	}
	
	@GetMapping("/admin/user-create/year")
	@CrossOrigin
	public double getUserCreateThisYear(@RequestHeader("Authorization") String token){
		return userService.getAllNewUserThisYear(token.substring(7));
	}
	
	@GetMapping("/admin/order-info")
	@CrossOrigin
	public List<OrderInfoDTO> getOrderInfoAdmin(@RequestHeader("Authorization") String token){
		return userService.getOrderInfoAdmin(token.substring(7));
	}
	
	@GetMapping("/admin/order")
	@CrossOrigin
	public List<OrderDTO> getAllOrderAdmin(@RequestHeader("Authorization") String token){
		return userService.getAllOrderAdmin(token.substring(7));
	}
	
	@GetMapping("/admin/near-order")
	@CrossOrigin
	public List<OrderDTO> getNearOrderAdmin(@RequestHeader("Authorization") String token){
		List<OrderDTO> result = userService.getAllOrderAdmin(token.substring(7));
		return result.size() != 0 ? result.subList(Math.max(0,result.size()-5), result.size()-1) : result;
	}
	
	@PostMapping("/admin/add-new-category")
	@CrossOrigin
	public ResponseEntity<String> addNewCategory(@RequestHeader("Authorization") String token, @RequestBody CategoryDTO categoryDTO){
		String result = userService.addNewCategory(token.substring(7), categoryDTO);
		//System.out.println(result);
		if (!result.equals("Thêm loại sản phẩm thành công!!!")) return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@PutMapping(value="/admin/product/{id}", consumes = "multipart/form-data")
	@CrossOrigin
	public ResponseEntity<String> updateProduct(@PathVariable String id, @ModelAttribute ProductDTO productDTO){
		String result = productService.updateProduct(productDTO);
		System.out.println(result);
		if (!result.equals("Cập nhật sản phẩm thành công!!!")) return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
}
