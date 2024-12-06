package com.example.mat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.shin.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
