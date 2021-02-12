package com.gmp.persistence.dao;

import com.gmp.persistence.model.FactUser;
import com.gmp.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FactUserRepository extends JpaRepository<FactUser, Long> {
    @Query(value = "SELECT u FROM FactUser u where u.user = ?1")
    FactUser findFactUserByUser(User user);
}
