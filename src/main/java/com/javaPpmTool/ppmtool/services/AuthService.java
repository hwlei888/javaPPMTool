package com.javaPpmTool.ppmtool.services;

import com.javaPpmTool.ppmtool.domain.Register;
import com.javaPpmTool.ppmtool.payload.LoginRequest;

public interface AuthService {
    String register(Register register);

    String login(LoginRequest loginRequest);
}
