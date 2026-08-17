package com.ecommerce.sbecom.repository;

import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String s);
}
