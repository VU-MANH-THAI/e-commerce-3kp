package com.giangnam.vn.Ecommerce.Website.Service;

import com.giangnam.vn.Ecommerce.Website.DTO.ShopOrderDTO;
import com.giangnam.vn.Ecommerce.Website.Entity.Shop_Order;
import org.springframework.http.ResponseEntity;

public interface ShopOrderService {
    ResponseEntity<ShopOrderDTO> getOrder(Integer id);
}
