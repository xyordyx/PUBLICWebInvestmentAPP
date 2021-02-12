package com.gmp.persistence.dao;

import com.gmp.persistence.model.User;
import com.gmp.persistence.model.WeeklyDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WeeklyDataRepository extends JpaRepository<WeeklyDataModel, Long> {
    @Query(value = "SELECT u FROM WeeklyDataModel u where u.dayOfYear = ?1 and u.user =?2")
    WeeklyDataModel getByDayOfYearAndUser(int dayOfYear, User user);

    @Query(value = "FROM WeeklyDataModel u where u.user = ?1 ORDER BY u.month ASC")
    List<WeeklyDataModel> getAllByUser(User user);

    @Modifying
    @Query("delete from WeeklyDataModel u where u.user = ?1")
    void deleteByUserID(User user);

}
