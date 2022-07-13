package com.omar.jwt.Repository;

import com.omar.jwt.Entities.AppRole;
import com.omar.jwt.Entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);

}
