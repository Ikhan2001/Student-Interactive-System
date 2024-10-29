package com.cleducate.entity;

import com.cleducate.enums.Privileges;
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
public class Privilege extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private Privileges name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "privileges")
    @JsonIgnore
    private Set<Role> roles;

}











/*
database Query
INSERT INTO privilege (name, is_deleted, created_at, updated_at) VALUES
('VIEW_USERS', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CREATE_USERS', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('UPDATE_USERS', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DELETE_USERS', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
 */
