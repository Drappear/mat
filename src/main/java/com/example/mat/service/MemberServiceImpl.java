package com.example.mat.service;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mat.dto.shin.AuthMemberDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.PasswordDto;

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

    @Override
    public void passwordUpdate(PasswordDto passwordDto) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'passwordUpdate'");
    }

}