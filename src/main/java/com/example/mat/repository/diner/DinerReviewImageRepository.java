package com.example.mat.repository.diner;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.diner.Diner;

public interface DinerReviewImageRepository {

  @Modifying
  @Query("DELETE FROM Image di WHERE di.diner = :diner")
  void deleteByDiner(Diner diner);

}
