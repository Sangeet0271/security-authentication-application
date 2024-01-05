package com.example.SecurityAuthentication.controller;

import java.util.Collections;
import java.util.Optional;

import com.example.SecurityAuthentication.Entity.Role;
import com.example.SecurityAuthentication.Entity.User;
import com.example.SecurityAuthentication.dto.LoginDto;
import com.example.SecurityAuthentication.dto.SignUpDto;
import com.example.SecurityAuthentication.repository.RoleRepository;
import com.example.SecurityAuthentication.repository.UserRepository;
import com.example.SecurityAuthentication.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        System.out.println(loginDto);
        System.out.println(userRepository.findByUsername(loginDto.getUsername()));
        User user=null;
        try{
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            user = userRepository.findByUsername(loginDto.getUsername());
        } catch (Exception e){
            e.printStackTrace();
        }



        if(user==null){
            return ResponseEntity.status(404).body("User not found!");
        } else{
            var jwt = jwtHelper.generateToken(user.getUsername());
            return new ResponseEntity<>("User login successfully!..." + jwt, HttpStatus.OK);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        // checking for username exists in a database
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
        }


        // checking for email exists in a database
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        // creating user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        String pass = passwordEncoder.encode(signUpDto.getPassword());
        user.setPassword(pass);
        Optional<Role> role = roleRepository.findByName(signUpDto.getRole());
        if(role.isEmpty()){
            Role newRole = new Role(signUpDto.getRole());
            roleRepository.save(newRole);
            user.setRoles(Collections.singleton(newRole));
        } else {
            user.setRoles(Collections.singleton(role.get()));
        }

        System.out.println(userRepository.save(user));
        var token = jwtHelper.generateToken(user.getUsername());



        return new ResponseEntity<>("User is registered successfully!" + token, HttpStatus.OK);
    }
}