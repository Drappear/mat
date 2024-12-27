package com.example.mat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.MemberImageDto;
import com.example.mat.dto.shin.PasswordDto;
import com.example.mat.dto.shin.UpdateMemberDto;
import com.example.mat.entity.constant.MemberRole;
import com.example.mat.entity.shin.Member;
import com.example.mat.entity.shin.MemberImage;

public interface MemberService {
    // 닉네임 수정
    // void nickUpdate(MemberDto memberDto);
    void updateProfile(MemberDto memberDto);

    void saveMemberWithImage(MemberImageDto memberImageDto);

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

    default Map<String, Object> dtoToEntityMap(MemberDto memberDto) {

        Map<String, Object> resultMap = new HashMap<>();
        Member member = Member.builder()
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

        resultMap.put("member", member);

        MemberImageDto memberImageDto = memberDto.getMemberImageDto();

        // if (memberImageDto != null) {
        // MemberImage memberImage = MemberImage.builder()
        // .uuid(memberImageDto.getUuid())
        // .imgName(memberImageDto.getImgName())
        // .path(memberImageDto.getPath())
        // .member(member)
        // .build();

        // resultMap.put("memberImage", memberImage);
        // }

        return resultMap;
    }

}