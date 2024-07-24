package org.example.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.example.config.TokenProvider;
import org.example.dto.RequestLogin;
import org.example.dto.ResponseOrder;
import org.example.dto.TokenDto;
import org.example.dto.TokenResponseWithUserIdDto;
import org.example.dto.UserDto;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto,UserEntity.class);
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);


        return null;
    }



    @Override
    @Transactional
    public TokenResponseWithUserIdDto login(final RequestLogin requestLogin){

        UserEntity userEntity =userRepository.findByEmail(requestLogin.getEmail()).orElseThrow(()->new BadRequestException(
                "sk"));
        System.out.println("조기");

        if(!bCryptPasswordEncoder.matches(requestLogin.getPassword(),userEntity.getEncryptedPwd())){
            throw new BadRequestException("비밀번호 오류");
        }
        System.out.println("조기");
        UsernamePasswordAuthenticationToken authenticationToken= requestLogin.getAuthenticationToken();
        System.out.println("조기");
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("조기");
        TokenDto tokenDto=tokenProvider.createToken(authentication);
        System.out.println("조기");


        return new TokenResponseWithUserIdDto(tokenDto.getType(),tokenDto.getAccessToken(),
                 tokenDto.getRefreshToken(),
                tokenDto.getAccessTokenValidationTime(),userEntity.getEmail(),userEntity.getUserId());

    }

    @Override
    public UserDto getUserByUserId(String userId,String accessToken) {

        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(()->new NotFoundException("노노"));

        UserDto userDto = new ModelMapper().map(userEntity,UserDto.class);

        List<ResponseOrder> orderList = new ArrayList<>();

        //RestTemplate 방식
       /* String orderUrl = String.format(env.getProperty("order_service.url"),userId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+accessToken);
        ResponseEntity<List<ResponseOrder>> orderListResponse=
                restTemplate.exchange(orderUrl, HttpMethod.GET, new HttpEntity<>(headers),
                        new ParameterizedTypeReference<List<ResponseOrder>>() {
                        });

        List<ResponseOrder> ordersList = orderListResponse.getBody();
*/


        //Order Servcie 에서 에러나는 경우 500 error 발생
        //List<ResponseOrder> ordersList = orderServiceClient.getOrders(userId,"Bearer "+accessToken);

        log.info("Before call orders microservices");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> ordersList = circuitBreaker.run(()->orderServiceClient.getOrders(userId,accessToken),
                throwable -> new ArrayList<>());

        log.info("after callec orders microservice");

        userDto.setOrders(ordersList);

        return userDto;

    }
}
