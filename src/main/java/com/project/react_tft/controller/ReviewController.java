package com.project.react_tft.controller;

import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.MovieDTO;
import com.project.react_tft.dto.ReviewDTO;
import com.project.react_tft.service.MemberService;
import com.project.react_tft.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReviewDTO reviewDTO, BindingResult bindingResult) throws BindException, MemberService.MemberMidExistException {
        log.info("register review: {}", reviewDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = authentication.getName();
        log.info(principal);
        log.info("username : " + username );
        if("".equals(username) || (username == null) || "anonymousUser".equals(username)){
            log.info("no data username : " + username );
            throw new MemberService.MemberMidExistException();
        }
        else {
            reviewDTO.setMid(authentication.getName());
        }

        log.info("register review2: {}", reviewDTO);

        Long result = reviewService.register(reviewDTO);

        resultMap.put("result", result);

        return resultMap;
    }




}
