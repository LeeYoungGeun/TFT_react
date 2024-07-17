package com.project.react_tft.service;

import com.project.react_tft.Repository.MovieRepository;
import com.project.react_tft.domain.Movie;
import com.project.react_tft.dto.MovieDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(MovieDTO movieDTO) {
        Movie movie = modelMapper.map(movieDTO, Movie.class);
        return movieRepository.save(movie).getMovie_id();
    }
}
