package com.vany.controller;

import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vany.services.EmailService;
import com.vany.services.JwtUserDetailsService;

import com.vany.config.JwtTokenUtil;
import com.vany.model.DAOUser;
import com.vany.model.JwtRequest;
import com.vany.model.JwtResponse;
import com.vany.model.UserDTO;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final String refreshToken = jwtTokenUtil.refreshToken(userDetails);
		Long Time = System.currentTimeMillis() + (5 * 60) * 1000;
		JwtResponse jwt = new JwtResponse();
		jwt.setToken(token);
		jwt.setExpire(Time);
		jwt.setRefreshToken(refreshToken);
		return ResponseEntity.ok(jwt);
	}

	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO user) throws Exception {
		String subject ="User Registerd on "+new Date().toString();
		String body= "Hi Mr/Ms. <b>"+user.getName()+",</b><br><br>This Mail is Regarding Your Successfull Registration with V&Y company Bar Managment Software(BMS).<br><br><br> With Follows Details: <br> Name : <b>"+user.getName()+"</b> <br> Address : <b>"+user.getAddress()+"</b> <br> Email : <b>"+user.getEmail()+"</b> <br><br> Here are Your Credtionals :<br> Username:<b>"+user.getUsername()+"</b> <br> Password :<b>"+user.getPassword()+"</b>  <br>If any Query Contact Us on this Mobile No: <br> 1) +91-8007046661(Yogesh)<br> 2) +91-8149648134(Vishva)";
		emailService.sendEmail(user.getEmail(), subject, body);
		return ResponseEntity.ok(userDetailsService.save(user));
	}
	
	
	
	@RequestMapping(value = "/oauth/token", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        
		String authToken = request.getHeader("refresh");
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        UserDetails user = (UserDetails) userDetailsService.loadRefreshUserByUsername(username);
        String mainToken = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.refreshToken(user);
        Long Time = System.currentTimeMillis() + (5 * 60) * 1000;
        JwtResponse jwt = new JwtResponse();
		jwt.setToken(mainToken);
		jwt.setExpire(Time);
		jwt.setRefreshToken(refreshToken);
		return ResponseEntity.ok(jwt);    
    }
	

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
