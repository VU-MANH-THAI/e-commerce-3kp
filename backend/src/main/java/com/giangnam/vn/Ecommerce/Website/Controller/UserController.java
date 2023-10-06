package com.giangnam.vn.Ecommerce.Website.Controller;

import java.util.List;
import java.util.UUID;

import com.giangnam.vn.Ecommerce.Website.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.giangnam.vn.Ecommerce.Website.Entity.User;
import com.giangnam.vn.Ecommerce.Website.Repository.UserRepository;
import com.giangnam.vn.Ecommerce.Website.Service.EmailService;
import com.giangnam.vn.Ecommerce.Website.Service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping(value = "/user")
	@CrossOrigin
	public ResponseEntity<String> createNewUser(@RequestBody UserDTO user) {
		String random = "Mã xác minh cho tài khoản của bạn là: " + UUID.randomUUID().toString();
		String result = userService.createNewUser(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getAddress(), random.substring(38));
		if (result.equals("Success")) 
		{
			emailService.sendSimpleMessage(user.getEmail(), "Mã xác minh cho đăng ký tài khoản mới ", random);
			return new ResponseEntity<String>(result, HttpStatus.OK);
		}
		return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/send-confirmation")
	@CrossOrigin
	public ResponseEntity<String> sendConfirmationEmail(@RequestHeader("Authorization") String token){
		if (!userService.isValidJWT(token.substring(7))) return new ResponseEntity<String>("Token khong hop le!!!", HttpStatus.BAD_REQUEST);
		String random = "Mã xác minh cho tài khoản của bạn là: " + UUID.randomUUID().toString();
		emailService.sendSimpleMessage(userService.getInforJWT(token.substring(7)), "Mã xác minh cho việc thay đổi mật khẩu ", random);
		User user = userRepository.findByEmail(userService.getInforJWT(token.substring(7)));
		user.setVerifyToken(random.substring(38));
		userRepository.save(user);
		return new ResponseEntity<String>("Gửi mã xác nhận thành công", HttpStatus.OK);
	}
	
	@PostMapping("/change-password")
	@CrossOrigin
	public ResponseEntity<String> changePasswordUser(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordDTO changePasswordDTO){
		if (!userService.isValidJWT(token.substring(7))) return new ResponseEntity<String>("Token khong hop le!!!", HttpStatus.BAD_REQUEST);
		if(!userService.changePasswordUser(userService.getInforJWT(token.substring(7)), changePasswordDTO)) return new ResponseEntity<String>("Thay đổi mật khẩu thất bại!!!", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<String>("Thay đổi mật khẩu thành công!!!", HttpStatus.OK);
	}
	
	@GetMapping("/active/{email}/{verifyToken}")
	@CrossOrigin
	public ResponseEntity<String> activeUser(@PathVariable String email,@PathVariable String verifyToken){
		if (userService.activeUser(email, verifyToken).equals("Active tài khoản thất bại!!!")) return new ResponseEntity<String>("Active tài khoản thất bại!!!", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<String>("Active tài khoản thành công!!!", HttpStatus.OK);
	}
	
	@GetMapping("/user")
	@CrossOrigin
	public List<UserDTO> listAllUser(){
		return userService.findAllUser();
	}
	
	@GetMapping("/user/{email}")
	@CrossOrigin
	public UserDTO findUserByEmail(@RequestHeader("Authorization") String token,@PathVariable String email) {
		if (!userService.isValidJWT(token.substring(7))) return null;
		return userService.toDTO(userService.findByEmail(email));
	}
	
	@PostMapping("/user/check-out")
	@CrossOrigin
	public ResponseEntity<String> checkOutCartUser(@RequestHeader("Authorization") String token, @RequestBody CartDTO cartDTO){
		if (!userService.isValidJWT(token.substring(7))) return new ResponseEntity<String>("Token khong hop le!!!", HttpStatus.BAD_REQUEST);
		if (!userService.checkOutCartUser(userService.getInforJWT(token.substring(7)), cartDTO)) return new ResponseEntity<String>("Check out thất bại!!!", HttpStatus.BAD_REQUEST);  
		return new ResponseEntity<String>("Check out thành công!!!", HttpStatus.OK); 
	}

	@GetMapping("/get-cart-id")
	@CrossOrigin
	public ResponseEntity<CartIdDTO>  getCartId (@RequestHeader("Authorization") String token){
		if (!userService.isValidJWT(token.substring(7))) return null;
		return new ResponseEntity<>(userService.getCartId(userService.getInforJWT(token.substring(7))),HttpStatus.OK);
	}
	@PostMapping("/user/{email}")
	@CrossOrigin
	public ResponseEntity<String> updateInfoUser(@RequestHeader("Authorization") String token,@PathVariable String email, @RequestBody UserDTO userDTO) {
		if (!userService.isValidJWT(token.substring(7))) return new ResponseEntity<String>("Token khong hop le!!!", HttpStatus.BAD_REQUEST);
		if (userService.changePhoneNumberUser(email, userDTO.getPhoneNumber()) && userService.changeAddressUser(email, userDTO.getAddress()) &&
			userService.changeFirstNameUser(email, userDTO.getFirstName()) && userService.changeLastNameUser(email, userDTO.getLastName()) )
			return new ResponseEntity<String>("Cap nhat thong tin thanh cong!!!", HttpStatus.OK);
		return new ResponseEntity<String>("Cap nhat thong tin that bai!!!", HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/user/{email}")
	@CrossOrigin
	public ResponseEntity<String> deleteUser(@PathVariable String email) {
		if(!userService.deleteUser(email)) return new ResponseEntity<String>("Xóa người dùng thất bại!!!", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<String>("Xóa người dùng thành công!!!", HttpStatus.OK);
	}
	
	@PostMapping("/user/add-product/{id}")
	@CrossOrigin
	public ResponseEntity<String> addProductToCart(@RequestHeader("Authorization") String token, @PathVariable Integer id){
		if (!userService.isValidJWT(token.substring(7))) return new ResponseEntity<String>("Token khong hop le!!!", HttpStatus.BAD_REQUEST);
		if (!userService.addnewProductToCartUser(userService.getInforJWT(token.substring(7)), id, 1)) return new ResponseEntity<String>("Thêm sản phẩm vào giỏ hàng thất bại!!!", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<String>("Thêm sản phẩm vào giỏ hàng thành công!!!", HttpStatus.OK);
	}
	
	@GetMapping("/user/product")
	@CrossOrigin
	public List<ProductDTO> getAllProductCart(@RequestHeader("Authorization") String token){
		if (!userService.isValidJWT(token.substring(7))) return null;
		return userService.getAllProductCart(userService.getInforJWT(token.substring(7))); 
	}
	
	@DeleteMapping("/user/product/{id}")
	@CrossOrigin
	public ResponseEntity<String> deleteProductCartUser(@RequestHeader("Authorization") String token, @PathVariable int id){
		if (!userService.isValidJWT(token.substring(7))) return new ResponseEntity<String>("Phiên đăng nhập của bạn đã hết hạn!!!", HttpStatus.BAD_REQUEST);
		if (!userService.deleteProductToCartUser(userService.getInforJWT(token.substring(7)), id)) return new ResponseEntity<String>("Xóa sản phẩm ra khỏi giỏ hàng thất bại!!!", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<String>("Xóa sản phẩm ra khỏi giỏ hàng thành công!!!", HttpStatus.OK);
	}
	
	@PostMapping("/login")
	@CrossOrigin
	public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO) {
		String result = userService.verifyUser(loginDTO,false);
		if (result.equals("Sai ten dang nhap hoac mat khau")) return new ResponseEntity<ResponseDTO>(new ResponseDTO("Sai ten dang nhap hoac mat khau", ""), HttpStatus.BAD_REQUEST);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("Dang nhap thanh cong!!!", result), HttpStatus.OK);
	}
	
	@GetMapping("/cart-size")
	@CrossOrigin
	public int getCurrentCartSize(@RequestHeader("Authorization") String token) {
		return userService.getCurrentCartSize(userService.getInforJWT(token.substring(7)));
	}
	
	@GetMapping("/test")
	@CrossOrigin
	public String test() {
		emailService.sendSimpleMessage("giangnam.trunghoccoso@gmail.com", "Mã xác minh cho đăng ký tài khoản mới ", "test123");
		return "test";
	}
}

record JwtRespose(String token) {}
