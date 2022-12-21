package com.example.demo.security.userprincal;

import com.example.demo.dto.LoginDto;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailService implements UserDetailsService {

    private final Logger logger= LoggerFactory.getLogger(UserDetailService.class);

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user= userRepository.findByUsername(username);
        if(!user.isPresent()){
            throw new UsernameNotFoundException("User "+username+"was not found in the db");
        }
        //Authorities
        Set<SimpleGrantedAuthority> roles= Collections.singleton(new SimpleGrantedAuthority(user.get().getRole().getName()));
        UserDetails userDetails= new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword()
        , roles);
        return userDetails;
    }
}
