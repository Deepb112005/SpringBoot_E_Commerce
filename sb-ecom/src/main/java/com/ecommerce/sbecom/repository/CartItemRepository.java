package com.ecommerce.sbecom.repository;

import com.ecommerce.sbecom.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select ci from CartItem ci where ci.product.id = ?1 and ci.cart.id = ?2 ")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);

    @Modifying
    @Query("delete from CartItem ci where ci.cart.id = ?1 and ci.product.id = ?2 ")
    void deleteCartItemByCartIdAndProductId(Long cartId, Long productId);
}
