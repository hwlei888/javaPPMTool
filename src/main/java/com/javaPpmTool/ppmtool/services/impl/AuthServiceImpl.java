package com.javaPpmTool.ppmtool.services.impl;

import com.javaPpmTool.ppmtool.domain.Register;
import com.javaPpmTool.ppmtool.domain.User;
import com.javaPpmTool.ppmtool.exceptions.TodoAPIException;
import com.javaPpmTool.ppmtool.repositories.UserRepository;
import com.javaPpmTool.ppmtool.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

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
}