package com.tpe.controller;


import com.tpe.dto.LoginRequest;
import com.tpe.dto.RegisterRequest;
import com.tpe.security.JWTUtils;
import com.tpe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJWTController {



    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    //user register
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest){

        userService.saveUser(registerRequest);


        return new ResponseEntity<>("user is registered successfully", HttpStatus.CREATED);
    }

    //login isleminde sadece username ve password giriyoruz
    @PostMapping("/login")//post kullanimi standart
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication =authenticationManager.
                authenticate(//valid dogrulama yapiyor. username ve pass u . aksi halde exception atar.
                        new
                        UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword()));

            String token=jwtUtils.generateToken(authentication);

            return  new ResponseEntity<>(token,HttpStatus.CREATED);
    }

    @GetMapping("/goodbye")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> goodbye(){
        return ResponseEntity.ok("goodbye security");
    }


















}
