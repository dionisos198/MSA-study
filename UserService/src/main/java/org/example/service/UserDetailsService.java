package org.example.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements
        org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("kk"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("임시");

        return new User(userEntity.getEmail(),userEntity.getEncryptedPwd(),true,true,true,true
                ,Collections.singleton(authority));
    }

}
