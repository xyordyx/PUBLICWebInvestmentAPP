package com.gmp.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.gmp.persistence.dao.PrivilegeRepository;
import com.gmp.persistence.dao.RoleRepository;
import com.gmp.persistence.dao.UserRepository;
import com.gmp.persistence.model.Privilege;
import com.gmp.persistence.model.Role;
import com.gmp.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        final Privilege userPrivilege = createPrivilegeIfNotFound("USER_PRIVILEGE");
        final Privilege finSmartPrivilege = createPrivilegeIfNotFound("FINSMART_PRIVILEGE");
        final Privilege adminPrivilege = createPrivilegeIfNotFound("ADMIN_PRIVILEGE");
        final Privilege facturedoPrivilege = createPrivilegeIfNotFound("FACTUREDO_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(userPrivilege, readPrivilege, writePrivilege,
                passwordPrivilege,finSmartPrivilege, adminPrivilege, facturedoPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(userPrivilege,readPrivilege, passwordPrivilege));
        final List<Privilege> finSmartPrivileges = new ArrayList<>(Arrays.asList(userPrivilege,
                readPrivilege, passwordPrivilege,finSmartPrivilege));
        final List<Privilege> facturedoPrivileges = new ArrayList<>(Arrays.asList(userPrivilege,
                readPrivilege, passwordPrivilege,facturedoPrivilege));

        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        final Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);
        final Role finSmartRole = createRoleIfNotFound("ROLE_FINSMART", finSmartPrivileges);
        final Role facturedoRole = createRoleIfNotFound("ROLE_FACTUREDO", facturedoPrivileges);

        // == create initial admin
        createUserIfNotFound("admin", "Admin", "Account", "root", new ArrayList<>(Arrays.asList(adminRole)));
        // == create initial user
        //createUserIfNotFound("user", "user", "user", "root", new ArrayList<>(Arrays.asList(userRole)));
        // == create initial finSmartuser
        //createUserIfNotFound("fin", "Fin", "Smart", "root", new ArrayList<>(Arrays.asList(finSmartRole)));

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    User createUserIfNotFound(final String email, final String firstName, final String lastName, final String password, final Collection<Role> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setEnabled(true);
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }

}