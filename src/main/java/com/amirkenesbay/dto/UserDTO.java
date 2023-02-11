package com.amirkenesbay.dto;

import com.amirkenesbay.entity.Role;
import com.amirkenesbay.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;

    @Email(message = "Invalid email")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    private boolean isActive;

    private String[] roles;

    @JsonIgnore
    public static UserDTO getUserFromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                Arrays.copyOf(
                        user.getRoles().stream().map(Role::getName).toArray(),
                        user.getRoles().size(),
                        String[].class
                )
        );
    }
}
