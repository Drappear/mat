package com.example.mat.entity.diner;

import com.example.mat.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Diner extends BaseEntity {
    @SequenceGenerator(name = "diner_seq_gen", sequenceName = "diner_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diner_seq_gen")
    @Id
    private Long did;

    private String name;
    private String address;
    private String addressDetail;
    private String phone;
    private String content;
    private String menu;
    private String workTime;
    private Long viewCount;
    private String regNum;
}
