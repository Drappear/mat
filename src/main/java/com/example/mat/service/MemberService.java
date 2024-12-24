package com.example.mat.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.MemberImageDto;
import com.example.mat.dto.shin.PasswordDto;
import com.example.mat.dto.shin.UpdateMemberDto;
import com.example.mat.entity.constant.MemberRole;
import com.example.mat.entity.shin.Member;
import com.example.mat.entity.shin.MemberImage;

public interface MemberService {
    // 닉네임 수정
    void nickUpdate(MemberDto memberDto);

    // 개인정보 수정
    void personalUpdate(UpdateMemberDto updatememberDto);

    // 비밀번호 수정
    void passwordUpdate(PasswordDto passwordDto) throws Exception;

    // 회원탈퇴
    void leave(PasswordDto passwordDto) throws Exception;

    // 회원가입
    String register(MemberDto memberDto);

    // 이메일, 아이디, 닉네임 증복검사
    boolean checkDuplicateUserid(String userid);

    // boolean checkDuplicateEmail(String email);

    boolean checkDuplicateNickname(String nickname);

    // dtoToEntity
    default Member dtoToEntity(MemberDto memberDto) {
        return Member.builder()
                .mid(memberDto.getMid())
                .userid(memberDto.getUserid())
                .username(memberDto.getUsername())
                .nickname(memberDto.getNickname())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .tel(memberDto.getTel())
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
                .tel(member.getTel())
                .addr(member.getAddr())
                .detailAddr(member.getDetailAddr())
                .role(MemberRole.MEMBER)
                .build();
    }

    // default MemberImageDto entityToDto(Member member, List<MemberImage>
    // memberImages) {
    // // MovieImage => MovieImageDto 변경 후 리스트 작업
    // List<MemberImageDto> memberImageDtos = memberImages.stream().map(memberImage
    // -> {
    // return MemberImageDto.builder()
    // .inum(memberImage.getInum())
    // .uuid(memberImage.getUuid())
    // .imgName(memberImage.getImgName())
    // .path(memberImage.getPath())
    // .build();
    // }).collect(Collectors.toList());

    // memberDto.setMemberImageDtos(memberImageDtos);

    // return memberDto;
    // }
}