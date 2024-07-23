package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.example.dto.RequestLogin;
import org.example.dto.RequestUser;
import org.example.dto.ResponseUser;
import org.example.dto.TokenResponseWithUserIdDto;
import org.example.dto.UserDto;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Environment environment;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/health_check")
    public String status(){
        return "it's working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome(){
        return environment.getProperty("greeting.message");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> list(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser user){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(user,UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = modelMapper.map(userDto,ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseWithUserIdDto> login(@RequestBody RequestLogin requestLogin){

        System.out.println("hi");

        TokenResponseWithUserIdDto tokenResponseWithUserIdDto = userService.login(requestLogin);
        return ResponseEntity.status(HttpStatus.OK
        ).body(tokenResponseWithUserIdDto);
    }


    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId,@RequestHeader("Authorization") String accessToken){
        UserDto userDto = userService.getUserByUserId(userId,accessToken);

        ResponseUser returnValue = new ModelMapper().map(userDto,ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }



}
