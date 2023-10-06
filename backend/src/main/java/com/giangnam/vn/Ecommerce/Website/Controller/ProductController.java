package com.giangnam.vn.Ecommerce.Website.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giangnam.vn.Ecommerce.Website.Config.JWTUtils;
import com.giangnam.vn.Ecommerce.Website.DTO.ProductDTO;
import com.giangnam.vn.Ecommerce.Website.DTO.PromotionDTO;
import com.giangnam.vn.Ecommerce.Website.Service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@GetMapping(value = "/product")
	@CrossOrigin
	List<ProductDTO> getAllProduct(){
		return productService.findAllProduct();
	}
	
	@GetMapping("/product/{id}")
	@CrossOrigin
	ProductDTO getProductById(@PathVariable int id) {
		return productService.findProductById(id);
	}
	
	@GetMapping("/promotion")
	@CrossOrigin
	List<PromotionDTO> getAllPromotion(){
		return productService.findAllPromotion();
	}
	
	@GetMapping("/category")
	@CrossOrigin
	List<String> getAllCategory(){
		return productService.findAllCategory();
	}
	
	@PostMapping(value = "/product", consumes = "multipart/form-data")
	@CrossOrigin
	public String createNewProduct(@ModelAttribute ProductDTO productDTO) {
		return productService.addNewProduct(productDTO);
	}
	
	@DeleteMapping("/product/{id}")
	@CrossOrigin
	public String deleteProduct(@PathVariable int id) {
		String result = productService.deleteProduct(id);
		System.out.println(result);
		return result;
	}
	
	@DeleteMapping("/promotion/{id}")
	@CrossOrigin
	public String deletePromotion(@PathVariable int id) {
		String result = productService.deletePromotion(id);
		System.out.println(result);
		return result;
	}
	
}
