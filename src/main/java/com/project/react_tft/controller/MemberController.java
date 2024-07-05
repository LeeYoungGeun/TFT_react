package com.project.react_tft.controller;

import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.MemberDTO;


import com.project.react_tft.security.CustomUserDetailsService;
import com.project.react_tft.service.MemberService;
import com.project.react_tft.util.JWTUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
@EnableWebSecurity
public class MemberController {
    private final MemberService memberService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/signup")
    public void signupGET() {
        log.info("로그인창 들어옴");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signUp")
    public Member signUp(@RequestBody MemberDTO memberDTO) {

        try {
            Member member = memberService.signUp(memberDTO);

            return member;
        }catch (MemberService.MemberMidExistException e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody MemberDTO dto) {

        Member member = memberService.login(
                dto.getMid(),
                dto.getMpw());

        if (member != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getMid());

            //토큰 생성.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());

            //인증설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Payload 값 보내기.
            Map<String, Object> claim = new HashMap<>();
            claim.put("mid",member.getMid());
            claim.put("mpw",member.getMpw());
            claim.put("memail",member.getMemail());
            claim.put("role", userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            String accessToken = jwtUtil.generateToken(claim, 1);

            Map<String, String> tokens = Map.of("accessToken", accessToken);


            return ResponseEntity.ok(tokens + "토큰내용+++++++++++++++++++++++++");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 비밀번호 제대로 확인부탁");
        }
    }






}
