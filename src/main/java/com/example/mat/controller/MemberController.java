package com.example.mat.controller;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.sql.model.jdbc.UpsertOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.UploadResultDto;
import com.example.mat.dto.shin.AuthMemberDto;
// import com.example.mat.dto.shin.AuthMemberDto;
import com.example.mat.dto.shin.MemberDto;
import com.example.mat.dto.shin.MemberImageDto;
import com.example.mat.dto.shin.PasswordDto;
import com.example.mat.dto.shin.UpdateMemberDto;
import com.example.mat.entity.shin.Member;
import com.example.mat.entity.shin.MemberImage;
import com.example.mat.repository.shin.MemberImageRepository;
import com.example.mat.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    private final MemberService memberService;

    @Value("${com.example.mat.profile.path}")
    private String uploadPath;

    @PostMapping("/image")
    public ResponseEntity<MemberImageDto> postUpload(MultipartFile uploadFiles) {

        log.info("OriginalFilename {}", uploadFiles.getOriginalFilename());
        log.info("Size {}", uploadFiles.getSize());
        log.info("ContentType {}", uploadFiles.getContentType()); // image/png
        MemberImageDto memberImageDto = null;
        log.info("### [START] 이미지 업로드 처리 시작 ###");

        // 이미지 파일 여부 확인
        if (!uploadFiles.getContentType().startsWith("image")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 사용자가 올린 파일명
        String originName = uploadFiles.getOriginalFilename();

        // 년/월/일
        // String saveFolderPath = makeFolder();

        // 파일저장 - uuid(중복파일 해결)
        String uuid = UUID.randomUUID().toString();
        // upload/2024/11/26/9fae42cf-0733-453f-b3b9-3bfca31a6fe2_1.jpg
        String saveName = uploadPath + File.separator + uuid + "_" + originName;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        Path savePath = Paths.get(saveName);

        try {
            // 폴더 저장
            uploadFiles.transferTo(savePath);

            memberImageDto = new MemberImageDto();
            memberImageDto.setUuid(uuid);
            memberImageDto.setImgName(originName);
            memberImageDto.setMid(authMemberDto.getMemberDto().getMid());

            log.info("memberImageDt {}", memberImageDto);

            // db 저장
            memberService.saveMemberWithImage(memberImageDto);

            authMemberDto.getMemberDto().setMemberImageDto(memberImageDto);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("프로필사진 {}", memberImageDto.getImageURL());
        return new ResponseEntity<>(memberImageDto, HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {

        log.info("fileName: " + fileName);

        ResponseEntity<byte[]> result = null;

        try {
            // "2024%2F11%2F27%5C7e9547c0-ba45-463b-a4ae-59a35d92962a_seoul1.jpg"
            String srcFileName = URLDecoder.decode(fileName, "utf-8");
            // upload/2024/11/27/s_C7e9547c0-ba45-463b-a4ae-59a35d92962a_seoul1.jpg
            File file = new File(uploadPath + File.separator + srcFileName);

            if (size != null && size.equals("1")) {
                // upload/2024/11/27/, 원본파일명
                file = new File(file.getParent(), file.getName().substring(2));
            }

            HttpHeaders headers = new HttpHeaders();
            // Content-Type : image/png or text/html
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

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
    @PostMapping("/edit/profile")
    public String postUpdateProfile(@ModelAttribute MemberDto memberDto, BindingResult result,
            RedirectAttributes redirectAttributes) {
        log.info("프로필 수정 요청: {}", memberDto);
        log.info("bio값: {}", memberDto.getBio());
        log.info("memberImageDto값: {}", memberDto.getMemberImageDto());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();

        // 현재 사용자 ID 설정
        memberDto.setUserid(authMemberDto.getMemberDto().getUserid());
        // 닉네임 중복 확인
        if (!authMemberDto.getMemberDto().getNickname().equals(memberDto.getNickname()) &&
                memberService.checkDuplicateNickname(memberDto.getNickname())) {
            result.rejectValue("nickname", "error.nickname", "이미 사용 중인 닉네임입니다.");
            return "/member/edit"; // 수정 페이지로 이동
        }

        // 프로필 업데이트
        memberService.updateProfile(memberDto);

        // SecurityContextHolder 값 업데이트
        authMemberDto.getMemberDto().setNickname(memberDto.getNickname());
        authMemberDto.getMemberDto().setBio(memberDto.getBio());
        // authMemberDto.getMemberDto().setMemberImageDto(memberImageDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 성공적으로 수정한 후 리다이렉트
        redirectAttributes.addFlashAttribute("message", "프로필이 성공적으로 수정되었습니다.");
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

    @GetMapping("/editPassword")
    public void getEditPassword(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("비밀번호 변경 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/password")
    public String postUpdatePassword(PasswordDto passwordDto, HttpSession session, RedirectAttributes rttr) {
        log.info("비밀번호 수정");

        // 서비스 호출
        try {
            memberService.passwordUpdate(passwordDto);

        } catch (Exception e) {
            // 실패시 /edit
            e.printStackTrace();
            rttr.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/editPassword";
        }
        // 성공 시 세션 해제 후 /login 이동
        session.invalidate();
        return "redirect:/member/login";
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

        return "/member/login";
    }

    @GetMapping("/check-duplicate-userid")
    public ResponseEntity<Boolean> checkDuplicateUserid(@RequestParam String userid) {
        boolean isDuplicate = memberService.checkDuplicateUserid(userid);
        return ResponseEntity.ok(isDuplicate);
    }

    @GetMapping("/check-duplicate-nickname")
    public ResponseEntity<Boolean> checkDuplicateNickname(@RequestParam String nickname) {

        boolean isDuplicate = memberService.checkDuplicateNickname(nickname);
        return ResponseEntity.ok(isDuplicate);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/leave")
    public void getLeave(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("회원 탈퇴 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/leave")
    public String postLeave(PasswordDto passwordDto, boolean check, HttpSession session, RedirectAttributes rttr) {
        log.info("회원 탈퇴 요청 {}, {}", passwordDto, check);
        if (!check) {
            rttr.addFlashAttribute("error", "체크표시를 확인해 주세요");
            return "/member/leave";
        }
        // 서비스 작업
        try {
            memberService.leave(passwordDto);
        } catch (Exception e) {
            e.printStackTrace();
            rttr.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/leave";
        }
        session.invalidate();
        return "redirect:/diner/list";
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