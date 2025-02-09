package com.javaPpmTool.ppmtool.services;

import com.javaPpmTool.ppmtool.domain.User;
import com.javaPpmTool.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // don't store anything that's readable in database
    // if cannot be autowired, can create the bean on the main spring boot application - PpmtoolApplication
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

        // Username has to be unique (exception)

        // Make sure that password and confirmPassword match
        // We don't persist or show the confirmPassword


        return userRepository.save(newUser);
    }


}
