package com.omar.jwt.Service;

import com.omar.jwt.Entities.AppRole;
import com.omar.jwt.Entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username , String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
    List<AppRole> listRoles();

}
