package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.Refresh;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);

}
