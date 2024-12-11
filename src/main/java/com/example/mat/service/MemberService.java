package com.example.mat.service;

import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.PasswordDto;
import com.example.mat.entity.constant.MemberRole;
import com.example.mat.entity.shin.Member;

public interface MemberService {
    // 닉네임 수정
    void nickUpdate(MemberDto memberDto);

    // 비밀번호 수정
    void passwordUpdate(PasswordDto passwordDto) throws Exception;
    // 회원탈퇴

    // 회원가입
    String register(MemberDto memberDto);

    // dtoToEntity
    default Member dtoToEntity(MemberDto memberDto) {
        return Member.builder()
                .mid(memberDto.getMid())
                .userid(memberDto.getUserid())
                .username(memberDto.getUsername())
                .nickname(memberDto.getNickname())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .addr(memberDto.getAddr())
                .detailAddr(memberDto.getDetailAddr())
                .role(MemberRole.MEMBER)
                .build();
    }

    default MemberDto dtoToEntity(Member member) {
        return MemberDto.builder()
                .mid(member.getMid())
                .username(member.getUsername())
                .userid(member.getUserid())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .email(member.getEmail())
                .addr(member.getAddr())
                .detailAddr(member.getDetailAddr())
                .role(MemberRole.MEMBER)
                .build();
    }
}