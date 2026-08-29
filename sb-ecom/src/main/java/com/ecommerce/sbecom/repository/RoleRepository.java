package com.ecommerce.sbecom.repository;

import com.ecommerce.sbecom.model.AppRole;
import com.ecommerce.sbecom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(AppRole role);

}
