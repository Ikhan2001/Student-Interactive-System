package com.cleducate.repository;

import com.cleducate.entity.User;
import com.cleducate.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "Select * from users u where u.id =:id and is_deleted = false", nativeQuery = true)
    Optional<User> findByIdAndIsDeletedFalse(Long id);

    User findByEmailIgnoreCaseAndRoles_RoleName(String email, Roles roles);

    @Query(value = "SELECT * FROM users u WHERE u.email =:email", nativeQuery = true)
    User findByEmail(String email);

    @Query(value = "SELECT * FROM users u", nativeQuery = true)
    List<User> getAllUsers();

    @Query(value = "Select count(u.id) from users u", nativeQuery = true)
    int totalUsers();

    @Query(value = "Select count(u.id) from users u where u.is_active = true", nativeQuery = true)
    int totalActiveUsers();


}
