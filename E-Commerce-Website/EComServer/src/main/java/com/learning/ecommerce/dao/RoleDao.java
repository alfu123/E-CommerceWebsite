package com.learning.ecommerce.dao;

import com.learning.ecommerce.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role,Integer> {
//  Find By UserRoleName
    Role findByRoleName(String rolename);

}
