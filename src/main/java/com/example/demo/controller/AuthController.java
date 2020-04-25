package com.example.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.ERole;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositorys.RoleRepository;
import com.example.demo.Repositorys.UserRepository;
import com.example.demo.Services.UserDetailImplementation;
import com.example.demo.configs.JwtUtils;
import com.example.demo.payload.JwtResponse;
import com.example.demo.payload.MessageResponse;
import com.example.demo.payload.dto.LoginRequest;
import com.example.demo.payload.dto.SignupRequest;


@CrossOrigin()
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	RoleRepository roleRepository;
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailImplementation userDetails=(UserDetailImplementation) authentication.getPrincipal();	
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
		
		//Verify the existance of username or the email in the DB
		if(userRepository.existsByUsername(signupRequest.getUsername()))
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if(userRepository.existsByEmail(signupRequest.getEmail()))
		{
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		
		//Create User's Account
		User user=new User(signupRequest.getUsername(),signupRequest.getEmail(),encoder.encode(signupRequest.getPassword()));
		//1.récupérer les roles du request body
		Set<String> rolesfromRequest = signupRequest.getRole();
		//2.create an empty set of roles
		Set<Role> roles = new HashSet<>();
		//3.user who's tryin to signin w'll be saved as a ROLE_USER
		if(rolesfromRequest==null)
		{
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(()->new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		}
		else
		{
			rolesfromRequest.forEach(role ->{
				switch (role) {
				case "admin":
					Role adminRole=roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(()-> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;

				default:
					Role userRole=roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
					break;
				}
			});
		}
		
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		
	}	
}
