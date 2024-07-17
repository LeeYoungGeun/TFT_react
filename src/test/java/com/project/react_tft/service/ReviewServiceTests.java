package com.project.react_tft.service;

import com.project.react_tft.domain.Review;
import com.project.react_tft.dto.ReviewDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Log4j2
@SpringBootTest
public class ReviewServiceTests {

    @Autowired
    private ReviewService reviewService;

    @Test
    public void testReviewRegister(){
        log.info(reviewService.getClass().getName());

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .review_id(3L)
                .review_star(10)
                .review_text("리뷰 테스트2")
                .mid("user1")
                .movie_id(2L)
                .build();

        long result = reviewService.register(reviewDTO);

        log.info(result);
    }

    @Test
    public void testReviewReadOne() throws ReviewService.ReviewIdExistException {
        long review_id = 1L;

        ReviewDTO reviewDTO = reviewService.readOne(review_id);

        log.info(reviewDTO);
    }

    @Test
    public void testReviewModify() throws ReviewService.ReviewIdExistException {
       ReviewDTO reviewDTO = ReviewDTO.builder()
               .review_id(3L)
               .review_text("수정 테스트")
               .review_star(4)
               .build();
       reviewService.modify(reviewDTO);
       log.info(reviewService.readOne(reviewDTO.getReview_id()));
    }

    @Test
    public void testReviewDelete() throws ReviewService.ReviewIdExistException {
        long review_id = 3L;
        reviewService.remove(review_id);
    }

//    @Test
//    public void testReviewList() throws ReviewService.ReviewIdExistException {
//        List<Review> reviewList =  reviewService.listOfReview();
//        log.info(reviewList);
//    }

}
