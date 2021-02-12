package com.gmp.persistence.dao;

import com.gmp.persistence.model.SmartUser;
import com.gmp.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SmartUserRepository extends JpaRepository<SmartUser, Long> {
    @Query(value = "SELECT u FROM SmartUser u where u.user = ?1")
    SmartUser findSmartUserByUser(User user);
}
