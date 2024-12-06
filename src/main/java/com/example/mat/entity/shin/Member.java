package com.example.mat.entity.shin;

import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.constant.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Mat_Member")
public class Member extends BaseEntity {

    @Id
    @SequenceGenerator(name = "mat_member_seq_gen", sequenceName = "mat_member_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mat_member_seq_gen")
    private Long mid;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String userid;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;
    private String address;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

}