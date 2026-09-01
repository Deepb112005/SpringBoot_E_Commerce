package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.payload.CartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {


    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();
}
