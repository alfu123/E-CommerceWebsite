package com.learning.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learning.ecommerce.constants.Constants;
import com.learning.ecommerce.converters.RoleDtoConverter;
import com.learning.ecommerce.converters.UserDtoConverter;
import com.learning.ecommerce.dao.RoleDao;
import com.learning.ecommerce.dto.RoleDto;
import com.learning.ecommerce.dto.UserDto;
import com.learning.ecommerce.models.Role;
import com.learning.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.learning.ecommerce.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

	ObjectMapper objMapper = new ObjectMapper();
	JsonNode errorNode = objMapper.createObjectNode();
	JsonNode dataNode = objMapper.createObjectNode();

//	@PersistenceContext
//	private EntityManager entityManager;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserDtoConverter userDtoConverter;
	@Autowired
	private RoleDtoConverter roleDtoConverter;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public void initRoleAndUser() {

		RoleDto adminRole = new RoleDto();
		adminRole.setRoleName("Admin");
		adminRole.setRoleDescription("Admin role");
		roleDao.save(roleDtoConverter.convertDtoToEntity(adminRole));


		RoleDto userRole = new RoleDto();
		userRole.setRoleName("User");
		userRole.setRoleDescription("Default role for newly created record");
		roleDao.save(roleDtoConverter.convertDtoToEntity(userRole));

		UserDto adminUser = new UserDto();
		adminUser.setUserName("admin123");
		adminUser.setUserPassword(getEncodedPassword("admin@pass"));
		adminUser.setUserFirstName("admin");
		adminUser.setUserLastName("admin");
		Set<RoleDto> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);
		adminUser.setRole(adminRoles);
		userDao.save(userDtoConverter.convertDtoToEntity(adminUser));

		UserDto user = new UserDto();
		user.setUserName("raj123");
		user.setUserPassword(getEncodedPassword("raj@123"));
		user.setUserFirstName("raj");
		user.setUserLastName("sharma");
		Set<RoleDto> userRoles = new HashSet<>();
		userRoles.add(userRole);
		user.setRole(userRoles);
		userDao.save(userDtoConverter.convertDtoToEntity(user));

	}

	public ResponseEntity<String> registerNewUser(UserDto userDto) {
		User user = userDao.findByUserName(userDto.getUserName());

// 		 Check if user with username does not exists in database
		if (user == null) {
			Role role = roleDao.findById(2).get();
//			role = entityManager.merge(role);
			Set<RoleDto> userRoles = new HashSet<>();
			userRoles.add(roleDtoConverter.convertEntityToDto(role));
			userDto.setRole(userRoles);
			userDto.setUserPassword(getEncodedPassword(userDto.getUserPassword()));

			user=userDao.save(userDtoConverter.convertDtoToEntity(userDto));
//			If User is saved Successfully
            dataNode = objMapper.valueToTree(userDtoConverter.convertEntityToDto(user));


            // Return if user is created is successfully
            return new ResponseEntity<String>(
                    constructResponse(Constants.STATUS_SUCCESS, HttpStatus.CREATED.value(), dataNode, null),
                    HttpStatus.CREATED);
            //       Modify JsonNode for response
        }
		// Modify JsonNode for response
		((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_FIELD, Constants.USERNAME_FIELD);
		((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_MESSAGE, Constants.USERNAME_ALREADY_EXISTS);

		// Return Username Already Exists Response
		return new ResponseEntity<String>(
				constructResponse(Constants.STATUS_ERROR, HttpStatus.CONFLICT.value(), null, errorNode),
				HttpStatus.OK);

	}

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
