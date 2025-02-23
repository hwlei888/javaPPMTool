package com.javaPpmTool.ppmtool.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Register {

    private String username;
    private String fullName;
    private String password;
    private String confirmPassword;
}
