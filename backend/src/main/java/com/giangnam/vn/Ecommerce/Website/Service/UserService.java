package com.giangnam.vn.Ecommerce.Website.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.giangnam.vn.Ecommerce.Website.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.giangnam.vn.Ecommerce.Website.Config.JWTUtils;
import com.giangnam.vn.Ecommerce.Website.Entity.Payment_Type;
import com.giangnam.vn.Ecommerce.Website.Entity.Product;
import com.giangnam.vn.Ecommerce.Website.Entity.Product_Category;
import com.giangnam.vn.Ecommerce.Website.Entity.Product_Item;
import com.giangnam.vn.Ecommerce.Website.Entity.Shop_Order;
import com.giangnam.vn.Ecommerce.Website.Entity.Shopping_Cart;
import com.giangnam.vn.Ecommerce.Website.Entity.Shopping_Cart_Item;
import com.giangnam.vn.Ecommerce.Website.Entity.User;
import com.giangnam.vn.Ecommerce.Website.Entity.User_Payment_Method;
import com.giangnam.vn.Ecommerce.Website.Entity.CompositeKey.ShoppingProductKey;
import com.giangnam.vn.Ecommerce.Website.Repository.Payment_TypeRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Product_CategoryRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Product_ItemRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Shipping_MethodRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Shop_OrderRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Shopping_CartRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Shopping_Cart_ItemRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.UserRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.User_Payment_MethodRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private User_Payment_MethodRepository user_Payment_MethodRepository;

	@Autowired
	private Payment_TypeRepository payment_TypeRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private Validation validation;

	@Autowired
	private Shopping_CartRepository shopping_CartRepository;

	@Autowired
	private Product_ItemRepository product_ItemRepository;

	@Autowired
	private Shopping_Cart_ItemRepository shopping_Cart_ItemRepository;

	@Autowired
	private Shipping_MethodRepository shipping_MethodRepository;

	@Autowired
	private Shop_OrderRepository shop_OrderRepository;
	
	@Autowired
	private Product_CategoryRepository categoryRepository;

	@Autowired
	private JWTUtils jwtUtils;

	@Autowired
	private ProductService productService;

	public List<UserDTO> findAllUser() {
		List<UserDTO> result = new ArrayList<>();
		for (User user : userRepository.findAll()) {
			result.add(toDTO(user));
		}
		return result;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean isValidJWT(String token) {
		return jwtUtils.validateJwtToken(token);
	}

	public String getInforJWT(String token) {
		return jwtUtils.parseJwtToken(token);
	}

	public UserDTO toDTO(User user) {
		return new UserDTO(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getAddress(), user.getFirstName(),
				user.getLastName(), user.getPassword(), user.getRole());
	}

	public List<User> findByRole(String role) {
		return userRepository.findByRole(role);
	}

	public List<User> findByAddress(String address) {
		return userRepository.findByAddress(address);
	}

	public String createNewUser(String email, String password, String firstName, String lastName, String phoneNumber,
			String address, String verifyToken) {
		try {
			if (!validation.isValidPassWord(password))
				return "Password is invalid";
			if (findByEmail(email) != null)
				return "User is already exist!!";
			User user = new User(email, phoneNumber, address, firstName, lastName,
					bCryptPasswordEncoder.encode(password), "customer", verifyToken, false);
			userRepository.save(user);
			if (!creatNewCartUser(email))
				return "Fail";
			return "Success";
		} catch (Exception ex) {
			return ex.toString();
		}
	}

	public String activeUser(String email, String verifyToken) {
		try {
			User user = userRepository.findByEmail(email);
			if (!user.getVerifyToken().equals(verifyToken))
				return "Active tài khoản thất bại!!!";
			user.setActive(true);
			userRepository.save(user);
			return "Active tài khoản thành công!!!";
		} catch (Exception e) {
			return "Active tài khoản thất bại!!!";
		}
	}

	public Boolean deleteUser(String email) {
		try {
			if (userRepository.findByEmail(email).getRole().toUpperCase().equals("CUSTOMER")) {
				userRepository.delete(userRepository.findByEmail(email));
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateUserPaymentMethod(String email, String value, String provider, String accountnumber,
			Date expiry_date, Boolean is_default) {
		try {
			User_Payment_Method result = findByEmail(email).getPaymentList().stream()
					.filter(paymentMethod -> value.equals(paymentMethod.getPaymentType().getName())).findFirst()
					.orElseGet(() -> {
						User_Payment_Method newPaymentMethod = new User_Payment_Method();
						Payment_Type paymentType = payment_TypeRepository.findAll().stream()
								.filter(paymentType1 -> value.equals(paymentType1.getName())).findFirst().orElse(null);
						paymentType.addUserPaymentMethod(newPaymentMethod);
						return newPaymentMethod;
					});
			result.setAccountNumber(accountnumber);
			result.setExpiryDate(expiry_date);
			result.setIsDefault(is_default);
			user_Payment_MethodRepository.save(result);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean changePasswordUser(String email, ChangePasswordDTO changePasswordDTO) {
		try {
			User user = findByEmail(email);
			if (user != null && changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())
					&& user.getVerifyToken().equals(changePasswordDTO.getConfirmToken())
					&& validation.isValidPassWord(changePasswordDTO.getNewPassword())) {
				user.setPassword(bCryptPasswordEncoder.encode(changePasswordDTO.getNewPassword()));
				userRepository.save(user);
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean changePhoneNumberUser(String email, String phoneNumber) {
		try {
			User user = findByEmail(email);
			if (user != null) {
				user.setPhoneNumber(phoneNumber);
				userRepository.save(user);
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean changeAddressUser(String email, String address) {
		try {
			User user = findByEmail(email);
			if (user != null) {
				user.setAddress(address);
				userRepository.save(user);
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean changeFirstNameUser(String email, String firstName) {
		try {
			User user = findByEmail(email);
			if (user != null) {
				user.setFirstName(firstName);
				userRepository.save(user);
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean changeLastNameUser(String email, String lastName) {
		try {
			User user = findByEmail(email);
			if (user != null) {
				user.setLastName(lastName);
				userRepository.save(user);
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean creatNewCartUser(String email) {
		try {
			Shopping_Cart shopping_Cart = new Shopping_Cart();
			shopping_Cart.setUser(findByEmail(email));
			shopping_CartRepository.save(shopping_Cart);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean addnewProductToCartUser(String email, Integer productItemId, int quantity) {
		try {
			User user = findByEmail(email);
			Shopping_Cart shopping_Cart = user.getCartList().get(user.getCartList().size() - 1);
			Optional<Shopping_Cart_Item> shopping_Cart_ItemOptional = shopping_Cart_ItemRepository
					.findById(new ShoppingProductKey(shopping_Cart.getId(), productItemId));
			Shopping_Cart_Item shopping_Cart_Item = new Shopping_Cart_Item();
			if (shopping_Cart_ItemOptional.isEmpty()) {
				if (product_ItemRepository.findById(productItemId).get()
						.getQuantityStock() >= shopping_Cart_Item.getQuantity() + quantity) {
					shopping_Cart_Item.setProductItem(product_ItemRepository.findById(productItemId).get());
					shopping_Cart_Item.setId(new ShoppingProductKey(shopping_Cart.getId(), productItemId));
					shopping_Cart_Item.setQuantity(shopping_Cart_Item.getQuantity() + quantity);
					shopping_Cart.addShoppingCartItem(shopping_Cart_Item);
				} else
					return false;
			} else {
				shopping_Cart_Item = shopping_Cart_ItemOptional.get();
				if (!(product_ItemRepository.findById(productItemId).get()
						.getQuantityStock() >= shopping_Cart_Item.getQuantity() + quantity))
					return false;
				shopping_Cart_Item.setQuantity(shopping_Cart_Item.getQuantity() + quantity);
			}
			shopping_Cart_ItemRepository.save(shopping_Cart_Item);
			shopping_CartRepository.save(shopping_Cart);
			return true;
		} catch (Exception ex) {
			// System.out.println(ex.toString());
			return false;
		}
	}

	public boolean updateProductToCartUser(String email, Integer productItemId, int quantity) {
		try {
			User user = findByEmail(email);
			Shopping_Cart shopping_Cart = user.getCartList().get(user.getCartList().size() - 1);
			if (product_ItemRepository.findById(productItemId).get().getQuantityStock() < quantity)
				return false;
			Shopping_Cart_Item shopping_Cart_Item = shopping_Cart_ItemRepository
					.findById(new ShoppingProductKey(productItemId, shopping_Cart.getId())).get();
			shopping_Cart_Item.setQuantity(quantity);
			shopping_Cart_ItemRepository.save(shopping_Cart_Item);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean deleteProductToCartUser(String email, Integer productItemId) {
		try {
			User user = findByEmail(email);
			Shopping_Cart shopping_Cart = user.getCartList().get(user.getCartList().size() - 1);
			System.out.println(shopping_Cart.getId());
			System.out.println(productItemId);
			Shopping_Cart_Item shopping_Cart_Item = shopping_Cart_ItemRepository
					.findById(new ShoppingProductKey(shopping_Cart.getId(), productItemId)).get();
			shopping_Cart_ItemRepository.delete(shopping_Cart_Item);
			return true;
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return false;
		}
	}

	public boolean checkOutCartUser(String email, CartDTO cartDTO) {
		try {
			User user = findByEmail(email);
			Shopping_Cart shopping_Cart = user.getCartList().get(user.getCartList().size() - 1);
			int total = 0;
			for (Shopping_Cart_Item x : shopping_Cart.getShoppingCartItemList()) {
				total += x.getQuantity() * x.getProductItem().getPrice();
				x.getProductItem().setQuantityStock(x.getProductItem().getQuantityStock() - x.getQuantity());
			}
			if (total == 0)
				return false;
			Shop_Order shop_order = new Shop_Order(user,
					shipping_MethodRepository.findByName(cartDTO.getShippingMethod().toUpperCase()), shopping_Cart, new Date(),
					cartDTO.getAddress(), total, "Chờ xác nhận!!!");
			shopping_Cart.setShop_Order(shop_order);
			shop_OrderRepository.save(shop_order);
			shopping_CartRepository.save(shopping_Cart);
			creatNewCartUser(email);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String verifyUser(LoginDTO loginDTO, boolean isAdmin) {
		User user = userRepository.findByEmail(loginDTO.getEmail());
		if (user == null)
			return "Sai ten dang nhap hoac mat khau";
		else {
			if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
				if (!isAdmin || (isAdmin && user.getRole().toUpperCase().equals("ADMIN"))) return jwtUtils.generateJwtToken(loginDTO.getEmail());
			return "Sai ten dang nhap hoac mat khau";
		}
	}

	public int getCurrentCartSize(String email) {
		User user = userRepository.findByEmail(email);
		return user.getCartList().get(user.getCartList().size() - 1).getShoppingCartItemList().isEmpty() ? 0
				: user.getCartList().get(user.getCartList().size() - 1).getShoppingCartItemList().size();
	}

	public List<ProductDTO> getAllProductCart(String email) {
		User user = userRepository.findByEmail(email);
		List<ProductDTO> productList = new ArrayList<ProductDTO>();
		for (Shopping_Cart_Item x : user.getCartList().get(user.getCartList().size() - 1).getShoppingCartItemList()) {
			ProductDTO productDTO = productService.toDTO(x.getProductItem());
			productDTO.setQuantityStock(x.getQuantity());
			productList.add(productDTO);
		}
		return productList;
	}

	public List<OrderInfoDTO> getOrderInfoAdmin(String token) {
		//if (!isValidJWT(token) && userRepository.findByEmail(getInforJWT(token)).getRole().toUpperCase().equals("ADMIN")) return null;
		List<OrderInfoDTO> result = new ArrayList<>();
		List<Shopping_Cart_Item> shopping_Cart_Items = shopping_Cart_ItemRepository.findAll();
		Map<Product_Item,Integer> map = new HashMap<Product_Item,Integer>();
		for (Shopping_Cart_Item shopping_Cart_Item : shopping_Cart_Items) {
			map.put(shopping_Cart_Item.getProductItem(), map.getOrDefault(shopping_Cart_Item.getProductItem(), 0) +  Integer.valueOf(shopping_Cart_Item.getQuantity()));
		}
		for (Map.Entry<Product_Item, Integer> x : map.entrySet()) {
			result.add(new OrderInfoDTO(x.getKey().getId(), x.getKey().getProduct().getName(), x.getKey().getProduct().getDescription(), x.getKey().getProduct().getCategory().getCategoryName(), x.getKey().getPrice(), x.getValue(), x.getKey().getPrice() * x.getValue()));
		}
		return result;
	}

	public List<OrderDTO> getAllOrderAdmin(String token) {
		List<OrderDTO> result = new ArrayList<>();
		for (Shop_Order order : shop_OrderRepository.findAll()) {
			List<ProductDTO> productDTOList = new ArrayList<>();
			for (Shopping_Cart_Item shopping_Cart_Item : order.getShopping_Cart().getShoppingCartItemList()) {
				Product_Item product_Item = shopping_Cart_Item.getProductItem();
				product_Item.setQuantityStock(shopping_Cart_Item.getQuantity());
				productDTOList.add(productService.toDTO(product_Item));
			}
			result.add(new OrderDTO(order.getId(), order.getUser().getEmail(), order.getShippingMethod().getName(),
					order.getShipping_address(), order.getOrderTotal(), order.getOrderStatus(), order.getOrderDate(), productDTOList));
		}
		return result;
	}

	public String addNewCategory(String token, CategoryDTO categoryDTO) {
		try {
			//if (!(isValidJWT(token) && userRepository.findByEmail(getInforJWT(token)).getRole().toUpperCase().equals("ADMIN"))) return "JWT hoặc role không chính xác!!!";
			if (!categoryDTO.getName().equals(categoryDTO.getConfirmName())) return "Loại sản phẩm và xác nhận không trùng nhau!!!";
			if (!(categoryRepository.findByCategoryName(categoryDTO.getName()) == null)) return "Đã có loại sản phẩm này!!!";
			Product_Category category = new Product_Category();
			category.setCategoryName(categoryDTO.getName());
			category.setProductList(new ArrayList<Product>());
			categoryRepository.save(category);
			return "Thêm loại sản phẩm thành công!!!";
		} catch (Exception e) {
			return e.toString();
		}
	}

	public double getAllRevenue(String substring) {
		double result = 0;
		for (Shop_Order x : shop_OrderRepository.findAll()) {
			result += x.getOrderTotal();
		}
		return result;
	}
	
	public int getAllNewUserThisMonth(String substring) {
		List<User> users = userRepository.findAll();
		int result = 0;
		Date date = new Date();
		for (User user : users) {
			if(user.getCreatedAt().getYear() == date.getYear() && user.getCreatedAt().getMonth() == date.getMonth()) result++;
		}
		return result;
	}
	
	public int getAllNewUserThisYear(String substring) {
		List<User> users = userRepository.findAll();
		int result = 0;
		Date date = new Date();
		for (User user : users) {
			if(user.getCreatedAt().getYear() == date.getYear()) result++;
		}
		return result;
	}

	public CartIdDTO getCartId(String email) {
		User user = findByEmail(email);
		Integer shopping_Cart = user.getCartList().get(user.getCartList().size() - 1).getId() - 1;
		CartIdDTO cartIdDTO = new CartIdDTO();
		cartIdDTO.setCartId(shopping_Cart);
		return cartIdDTO;
	}
}
