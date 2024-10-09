package org.acme.demo.config;

import org.acme.demo.entity.Role;
import org.acme.demo.entity.RoleName;
import org.acme.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Autowired
    private RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles(){
        return args -> {
            if(roleRepository.findByName(RoleName.ROLE_USER).isEmpty()){
                Role userRole = new Role();
                userRole.setName(RoleName.ROLE_USER);
                roleRepository.save(userRole);
            }
            if(roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()){
                Role adminRole = new Role();
                adminRole.setName(RoleName.ROLE_ADMIN);
                roleRepository.save(adminRole);
            }
        };
    }
}
