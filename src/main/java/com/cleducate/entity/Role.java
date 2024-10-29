package com.cleducate.entity;

import com.cleducate.enums.Roles;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private Roles roleName;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @JsonBackReference
    private Set<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_privileges",
            joinColumns = {@JoinColumn(referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(referencedColumnName = "id")}
    )
    private Set<Privilege> privileges;

    public Role(Roles roleName){
        this.roleName = roleName;
    }


}





/*
 Database Query
 INSERT INTO role (role_name, is_deleted, created_at, updated_at) VALUES
('SUPER_ADMIN', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ADMIN', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TEAM_MEMBER', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CLIENT', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


 */
