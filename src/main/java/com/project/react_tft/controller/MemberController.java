package com.project.react_tft.controller;

import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.MemberDTO;
import com.project.react_tft.security.CustomUserDetailsService;
import com.project.react_tft.service.MemberService;
import com.project.react_tft.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {
    private final MemberService memberService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;



    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody MemberDTO memberDTO) {
        try {
            Member member = memberService.signUp(memberDTO);
            return ResponseEntity.ok(member);
        } catch (MemberService.MemberMidExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입중 오류발생...");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO dto) {
        Member member = memberService.login(dto.getMid(), dto.getMpw());

        if (member != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getMid());

            // 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 인증 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Payload 값 보내기
            Map<String, Object> claim = new HashMap<>();
            claim.put("mid", member.getMid());
            claim.put("mpw", member.getMpw());
            claim.put("role", userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            String accessToken = jwtUtil.generateToken(claim, 1);
            String refreshToken = jwtUtil.generateToken(claim, 30);

            Map<String, String> tokens = Map.of("accessToken", accessToken, "refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 및 비밀번호 오류임.");
        }
    }




}

