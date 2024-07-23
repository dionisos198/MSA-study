package org.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class RequestLogin {

    @NotNull(message = "Email cat not be null")
    private String email;

    @NotEmpty(message = "pwd cat not be null")
    private String password;

    public UsernamePasswordAuthenticationToken getAuthenticationToken(){
        return new UsernamePasswordAuthenticationToken(email,password);
    }



}
