package com.project.react_tft.repository;

import com.project.react_tft.Repository.ReviewRepository;
import com.project.react_tft.domain.Review;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMovieTest(){

        Review review = Review.builder()
                .review_id(1L)
                .review_text("리뷰1")
                .review_star(5)
                .build();

        reviewRepository.save(review);
    }

}
