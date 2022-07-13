package com.omar.jwt.Service;

import com.omar.jwt.Entities.AppRole;
import com.omar.jwt.Entities.AppUser;
import com.omar.jwt.Repository.AppRoleRepository;
import com.omar.jwt.Repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional //transactional de spring
public class AccountServiceImpl implements AccountService {


   private AppUserRepository appUserRepository;


    private AppRoleRepository appRoleRepository;

    private PasswordEncoder passwordEncoder;

    //good practice
    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        //add new users encodes the pw auto
        String pw =appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
       return appUserRepository.save(appUser);

    }

    @Override
    public AppRole addNewRole(AppRole appRole) {

       return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser =appUserRepository.findByUsername(username);
        AppRole appRole =appRoleRepository.findByRoleName(roleName);
        appUser.getAppRoles().add(appRole);
        }




    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
       return appUserRepository.findAll();

    }

    @Override
    public List<AppRole> listRoles() {
        return appRoleRepository.findAll();
    }
}
