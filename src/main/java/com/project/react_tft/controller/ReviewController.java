package com.project.react_tft.controller;

import com.project.react_tft.Repository.MemberRepository;
import com.project.react_tft.Repository.MovieRepository;
import com.project.react_tft.Repository.ReviewRepository;
import com.project.react_tft.domain.Member;
import com.project.react_tft.domain.Movie;
import com.project.react_tft.domain.Review;
import com.project.react_tft.dto.ReviewDTO;
import com.project.react_tft.dto.ReviewPageRequestDTO;
import com.project.react_tft.dto.ReviewPageResponseDTO;
import com.project.react_tft.service.MemberService;
import com.project.react_tft.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

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

    @PostMapping(value = "/listOfReviewPaginated", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReviewPageResponseDTO<ReviewDTO> listOfReviewPaginated(@RequestBody ReviewPageRequestDTO requestDTO, BindingResult bindingResult) throws BindException {
        log.info("listOfReviewPaginated: {}", requestDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        
        Page<Review> result = reviewService.listOfReviewPaginated(requestDTO.getMovie_id(), requestDTO.getPage(), requestDTO.getSize() );
        List<ReviewDTO> resultList = result.getContent().stream().map(review ->
                ReviewDTO.builder()
                        .review_id(review.getReview_id())
                        .movie_id(review.getMovie().getMovie_id())
                        .review_star(review.getReview_star())
                        .review_text(review.getReview_text())
                        .mid(review.getMember().getMid())
                        .build()
        ).collect(Collectors.toList());

        int starts = reviewRepository.getSumStarRatingByMovieId(requestDTO.getMovie_id());

        log.info("result : {}", result.getContent());
        log.info("resultList : {}", resultList);
        log.info("All start : {}", starts);

        return ReviewPageResponseDTO.<ReviewDTO>withAll()
                .reviewPageRequestDTO(requestDTO)
                .dtoList(resultList)
                .total((int) result.getTotalElements())
                .allStars(starts)
                .build();

        //아래처럼 사용가능
        /*Page<Review> result = reviewService.listOfReviewPaginated(requestDTO.getMovie_id(), requestDTO.getPage(), requestDTO.getSize());
        log.info(result.getContent());
        List<ReviewDTO> re = new ArrayList<>();
        result.getContent().forEach(review -> {
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setReview_id(review.getReview_id());
            reviewDTO.setMovie_id(review.getMovie().getMovie_id());
            reviewDTO.setReview_text(review.getReview_text());
            reviewDTO.setReview_star(review.getReview_star());
            reviewDTO.setMid(review.getMember().getMid());
            re.add(reviewDTO);
        });
        return re;*/
    }


}
