package com.giangnam.vn.Ecommerce.Website.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.giangnam.vn.Ecommerce.Website.Config.CustomMultipartFile;
import com.giangnam.vn.Ecommerce.Website.DTO.ProductDTO;
import com.giangnam.vn.Ecommerce.Website.DTO.PromotionDTO;
import com.giangnam.vn.Ecommerce.Website.Entity.Product;
import com.giangnam.vn.Ecommerce.Website.Entity.Product_Category;
import com.giangnam.vn.Ecommerce.Website.Entity.Product_Item;
import com.giangnam.vn.Ecommerce.Website.Entity.Promotion;
import com.giangnam.vn.Ecommerce.Website.Repository.ProductRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Product_CategoryRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.Product_ItemRepository;
import com.giangnam.vn.Ecommerce.Website.Repository.PromotionRepository;

@Service
public class ProductService {
	
	private String path = "D:\\Học tập\\React js\\WebSite E-commerce\\react-ecommerce\\public\\Images\\";

	@Autowired
	ProductRepository productRepository;

	@Autowired
	Product_ItemRepository product_ItemRepository;

	@Autowired
	Product_CategoryRepository product_CategoryRepository;
	
	@Autowired
	PromotionRepository promotionRepository;

	public ProductDTO toDTO(Product_Item product_Item) {
		return new ProductDTO(product_Item.getId(),product_Item.getProduct().getName(),
				product_Item.getProduct().getDescription(), product_Item.getProductImage().substring( Math.min(61,product_Item.getProductImage().length()-1) ),
				product_Item.getProduct().getCategory().getCategoryName(), product_Item.getPrice(),
				product_Item.getQuantityStock());
	}
	
	public PromotionDTO toDTO(Promotion promotion) {
		PromotionDTO promotionDTO = new PromotionDTO();
		promotionDTO.setId(promotion.getId());
		promotionDTO.setName(promotion.getName());
		promotionDTO.setStartDate(promotion.getStartDate());
		promotionDTO.setEndDate(promotion.getEndDate());
		promotionDTO.setDiscount(promotion.getDiscount());
		promotionDTO.setDescription(promotion.getDescription());
		promotionDTO.setCategoryName(promotion.getCategoryList().get(0).getCategoryName());
		for (int i = 1; i < promotion.getCategoryList().size(); i++) promotionDTO.setCategoryName(promotionDTO.getCategoryName() + " ");
		return promotionDTO;
	}

	public String addNewProduct(ProductDTO productDTO) {
		try {	
			Product product = new Product();
			Product_Item product_Item = new Product_Item();
			product_Item.setPrice(productDTO.getPrice());
			String fileName = productDTO.getName() + ".jpg";
			String filePath = path + fileName;
			((MultipartFile) productDTO.getImage()).transferTo(new File(filePath));
			product_Item.setProductImage(filePath);
			product_Item.setQuantityStock(productDTO.getQuantityStock());
			product.addProductItem(product_Item);
			product.setName(productDTO.getName());
			product.setImage(filePath);
			product.setDescription(productDTO.getDescription());
			product.setCategory(product_CategoryRepository.findByCategoryName(productDTO.getCategoryName()));
			productRepository.save(product);
			product_ItemRepository.save(product_Item);
			return "Thêm Sản Phẩm Thành Công!!!";
		} catch (Exception e) {
			return e.toString();
		}
	}

	public String deleteProduct(int id) {
		try {
			Product_Item product_Item = product_ItemRepository.findById(id).get();
			if(product_Item.getProduct().getProductItemList().size() == 1) productRepository.delete(product_Item.getProduct());
			else product_ItemRepository.deleteById(id);
			return "Xoa San Pham Thanh Cong!!!";
		} catch (Exception e) {
			return e.toString();
		}
	}

	public boolean updateNameProduct(String name) {
		try {
			Product product = productRepository.findByName(name).get(0);
			product.setName(name);
			productRepository.save(product);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateDescriptionProduct(String name, String description) {
		try {
			Product product = productRepository.findByName(name).get(0);
			product.setDescription(description);
			productRepository.save(product);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateImageProduct(String name, String image) {
		try {
			Product product = productRepository.findByName(name).get(0);
			product.setImage(image);
			productRepository.save(product);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<Product> findProduct(String name, String description, int page, int productPerPage) {
		List<Product> productList = productRepository
				.findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCaseOrderByNameAsc(name, description);
		int check = productList.size() % productPerPage == 0 ? productList.size() / productPerPage
				: productList.size() / productPerPage + 1;
		if (page > check)
			page = check;
		return productList.subList(productPerPage * (page - 1), productPerPage * page - 1);
	}

	public List<ProductDTO> findAllProduct() {
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		for (Product_Item x : product_ItemRepository.findAll()) {
			result.add(toDTO(x));
		}
		return result;
	}
	
	public List<String> findAllCategory(){
		return product_CategoryRepository.findAll()
	            .stream()
	            .map(Product_Category::getCategoryName)
	            .collect(Collectors.toList());
	}
	
	public List<PromotionDTO> findAllPromotion(){
		List<Promotion> promotionList = promotionRepository.findAll();
		List<PromotionDTO> result = promotionList.stream()
		    .map(this::toDTO)
		    .collect(Collectors.toList());
		return result;
	}
	
	public String deletePromotion(int id) {
		try {
			promotionRepository.deleteById(id);
			return "Xoa khuyen mai thanh cong!!!";
		} catch (Exception e) {
			return e.toString();
		}
	}

	public ProductDTO findProductById(int id) {
		return toDTO(product_ItemRepository.findById(id).get());
	}

	public String updateProduct(ProductDTO productDTO) {
		try {
			Product_Item product_Item = product_ItemRepository.findById(productDTO.getId()).get();
			Product product = product_Item.getProduct();
			product_Item.setPrice(productDTO.getPrice());
			if (productDTO.getImage().toString().length() > 0)
			{
				String fileName = productDTO.getName() + ".jpg";
				String filePath = path + fileName;
				((MultipartFile) productDTO.getImage()).transferTo(new File(filePath));
				product_Item.setProductImage(filePath);
				product.setImage(filePath);
			}
			product_Item.setQuantityStock(productDTO.getQuantityStock());
			product.addProductItem(product_Item);
			product.setName(productDTO.getName());
			product.setDescription(productDTO.getDescription());
			product.setCategory(product_CategoryRepository.findByCategoryName(productDTO.getCategoryName()));
			productRepository.save(product);
			product_ItemRepository.save(product_Item);
			return "Cập nhật sản phẩm thành công!!!";
		} catch (Exception e) {
			return e.toString();
		}
	}
}

