package com.omar.jwt.Repository;

import com.omar.jwt.Entities.AppRole;
import com.omar.jwt.Entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.relation.Role;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {

    AppRole findByRoleName(String username);
}
