package com.project.react_tft.security;

import com.project.react_tft.dto.MemberSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("-----------------------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodePw = memberSecurityDTO.getMpw();

        // 소셜 로그인이고 회원 패스워드가 '1111' (기본값) 일 경우 비밀번호 변경 유도
        if (memberSecurityDTO.isSocial() && ("1111".equals(encodePw) || passwordEncoder.matches("1111", encodePw))) {
            log.info("비번 바꾸던가");
            response.sendRedirect("http://localhost:3000/");
        } else {
            response.sendRedirect("http://localhost:3000/");
        }
    }
}
