package com.deepak.training_batch_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deepak.training_batch_management.entity.Role;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        // check if admin already exists
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPhone("+919234567890");
            admin.setPassword("123"); // later you can encode
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("✅ Default Admin Created");
        }
    }
}
