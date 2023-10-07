package com.giangnam.vn.Ecommerce.Website.Controller;

import com.giangnam.vn.Ecommerce.Website.DTO.ShopOrderDTO;
import com.giangnam.vn.Ecommerce.Website.Service.ShopOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class ShopOrderController {
    @Autowired
    ShopOrderService shopOrderService;

    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<ShopOrderDTO> getShop(@PathVariable Integer id) {
        return shopOrderService.getOrder(id);
    }
}
