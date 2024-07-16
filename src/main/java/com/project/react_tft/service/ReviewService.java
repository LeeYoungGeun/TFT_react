package com.project.react_tft.service;

import com.project.react_tft.domain.Review;
import com.project.react_tft.dto.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface ReviewService {

    Logger log = LoggerFactory.getLogger(ReviewService.class);

    class ReviewIdExistException extends Exception {
       public ReviewIdExistException() {
            log.info("ReviewIdExistException");
       }
    }

    Long register(ReviewDTO reviewDTO);
    ReviewDTO readOne(Long reviewId) throws ReviewIdExistException;
    void modify(ReviewDTO reviewDTO) throws ReviewIdExistException;
    void remove(Long bno) throws ReviewIdExistException;
    List<Review> listOfReview()throws ReviewIdExistException;
}
