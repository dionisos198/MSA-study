package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestUser {




    @Email
    private String email;

    @NotNull(message = "password catnot be null")
    private String pwd;

    @NotEmpty(message = "이름은 공백 안됨")
    private String name;



}
