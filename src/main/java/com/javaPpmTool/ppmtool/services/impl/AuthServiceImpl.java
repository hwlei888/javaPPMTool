package com.javaPpmTool.ppmtool.services.impl;

import com.javaPpmTool.ppmtool.domain.Register;
import com.javaPpmTool.ppmtool.domain.User;
import com.javaPpmTool.ppmtool.exceptions.TodoAPIException;
import com.javaPpmTool.ppmtool.payload.LoginRequest;
import com.javaPpmTool.ppmtool.repositories.UserRepository;
import com.javaPpmTool.ppmtool.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @Override
    public String register(Register register) {

        // Check username is already exists in database
        if(userRepository.existsByUsername(register.getUsername())){
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        User user = new User();
        user.setUsername(register.getUsername());
        user.setFullName(register.getFullName());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setConfirmPassword("");
//        System.out.println("hwll" + user);

        userRepository.save(user);

        return "User Registered Successfully!";
    }


    @Override
    public String login(LoginRequest loginRequest) {
        // authenticate takes Authentication as an interface,
        // we have to provide the implementation (authentication token class) for this authentication interface
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "User logged-in successfully!";
    }

}