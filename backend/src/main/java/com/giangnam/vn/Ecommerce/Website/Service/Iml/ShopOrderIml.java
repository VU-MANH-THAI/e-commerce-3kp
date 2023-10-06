package com.giangnam.vn.Ecommerce.Website.Service.Iml;

import com.giangnam.vn.Ecommerce.Website.DTO.ShopOrderDTO;
import com.giangnam.vn.Ecommerce.Website.Entity.Shop_Order;
import com.giangnam.vn.Ecommerce.Website.Repository.Shop_OrderRepository;
import com.giangnam.vn.Ecommerce.Website.Service.ShopOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShopOrderIml implements ShopOrderService {

    @Autowired
    Shop_OrderRepository shopOrderRepository;
    @Override
    public ResponseEntity<ShopOrderDTO> getOrder(Integer id) {
        Optional<Shop_Order> shopOrder = shopOrderRepository.findById(id);
        if (shopOrder.isPresent()) {
            Shop_Order shopOrder1 = shopOrder.get();
            ShopOrderDTO shopOrderDTO = new ShopOrderDTO();
            
            shopOrderDTO.setOrderDate(shopOrder1.getOrderDate());
            shopOrderDTO.setOrderTotal(shopOrder1.getOrderTotal());
            shopOrderDTO.setAddress(shopOrder1.getShipping_address());

            return new ResponseEntity<>(shopOrderDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
