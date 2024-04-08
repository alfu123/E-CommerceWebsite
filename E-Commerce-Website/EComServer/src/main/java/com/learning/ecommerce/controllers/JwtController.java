package com.learning.ecommerce.controllers;

import com.learning.ecommerce.Util.JwtUtil;
import com.learning.ecommerce.converters.UserDtoConverter;
import com.learning.ecommerce.dao.UserDao;
import com.learning.ecommerce.dto.JwtRequest;
import com.learning.ecommerce.dto.JwtResponse;
import com.learning.ecommerce.models.User;
import com.learning.ecommerce.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class JwtController {
    @Autowired
    private JwtService jwtService;


    @PostMapping({"/login"})
    public ResponseEntity<String> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);


    }

}
