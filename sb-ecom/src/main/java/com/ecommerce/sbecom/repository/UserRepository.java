package com.ecommerce.sbecom.repository;

import com.ecommerce.sbecom.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);

    boolean existsByUserEmail(String email);
}
