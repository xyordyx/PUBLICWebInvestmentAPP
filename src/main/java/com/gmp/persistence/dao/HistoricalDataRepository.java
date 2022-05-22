package com.gmp.persistence.dao;

import com.gmp.persistence.model.HistoricalData;
import com.gmp.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface HistoricalDataRepository extends JpaRepository<HistoricalData, Long> {
    @Query(value = "SELECT u FROM HistoricalData u where u.month = ?1 and u.year =?2 and u.user =?3")
    HistoricalData getByMonthAndYearByUser(int month, int year, User user);

    @Query(value = "FROM HistoricalData u where u.year = ?1 ORDER BY u.month ASC")
    List<HistoricalData> getAllByYear(int year);

    @Query(value = "FROM HistoricalData u where u.user = ?1 ORDER BY u.month ASC")
    List<HistoricalData> getAllByUser(User user);

    @Modifying
    @Query("delete from HistoricalData t where t.user <= ?1")
    void deleteByUserID(User user);

}
