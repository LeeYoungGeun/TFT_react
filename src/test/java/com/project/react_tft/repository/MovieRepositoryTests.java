package com.project.react_tft.repository;

import com.project.react_tft.Repository.MovieRepository;
import com.project.react_tft.domain.Movie;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class MovieRepositoryTests {


    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void insertMovieTest(){

        Movie movie = Movie.builder()
                .movie_id(1L)
                .movie_title("테스트 영화 03")
                .build();

        movieRepository.save(movie);
    }

}
