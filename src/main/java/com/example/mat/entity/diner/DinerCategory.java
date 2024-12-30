package com.example.mat.entity.diner;

import com.example.mat.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Entity
public class DinerCategory extends BaseEntity{
  @SequenceGenerator(name = "diner_category_seq_gen", sequenceName = "diner_category_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diner_category_seq_gen")
    @Id
    private Long dcid;

    private String name;
}
