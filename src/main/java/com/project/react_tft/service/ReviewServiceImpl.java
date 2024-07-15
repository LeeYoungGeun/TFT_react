package com.project.react_tft.service;

import com.project.react_tft.Repository.MemberRepository;
import com.project.react_tft.Repository.MovieRepository;
import com.project.react_tft.Repository.ReviewRepository;
import com.project.react_tft.domain.Member;
import com.project.react_tft.domain.Movie;
import com.project.react_tft.domain.Review;
import com.project.react_tft.dto.ReviewDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Long register(ReviewDTO reviewDTO) {
        log.info("reviewDTO" + reviewDTO);

        Member member = memberRepository.findById(reviewDTO.getMid())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Movie movie = movieRepository.findById(reviewDTO.getMovie_id())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Review review =  Review.builder()
                .member(member)
                .movie(movie)
                .review_text(reviewDTO.getReview_text())
                .review_star(reviewDTO.getReview_star())
                .build();

        return reviewRepository.save(review).getReview_id();
    }

    @Override
    public ReviewDTO readOne(Long reviewId) {
        Optional<Review> result = reviewRepository.findById(reviewId);
        Review review = result.orElseThrow();
        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    public void modify(ReviewDTO reviewDTO) {
        Optional<Review> result = reviewRepository.findById(reviewDTO.getReview_id());
        Review review = result.orElseThrow();
        review.change(reviewDTO.getReview_text(),reviewDTO.getReview_star());
        reviewRepository.save(review);
    }

    @Override
    public void remove(Long review_id) {
        reviewRepository.deleteById(review_id);
    }

    @Override
    public List<Review> listOfReview(){
        List<Review> dtoList = reviewRepository.listOfReview();
        log.info("dtoList" + dtoList);
        return dtoList;
    }
}
