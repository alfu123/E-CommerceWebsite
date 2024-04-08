package com.learning.ecommerce.controllers;

import com.learning.ecommerce.dto.RoleDto;
import com.learning.ecommerce.models.Role;
import com.learning.ecommerce.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping({"/createNewRole"})
    public Role createNewRole(@RequestBody RoleDto roleDto) {
        return roleService.createNewRole(roleDto);
    }
}
