package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.model.Product;
import com.ecommerce.sbecom.payload.ProductDTO;
import org.springframework.stereotype.Service;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);
}
