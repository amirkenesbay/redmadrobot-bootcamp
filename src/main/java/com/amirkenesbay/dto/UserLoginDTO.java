package com.amirkenesbay.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserLoginDTO {
    @NotBlank(message = "E-mail cannot be empty")
    @Email
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
