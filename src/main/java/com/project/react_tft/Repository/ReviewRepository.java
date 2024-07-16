package com.project.react_tft.Repository;

import com.project.react_tft.domain.Review;
import com.project.react_tft.dto.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

//    @Query("select r from Review r where r.movie.reviews = :movie_id")
    @Query("select r from Review r")
    List<Review> listOfReview();
}
