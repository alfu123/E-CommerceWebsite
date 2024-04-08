package com.learning.ecommerce.services;

import com.learning.ecommerce.converters.RoleDtoConverter;
import com.learning.ecommerce.dao.RoleDao;
import com.learning.ecommerce.dto.RoleDto;
import com.learning.ecommerce.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleDtoConverter roleDtoConverter;

    public Role createNewRole(RoleDto roleDto) {

        return roleDao.save(roleDtoConverter.convertDtoToEntity(roleDto));
    }

}
