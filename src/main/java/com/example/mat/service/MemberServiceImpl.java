package com.example.mat.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mat.dto.shin.AuthMemberDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.PasswordDto;
import com.example.mat.dto.shin.UpdateMemberDto;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements UserDetailsService, MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("service username : {}", username);

        // 로그인 요청
        Optional<Member> result = memberRepository.findByUserid(username);

        log.info("result", result.get());

        if (!result.isPresent()) {
            throw new UsernameNotFoundException("아이디 확인");
        }

        Member member = result.get();

        MemberDto memberDto = MemberDto.builder()
                .mid(member.getMid())
                .username(member.getUsername())
                .userid(member.getUserid())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .email(member.getEmail())
                .tel(member.getTel())
                .addr(member.getAddr())
                .detailAddr(member.getDetailAddr())
                .role(member.getRole())
                .build();

        return new AuthMemberDto(memberDto);
    }

    @Override
    @Transactional
    public String register(MemberDto memberDto) {
        Member member = dtoToEntity(memberDto);
        member.setPassword((passwordEncoder.encode(member.getPassword())));
        // 객체 상태 로그 확인
        log.info("회원 정보: {}", member);
        return memberRepository.save(member).getNickname();

    }

    @Override
    public boolean checkDuplicateUserid(String userid) {
        return memberRepository.existsByUserid(userid);

    }

    // @Override
    // public boolean checkDuplicateEmail(String email) {
    // return memberRepository.existsByEmail(email);
    // }

    @Override
    public boolean checkDuplicateNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    @Override
    public void nickUpdate(MemberDto memberDto) {
        memberRepository.updateNickname(memberDto.getNickname(), memberDto.getUserid());
    }

    @Transactional
    @Override
    public void personalUpdate(UpdateMemberDto updatememberDto) {

        log.info("서비스단 {}", updatememberDto);
        // 레포지토리 메서드를 호출하여 모든 정보를 한 번에 업데이트
        memberRepository.updatePersonalInfo(
                updatememberDto.getEmail(),
                updatememberDto.getAddr(),
                updatememberDto.getDetailAddr(),
                updatememberDto.getTel(),
                updatememberDto.getUserid());
    }

    @Override
    public void passwordUpdate(PasswordDto passwordDto) throws Exception {
        // email 을 이용해 사용자 찾기
        // Optional<Member> result =
        // memberRepository.findByEmail(passwordDto.getEmail());
        // if (!result.isPresent()) throw new UsernameNotFoundException("이메일 확인");

        Member member = memberRepository.findByUserid(passwordDto.getUserid()).get();

        // 현재 비밀번호(db에 저장된 값)가 입력 비밀번호(입력값)와 일치하는지 검증
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), member.getPassword())) {
            // false : 되돌리기
            throw new Exception("현재 비밀번호를 확인해 주세요");
        } else {
            // true : 새 비밀번호로 수정
            member.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
            memberRepository.save(member);

        }
    }

    @Transactional
    @Override
    public void leave(PasswordDto passwordDto) throws Exception {
        Member member = memberRepository.findByUserid(passwordDto.getUserid()).get();

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), member.getPassword())) {
            throw new Exception("현재 비밀번호를 확인");
        }

        // reviewRepository.deleteByMember(member);

        memberRepository.deleteById(member.getMid());
    }
}