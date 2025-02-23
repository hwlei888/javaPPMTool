package com.javaPpmTool.ppmtool.web;

import com.javaPpmTool.ppmtool.domain.Register;
import com.javaPpmTool.ppmtool.payload.LoginRequest;
import com.javaPpmTool.ppmtool.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// We use RequestMapping to define a base URL for all the REST APIs in this controller
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private AuthService authService;

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
//        //Validate passwords match
//        userValidator.validate(user, result);
//
//        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//        if(errorMap != null) return errorMap;
//
//        User newUser = userService.saveUser(user);
//
//        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
//    }

    // PostMapping will map incoming http post request to below method
    // RequestBody will extract Json from the request & will convert that Json to register java object
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Register register){
        String response = authService.register(register);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        String response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
