use `e-commerce`;

-- Thêm dữ liệu vào bảng `user`
INSERT INTO `user` (`address`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `role`, `is_active`,`verify_token`, `created_at` )
VALUES ('123 Main Street', 'dinhnam@gmail.com', 'giang', 'nam', '$2a$10$aX5K74GHs.MSbML/CKF/i./7NV8MVHr.hg4ltI2cSEYfllDubZKM6', '1234567890', 'admin',true, 'abc','2023-07-27 17:22:55.546000'),
       ('456 Oak Avenue', 'jane@example.com', 'giang', 'nam', '$2a$10$aX5K74GHs.MSbML/CKF/i./7NV8MVHr.hg4ltI2cSEYfllDubZKM6', '9876543210', 'customer', true,'abc','2023-07-27 17:22:55.546000')
       -- Thêm người dùng khác ở đây
       ;

-- Thêm dữ liệu vào bảng `payment_type`
INSERT INTO `payment_type` (`name`)
VALUES ('Credit Card'),
       ('PayPal'),
       ('Cash on Delivery')
       -- Thêm các phương thức thanh toán khác ở đây
       ;
-- Thêm dữ liệu vào bảng `shipping_method`
INSERT INTO `shipping_method` (`price`, `name`)
VALUES (5, 'STANDARD SHIPPING'),
       (10, 'EXPRESS SHIPPING')
       -- Thêm các phương thức vận chuyển khác ở đây
       ;

-- Thêm dữ liệu vào bảng `shop_order`
--INSERT INTO `shop_order` (`order_total`, `shipping_method_id`, `user_id`, `order_date`, `order_status`, `shipping_address`)
--VALUES (250, 1, 1, '2023-06-15', 'Shipped', '123 Main Street'),
--       (150, 2, 2, '2023-07-12', 'Processing', '456 Oak Avenue')
       -- Thêm các đơn hàng khác ở đây
       ;

-- Thêm dữ liệu vào bảng `user_payment_method`
INSERT INTO `user_payment_method` (`is_default`, `payment_type_id`, `user_id`, `expiry_date`, `account_number`, `provider`)
VALUES (1, 1, 1, '2025-01-31', '1234567812345678', 'Visa'),
       (0, 2, 1, '2023-12-31', 'paypal_account@example.com', 'PayPal')
       -- Thêm các phương thức thanh toán của người dùng khác ở đây
       ;

-- Thêm dữ liệu vào bảng `product_category`
INSERT INTO `product_category` (`category_name`)
VALUES ('Electronics'),
       ('Fashion'),
       ('Home & Living'),
       ('Beauty & Health')
       -- Thêm các danh mục sản phẩm khác ở đây
       ;

-- Thêm dữ liệu vào bảng `product`
INSERT INTO `product` (`category_id`, `name`, `description`, `image`)
VALUES (1, 'Smartphone', 'High-quality smartphones from Japan', 'smartphone.jpg'),
       (2, 'Kimono', 'Traditional Japanese clothing', 'kimono.jpg'),
       (3, 'Teapot', 'Japanese-style teapots', 'teapot.jpg'),
       (4, 'Skincare Set', 'Natural skincare products', 'skincare.jpg')
       -- Thêm các sản phẩm khác ở đây
       ;

-- Thêm dữ liệu vào bảng `product_item`
INSERT INTO `product_item` (`product_id`, `price`, `quantity_stock`, `product_image`)
VALUES (1, 500, 50, 'smartphone1.jpg'),
       (1, 520, 30, 'smartphone2.jpg'),
       (2, 200, 20, 'kimono1.jpg'),
       (2, 220, 25, 'kimono2.jpg'),
       (3, 30, 100, 'teapot1.jpg'),
       (3, 35, 80, 'teapot2.jpg'),
       (4, 50, 60, 'skincare1.jpg'),
       (4, 55, 70, 'skincare2.jpg')
       -- Thêm các sản phẩm khác ở đây
       ;

-- Thêm dữ liệu vào bảng `promotion`
INSERT INTO `promotion` (`discount`, `name`, `description`, `start_date`, `end_date`)
VALUES (10, 'Summer Sale', 'Get 10% off on selected items', '2023-06-01', '2023-06-30'),
       (20, 'Flash Sale', 'Get 20% off for 24 hours', '2023-07-10', '2023-07-11')
       -- Thêm các chương trình khuyến mãi khác ở đây
       ;


-- Thêm dữ liệu vào bảng `promotion_category_list`
INSERT INTO `promotion_category_list` (`category_list_id`, `promotion_id`)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2)
       -- Thêm các thông tin chương trình khuyến mãi áp dụng cho danh mục sản phẩm khác ở đây
       ;

-- Thêm dữ liệu vào bảng `shopping_cart`
INSERT INTO `shopping_cart` (`user_id`)
VALUES (1),
       (2)
       -- Thêm giỏ hàng của người dùng khác ở đây
       ;

-- Thêm dữ liệu vào bảng `shopping_cart_item`
INSERT INTO `shopping_cart_item` (`product_id`, `quantity`, `shopping_id`)
VALUES (1, 2, 1),
       (2, 1, 1),
       (3, 3, 2)
       -- Thêm các sản phẩm trong giỏ hàng khác ở đây
       ;

