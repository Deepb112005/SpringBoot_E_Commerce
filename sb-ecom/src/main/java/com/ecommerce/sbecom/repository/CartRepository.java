package com.ecommerce.sbecom.repository;

import com.ecommerce.sbecom.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
