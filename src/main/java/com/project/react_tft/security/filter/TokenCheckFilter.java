package com.project.react_tft.security.filter;

import com.project.react_tft.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(!path.startsWith("/api")){
            filterChain.doFilter(request, response);
            return;
        }
        log.info("토큰필터 체인입니다~~");
        log.info("JWTUtil 값입니다~~" + jwtUtil);

        filterChain.doFilter(request,response);

    }
}
