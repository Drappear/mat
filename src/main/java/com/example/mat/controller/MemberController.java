package com.example.mat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.shin.AuthMemberDto;
// import com.example.mat.dto.shin.AuthMemberDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.UpdateMemberDto;
import com.example.mat.service.MemberService;

import jakarta.validation.Valid;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public void getMethodName(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("로그인 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public void getProfile(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("프로필 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public void getEdit(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("프로필 수정 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/nickname")
    public String postUpdateName(MemberDto memberDto, BindingResult result) {
        log.info("닉네임 수정 {}", memberDto);

        Authentication authentication = getAuthentication();
        // MemberDto 에 들어있는 값 접근 시
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        memberDto.setUserid((authMemberDto.getMemberDto().getUserid()));
        String currentNickname = authMemberDto.getMemberDto().getNickname();

        if (currentNickname.equals(memberDto.getNickname())) {
            log.info("입력된 닉네임이 현재 닉네임과 동일합니다.");
            return "redirect:/member/profile";
        }
        if (result.hasErrors()) {
            return "/member/edit";
        }
        if (memberService.checkDuplicateNickname(memberDto.getNickname())) {
            result.rejectValue("nickname", "error.nickname", "이미 사용 중인 닉네임입니다.");
            return "/member/edit";
        }
        memberService.nickUpdate(memberDto);

        // SecurityContext 에 보관된 값 업데이트
        authMemberDto.getMemberDto().setNickname(memberDto.getNickname());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/personalInformation")
    public void getDetailmember(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("회원 정보 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editpi")
    public void getEditPI(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("회원 정보 수정 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editpi")
    public String postEditPI(
            @Valid @ModelAttribute("updateMemberDto") UpdateMemberDto updatememberDto,
            BindingResult result) {
        log.info("회원 정보 수정 요청: {}", updatememberDto);

        if (result.hasErrors()) {
            return "/member/editpi";
        }

        // 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();

        // 개인정보 업데이트 수행
        memberService.personalUpdate(updatememberDto);

        // SecurityContext의 정보를 업데이트
        authMemberDto.getMemberDto().setEmail(updatememberDto.getEmail());
        authMemberDto.getMemberDto().setTel(updatememberDto.getTel());
        authMemberDto.getMemberDto().setAddr(updatememberDto.getAddr());
        authMemberDto.getMemberDto().setDetailAddr(updatememberDto.getDetailAddr());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/personalInformation";
    }

    // 회원 가입
    @GetMapping("/register")
    public void getRegister(MemberDto memberDto, @ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("회원가입 폼 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid MemberDto memberDto, BindingResult result,
            boolean check,
            @ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("회원가입 요청 {}", memberDto);

        if (result.hasErrors()) {

            // 사용자 ID 중복 검사
            if (memberService.checkDuplicateUserid(memberDto.getUserid())) {
                result.rejectValue("userid", "error.userid", "이미 사용 중인 아이디입니다.");
                return "/member/register";
            }
            if (memberService.checkDuplicateNickname(memberDto.getNickname())) {
                result.rejectValue("nickname", "error.nickname", "이미 사용 중인 닉네임입니다.");
                return "/member/register";
            }
            return "/member/register";
        }

        memberService.register(memberDto);

        return "redirect:/member/login";
    }

    // 이메일 중복 검사
    // if (memberService.checkDuplicateEmail(memberDto.getEmail())) {
    // result.rejectValue("email", "error.email", "이미 사용 중인 이메일입니다.");
    // return "/member/register";
    // }

    @GetMapping("/check-duplicate-userid")
    public ResponseEntity<Boolean> checkDuplicateUserid(@RequestParam String userid) {
        boolean isDuplicate = memberService.checkDuplicateUserid(userid);
        return ResponseEntity.ok(isDuplicate);
    }

    // @GetMapping("/check-duplicate-email")
    // public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String
    // email) {
    // boolean isDuplicate = memberService.checkDuplicateEmail(email);
    // return ResponseEntity.ok(isDuplicate);
    // }

    @GetMapping("/check-duplicate-nickname")
    public ResponseEntity<Boolean> checkDuplicateNickname(@RequestParam String nickname) {

        boolean isDuplicate = memberService.checkDuplicateNickname(nickname);
        return ResponseEntity.ok(isDuplicate);
    }

    // 개발자용 - Authentication 확인용
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication;

    }
}