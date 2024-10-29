package com.cleducate.entity;

import com.cleducate.enums.Gender;
import com.cleducate.enums.CurrentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roles"})
public class User extends BaseEntity{

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    @JsonIgnore
    private String password;
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    private CurrentStatus currentStatus;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ColumnDefault("true")
    private boolean isActive;
    @ColumnDefault("false")
    private boolean isAccountRemoved;
    @ColumnDefault("false")
    private boolean isProfileCompleted;
    private String timezone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(referencedColumnName = "id")}
    )
    @JsonManagedReference
    private Set<Role> roles;


    //new fields for reset password
    @ColumnDefault("0")
    private int resetPasswordAttempts;
    @ColumnDefault("false")
    private boolean is_blocked;


}
