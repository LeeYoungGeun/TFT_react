package com.project.react_tft.controller;

import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.MemberDTO;


import com.project.react_tft.security.CustomUserDetailsService;
import com.project.react_tft.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {
    private final MemberService memberService;
    private final CustomUserDetailsService customUserDetailsService;

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

        if (member == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getMid());
        }
        return ResponseEntity.ok("로그인성공");
    }






}
