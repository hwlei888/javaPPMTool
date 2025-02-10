package com.javaPpmTool.ppmtool.validator;

import com.javaPpmTool.ppmtool.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        // Use this to further validate we have a correct object here
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {

        // Want to cast this object passing here to type user
        User user = (User) object;

        if(user.getPassword().length() < 6){
            errors.rejectValue("password", "Length", "Password must be at least 6 characters");
        }

        if(!user.getPassword().equals(user.getConfirmPassword())){
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
        }

    }
}
