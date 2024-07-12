package com.project.react_tft.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    //리뷰id
    private Long review_id;
    //영화 id
    private Long movie_id;
    //맴버 id
    private String mid;
    //리뷰내용
    private String review_text;
    //별점
    private Integer review_star;

}
