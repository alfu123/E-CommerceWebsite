package com.learning.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learning.ecommerce.Util.JwtUtil;
import com.learning.ecommerce.constants.Constants;
import com.learning.ecommerce.converters.RoleDtoConverter;
import com.learning.ecommerce.converters.UserDtoConverter;
import com.learning.ecommerce.dao.RoleDao;
import com.learning.ecommerce.dao.UserDao;
import com.learning.ecommerce.dto.JwtRequest;
import com.learning.ecommerce.dto.JwtResponse;
import com.learning.ecommerce.dto.RoleDto;
import com.learning.ecommerce.dto.UserDto;
import com.learning.ecommerce.models.Role;
import com.learning.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    ObjectMapper objMapper = new ObjectMapper();
	JsonNode errorNode = objMapper.createObjectNode();
	JsonNode dataNode = objMapper.createObjectNode();
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDtoConverter userDtoConverter;



    public ResponseEntity<String> createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();

        User user=userDao.findByUserName(userName);
        if(user!=null){
            if (passwordEncoder.matches(userPassword,user.getUserPassword())) {
                UserDetails userDetails = loadUserByUsername(userName);
                String newGeneratedToken = jwtUtil.generateToken(userDetails);
                JwtResponse jwtResponse=new JwtResponse(userDtoConverter.convertEntityToDto(user),newGeneratedToken);

                dataNode= objMapper.valueToTree(jwtResponse);

                return new ResponseEntity<String>(
						constructResponse(Constants.STATUS_SUCCESS, HttpStatus.OK.value(), dataNode, null),
						HttpStatus.OK);

            }
            ((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_FIELD, Constants.PASSWORD_FIELD);
			((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_MESSAGE, Constants.INCORRECT_PASSWORD);

			// Incorrect Password Response
			return new ResponseEntity<String>(
					constructResponse(Constants.STATUS_ERROR, HttpStatus.UNAUTHORIZED.value(), null, errorNode),
					HttpStatus.OK);
        }
        ((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_FIELD, Constants.PASSWORD_FIELD);
			((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_MESSAGE, Constants.INCORRECT_PASSWORD);

			// Incorrect Password Response
			return new ResponseEntity<String>(
					constructResponse(Constants.STATUS_ERROR, HttpStatus.UNAUTHORIZED.value(), null, errorNode),
					HttpStatus.OK);

//        authenticate(userName, userPassword);

//        UserDetails userDetails = loadUserByUsername(userName);
//        String newGeneratedToken = jwtUtil.generateToken(userDetails);

//        User user = userDao.findByUserName(userName);
//        return new JwtResponse(userDtoConverter.convertEntityToDto(user), newGeneratedToken);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUserName(username);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
    public Set getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }
//
//    public void authenticate(String userName, String userPassword) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }

    	// Method to construct response
	public String constructResponse(String status, int statusCode, JsonNode dataNode, JsonNode errorNode) {
		String response = "";

		// Create a new Empty Object Node
		ObjectNode objNode = objMapper.createObjectNode();
		objNode.put(Constants.RESPONSE_STATUS, status);
		objNode.put(Constants.RESPONSE_STATUS_CODE, statusCode);
		objNode.set(Constants.RESPONSE_DATA_STRING, dataNode);
		objNode.set(Constants.RESPONSE_ERRORS, errorNode);

		try {
			response = objMapper.writeValueAsString(objNode);
		} catch (JsonProcessingException e) {
			response = null;
		}

		return response;
	}
}
