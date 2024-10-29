package com.cleducate.entity;

import com.cleducate.enums.Platform;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Session extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String authToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loginTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date logoutTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @ColumnDefault(value = "false")
    private boolean isExpired;


}
