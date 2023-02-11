package com.amirkenesbay.controller;

import com.amirkenesbay.config.tokenprovider.JwtTokenProvider;
import com.amirkenesbay.dto.ApiResponse;
import com.amirkenesbay.dto.AuthorizationTokeDTO;
import com.amirkenesbay.dto.UserDTO;
import com.amirkenesbay.dto.UserLoginDTO;
import com.amirkenesbay.entity.User;
import com.amirkenesbay.enums.ApiStatus;
import com.amirkenesbay.exception.UserNotFoundException;
import com.amirkenesbay.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Api(description = "Controller for users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/registration")
    @ApiOperation(value = "User registration")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDto) {
        User user = userService.save(userDto);
        user.setPassword(null);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, 200, null, UserDTO.getUserFromEntity(user)));
    }

    @GetMapping(value = "/logged")
    @ApiOperation(value = "Get logged user")
    @Nullable
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        Optional<User> result = userService.findByEmail(request.getUserPrincipal().getName());
        if (result.isPresent()) {
            User user = result.get();
            user.setPassword(null);
            return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, 200, null, UserDTO.getUserFromEntity(user)));
        }
        throw new UserNotFoundException("User not found");
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "User login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginUser) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, 200, null, new AuthorizationTokeDTO(token)));
    }
}
