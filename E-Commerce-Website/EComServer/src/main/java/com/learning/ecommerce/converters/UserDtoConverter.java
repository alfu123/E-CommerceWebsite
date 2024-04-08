package com.learning.ecommerce.converters;

import com.learning.ecommerce.dto.UserDto;
import com.learning.ecommerce.models.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDtoConverter {
	public UserDto convertEntityToDto(User user) {

		UserDto userDto = new UserDto();
		userDto.setUid(user.getUid());
		userDto.setUserName(user.getUserName());
        userDto.setUserFirstName(user.getUserFirstName());
        userDto.setUserLastName(user.getUserLastName());
        userDto.setUserPassword(user.getUserPassword());

		RoleDtoConverter roleDtoConverter=new RoleDtoConverter();
		userDto.setRole(user.getRole().stream().map(roleDtoConverter::convertEntityToDto).collect(Collectors.toSet()));

		return userDto;
	}

	public User convertDtoToEntity(UserDto userDto){

		User user = new User();
		user.setUid(userDto.getUid());
		user.setUserName(userDto.getUserName());
        user.setUserFirstName(userDto.getUserFirstName());
        user.setUserLastName(userDto.getUserLastName());
        user.setUserPassword(userDto.getUserPassword());

		RoleDtoConverter roleDtoConverter=new RoleDtoConverter();
		user.setRole(userDto.getRole().stream().map(roleDtoConverter::convertDtoToEntity).collect(Collectors.toSet()));

		return user;
	}
}
