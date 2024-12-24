package com.example.mat.repository.diner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.diner.Diner;
import com.example.mat.entity.diner.DinerImage;

public interface DinerImageRepository extends JpaRepository<DinerImage, Long>, DinerImageReviewRepository {
  // dienr.did 를 이용해 dinerImage제거
  @Modifying
  @Query("DELETE FROM DinerImage di WHERE di.diner = :diner")
  void deleteByDiner(Diner diner);
}
