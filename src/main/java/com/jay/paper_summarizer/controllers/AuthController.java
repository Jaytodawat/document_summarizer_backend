package com.jay.paper_summarizer.controllers;

import com.jay.paper_summarizer.config.JWTGenerator;
import com.jay.paper_summarizer.dto.UserDTO;
import com.jay.paper_summarizer.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> registerHandler(@RequestBody UserDTO userDTO){
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
        UserDTO savedUser = userService.registerUser(userDTO);
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JWTGenerator.generateToken();
        return ResponseEntity.status(HttpStatus.CREATED).body("Token : " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginHandler(@RequestBody UserDTO userDTO){

        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                userDTO.getEmail(), userDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(authCredentials);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JWTGenerator.generateToken();

        return ResponseEntity.ok("Token : " + token);
    }

}
