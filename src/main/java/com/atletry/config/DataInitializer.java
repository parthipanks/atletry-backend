package com.atletry.config;

import com.atletry.entity.Role;
import com.atletry.entity.User;
import com.atletry.enums.RoleName;
import com.atletry.repository.RoleRepository;
import com.atletry.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository     roleRepo;
    private final UserRepository     userRepo;
    private final AtletryProperties  props;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedSuperAdmin();
    }

    private void seedRoles() {
        Arrays.stream(RoleName.values()).forEach(name -> {
            if (roleRepo.findByName(name).isEmpty()) {
                roleRepo.save(Role.builder().name(name).build());
                log.info("Seeded role: {}", name);
            }
        });
    }

    private void seedSuperAdmin() {
        String mobile = props.getSuperAdmin().getMobile();
        if (mobile == null || mobile.isBlank()) {
            log.info("SUPER_ADMIN_MOBILE not configured — skipping super-admin seed");
            return;
        }

        Role superAdminRole = roleRepo.findByName(RoleName.SUPER_ADMIN).orElseThrow();
        Role userRole       = roleRepo.findByName(RoleName.USER).orElseThrow();

        User superAdmin = userRepo.findByMobile(mobile).orElseGet(() -> {
            log.info("Creating super-admin user for mobile {}", mobile);
            return userRepo.save(User.builder().mobile(mobile).build());
        });

        if (!superAdmin.getRoles().contains(superAdminRole)) {
            superAdmin.getRoles().add(superAdminRole);
            superAdmin.getRoles().add(userRole);
            userRepo.save(superAdmin);
            log.info("Assigned SUPER_ADMIN role to {}", mobile);
        }
    }
}
