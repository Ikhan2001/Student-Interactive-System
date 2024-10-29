package com.cleducate.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResetPasswordToken extends BaseEntity{

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Date expiryDate;

    public ResetPasswordToken(String token, User user){
        this.token = token;
        this.user = user;
        this.expiryDate = Date.from(Instant.now().plus(30, ChronoUnit.MINUTES));
    }

    public boolean isExpired(){
        return  expiryDate.before(new Date());
    }



}
