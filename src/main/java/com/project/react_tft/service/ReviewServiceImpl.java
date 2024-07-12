package com.project.react_tft.service;

import com.project.react_tft.Repository.ReviewRepository;
import com.project.react_tft.domain.Review;
import com.project.react_tft.dto.ReviewDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(ReviewDTO reviewDTO) {
        Review review = modelMapper.map(reviewDTO, Review.class);
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
}
