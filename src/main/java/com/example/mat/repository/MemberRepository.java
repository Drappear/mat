package com.example.mat.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mat.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    
   
}