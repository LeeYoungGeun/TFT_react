package com.project.react_tft.service;

import com.project.react_tft.dto.MovieDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class MovieServiceTests {

    @Autowired
    private MovieService movieService;

    @Test
    public void testMovieRegister() throws MovieService.MovieIdExistException {
        log.info(movieService.getClass().getName());

        MovieDTO movieDTO = MovieDTO.builder()
                .movie_id(2L)
                .movie_title("테스트 영화제목2")
                .build();

        long result = movieService.register(movieDTO);

        log.info(result);
    }

}
