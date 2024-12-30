package com.example.mat.repository.diner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.diner.Diner;

public interface DinerRepository extends JpaRepository<Diner, Long> {
    @Query("SELECT d FROM Diner d")
    Page<Object[]> getListPage(Pageable pageable);
}
