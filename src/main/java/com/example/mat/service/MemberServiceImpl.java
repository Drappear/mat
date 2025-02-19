package com.example.mat.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.shin.AuthMemberDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.MemberImageDto;
import com.example.mat.dto.shin.PasswordDto;
import com.example.mat.dto.shin.UpdateMemberDto;

import com.example.mat.entity.shin.Member;
import com.example.mat.entity.shin.MemberImage;
import com.example.mat.repository.BoardRepository;
import com.example.mat.repository.CartRepository;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.RecipeRepository;
import com.example.mat.repository.diner.DinerReviewRepository;
import com.example.mat.repository.shin.MemberImageRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements UserDetailsService, MemberService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;
    private final CartRepository cartRepository;
    private final RecipeRepository recipeRepository;
    private final DinerReviewRepository dinerReviewRepository;

    @Value("${com.example.mat.profile.path}")
    private String uploadPath;

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

        MemberImage memberImage = memberImageRepository.findByMember(member);

        MemberDto memberDto = MemberDto.builder()
                .mid(member.getMid())
                .username(member.getUsername())
                .userid(member.getUserid())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .email(member.getEmail())
                .tel(member.getTel())
                .bio(member.getBio())
                .addr(member.getAddr())
                .detailAddr(member.getDetailAddr())
                .role(member.getRole())
                .build();

        if (memberImage != null) {
            MemberImageDto memberImageDto = MemberImageDto.builder()
                    .uuid(memberImage.getUuid())
                    .imgName(memberImage.getImgName()).build();
            memberDto.setMemberImageDto(memberImageDto);
        }

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

    @Override
    public boolean checkDuplicateNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    @Override
    public void personalUpdate(UpdateMemberDto updatememberDto) {

        // log.info("서비스단 {}", updatememberDto);
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

        // 자식 프로필이미지
        MemberImage memberImage = memberImageRepository.findByMember(member);

        boardRepository.deleteByMember(member);
        recipeRepository.deleteByMember(member);
        dinerReviewRepository.deleteByMember(member);
        cartRepository.deleteByMember(member);
        memberImageRepository.delete(memberImage);

        memberRepository.deleteById(member.getMid());
    }

    @Transactional
    @Override
    public void updateProfile(MemberDto memberDto) {
        memberRepository.updateProfile(memberDto.getNickname(), memberDto.getBio(), memberDto.getUserid());
    }

    @Transactional
    @Override
    public void saveMemberWithImage(MemberImageDto memberImageDto) {

        Member member = memberRepository.findById(memberImageDto.getMid()).get();

        MemberImage memberImage = memberImageRepository
                .findByMember(member);

        // 이미지 저장
        if (memberImage != null) {
            memberImage.setImgName(memberImageDto.getImgName());
            memberImage.setUuid(memberImageDto.getUuid());
        } else {
            memberImage = new MemberImage();
            memberImage.setImgName(memberImageDto.getImgName());
            memberImage.setUuid(memberImageDto.getUuid());
            memberImage.setMember(Member.builder().mid(memberImageDto.getMid()).build());
        }

        memberImageRepository.save(memberImage);

    }

    // 결제 시, 멤버 정보 가져오기
    @Override
    public MemberDto getMemberById(Long mid) {
        Member member = memberRepository.findById(mid)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));
        return MemberDto.builder()
                .mid(member.getMid())
                .username(member.getUsername())
                .email(member.getEmail())
                .tel(member.getTel())
                .addr(member.getAddr())
                .detailAddr(member.getDetailAddr())
                .build();
    }

}