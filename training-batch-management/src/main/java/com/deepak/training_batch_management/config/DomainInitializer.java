package com.deepak.training_batch_management.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deepak.training_batch_management.entity.Domain;
import com.deepak.training_batch_management.repository.DomainRepository;

@Component
public class DomainInitializer implements CommandLineRunner {
	@Autowired
    private DomainRepository domainRepository;

    @Override
    public void run(String... args) {

        if (domainRepository.count() == 0) {

            List<String> defaultDomains = List.of(
                "java", "python", "react", "angular", "spring boot"
            );

            for (String name : defaultDomains) {
                Domain d = new Domain();
                d.setName(name.toLowerCase());
                domainRepository.save(d);
            }

            System.out.println("✅ Default domains inserted");
        }
    }
}
