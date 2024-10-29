package com.cleducate.security;

import com.cleducate.entity.Role;
import com.cleducate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<com.cleducate.entity.User> user = userRepository.findByIdAndIsDeletedFalse(Long.valueOf(username));
        if(!user.isPresent()){
            throw new UsernameNotFoundException("User Not Found with User Id : "+ username);
        }

        return new User(
                user.get().getEmail(),
                user.get().getPassword(),
                getAuthorities(user.get().getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(
                (role)->{
                    authorities.add(new SimpleGrantedAuthority(role.getRoleName().name()));
                    role.getPrivileges()
                            .stream()
                            .map(privilege -> new SimpleGrantedAuthority("ROLE_"+privilege.getName().name()))
                            .forEach(authorities::add);
                });
        return authorities;
    }













}
