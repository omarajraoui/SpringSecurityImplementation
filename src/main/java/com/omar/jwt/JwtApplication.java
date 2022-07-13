package com.omar.jwt;

import com.omar.jwt.Entities.AppRole;
import com.omar.jwt.Entities.AppUser;
import com.omar.jwt.Service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start(AccountService accountService) {
		return args->{
			accountService.addNewRole(new AppRole(null,"USER"));
			accountService.addNewRole(new AppRole(null,"ADMIN"));
			accountService.addNewRole(new AppRole(null,"CUSTOMER_MANAGER"));
			accountService.addNewRole(new AppRole(null,"BILLS_MANAGER"));


			accountService.addNewUser(new AppUser(null,"Omario","1234686sfzf",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"AFAIR","Xopen5f6",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user1","afdhj55",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user2","zgzmg878r((-",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user3","huj'-èh6",new ArrayList<>()));

			accountService.addRoleToUser("Omario","ADMIN");
			accountService.addRoleToUser("AFAIR","USER");
			accountService.addRoleToUser("user1","USER");
			accountService.addRoleToUser("user1","BILLS_MANAGER");
			accountService.addRoleToUser("user2","CUSTOMER_MANAGER");
			accountService.addRoleToUser("user3","USER");


		};

	}
}
