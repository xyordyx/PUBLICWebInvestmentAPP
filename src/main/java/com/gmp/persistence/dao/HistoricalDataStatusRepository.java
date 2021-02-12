package com.gmp.persistence.dao;

import com.gmp.persistence.model.HistoricalDataStatus;
import com.gmp.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HistoricalDataStatusRepository extends JpaRepository<HistoricalDataStatus, Long> {
    @Query(value = "from HistoricalDataStatus as u where u.user = ?1 and u.type = ?2")
    HistoricalDataStatus getLastRecordByUserByType(User user, String type);

    @Modifying
    @Query("DELETE from HistoricalDataStatus u where u.user = ?1 and u.type = ?2")
    void deleteByUserIDAAndType(User user, String data);
}
