package com.cleducate.repository;

import com.cleducate.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

    @Query(value = "SELECT * FROM reset_password_token rpt WHERE rpt.token =:token", nativeQuery = true)
    Optional<ResetPasswordToken> getResetPasswordTokenDetailsByToken(String token);
}
