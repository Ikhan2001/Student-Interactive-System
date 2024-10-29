package com.cleducate.repository;

import com.cleducate.entity.Role;
import com.cleducate.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(Roles roles);

}
