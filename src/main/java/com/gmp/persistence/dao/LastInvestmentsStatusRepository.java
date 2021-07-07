package com.gmp.persistence.dao;


import com.gmp.persistence.model.LastInvestmentsStatus;
import com.gmp.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LastInvestmentsStatusRepository extends JpaRepository<LastInvestmentsStatus, Long> {
    @Query(value = "from LastInvestmentsStatus u where u.user = ?1 order by u.dateTime asc")
    List<LastInvestmentsStatus> getLastRecordByUser(User user);

    List<LastInvestmentsStatus> getAllByUser(User user);

    @Query(value = "from LastInvestmentsStatus u where u.user = ?1 and u.systemId = ?2 order by u.dateTime desc")
    List<LastInvestmentsStatus> getAllByUserAndSystemId(User user, String systemId);
}
