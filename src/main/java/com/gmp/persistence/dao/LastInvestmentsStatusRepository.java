package com.gmp.persistence.dao;


import com.gmp.persistence.model.LastInvestmentsStatus;
import com.gmp.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LastInvestmentsStatusRepository extends JpaRepository<LastInvestmentsStatus, Long> {
    @Query(value = "from LastInvestmentsStatus u where u.user = ?1")
    List<LastInvestmentsStatus> getLastRecordByUser(User user);

    List<LastInvestmentsStatus> getAllByUser(User user);

    List<LastInvestmentsStatus> getAllByUserAndSystemId(User user, String systemId);
}
