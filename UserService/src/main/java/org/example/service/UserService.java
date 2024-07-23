package org.example.service;

import org.example.dto.RequestLogin;
import org.example.dto.TokenResponseWithUserIdDto;
import org.example.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  {

    public UserDto createUser(UserDto userDto);

    public TokenResponseWithUserIdDto login(RequestLogin requestLogin);

    public UserDto getUserByUserId(String userId,String accessToken);

}
