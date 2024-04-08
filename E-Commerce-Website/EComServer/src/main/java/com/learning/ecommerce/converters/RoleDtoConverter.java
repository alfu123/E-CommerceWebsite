package com.learning.ecommerce.converters;

import com.learning.ecommerce.dto.RoleDto;
import com.learning.ecommerce.models.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleDtoConverter {

    public RoleDto convertEntityToDto(Role role){
        RoleDto roleDto=new RoleDto();
        roleDto.setRid(role.getRid());
        roleDto.setRoleName(role.getRoleName());
        roleDto.setRoleDescription(role.getRoleDescription());
        return roleDto;
    }

    public Role convertDtoToEntity(RoleDto roleDto){
        Role role=new Role();
        role.setRid(roleDto.getRid());
        role.setRoleName(roleDto.getRoleName());
        role.setRoleDescription(roleDto.getRoleDescription());
        return role;
    }
}
