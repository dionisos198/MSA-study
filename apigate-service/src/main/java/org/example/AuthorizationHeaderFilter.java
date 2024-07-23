package org.example;

import com.netflix.discovery.converters.Auto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.example.CustomFilter.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {


    @Autowired
    public AuthorizationHeaderFilter(Environment environment){
        super(AuthorizationHeaderFilter.Config.class);

        this.environment = environment;
        byte[] key_set = Decoders.BASE64.decode(environment.getProperty("jwt.secret"));
        this.key = Keys.hmacShaKeyFor(key_set);

    }
    private Key key;

    private Environment environment;

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange,"No authorization Header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(
                    org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);

            String jwt = authorizationHeader.replace("Bearer","");

            if(!isJwtValidate(jwt)){
                return onError(exchange,"JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);

        });
    }

    private Mono<Void> onError(ServerWebExchange exchange,String err, HttpStatus httpStatus){

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    private boolean isJwtValidate(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(MalformedJwtException | SecurityException e) {
            log.info("잘못된 형식의 토큰입니다.");
        } catch(ExpiredJwtException e) {
            log.info("만료된 토큰입니다.");
        } catch(UnsupportedJwtException e) {
            log.info("지원하지 않는 형식의 토큰입니다.");
        } catch(IllegalArgumentException e) {
            log.info("잘못된 토큰입니다.");
        }
        return false;
    }

    public static class Config{

    }

}
