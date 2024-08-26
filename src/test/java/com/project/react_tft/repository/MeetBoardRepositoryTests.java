package com.project.react_tft.repository;

import com.project.react_tft.Repository.MeetBoardRepository;
import com.project.react_tft.domain.Board;
import com.project.react_tft.domain.MeetBoard;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Log4j2
public class MeetBoardRepositoryTests {

    @Autowired
    private MeetBoardRepository meetBoardRepository;

    @Test
    public void testSearchAll2() {

        String[] types = {"t", "c", "w"};

        String keyword = "test";

        Pageable pageable = PageRequest.of(0, 5, Sort.by("meetId").descending());

        Page<MeetBoard> result = meetBoardRepository.searchAll(types, keyword, pageable);

        // total pages
        log.info(result.getTotalPages());

        // pag size
        log.info(result.getSize());

        // pageNumber
        log.info(result.getNumber());

        //prev next
        log.info(result.hasPrevious() + ":=+ " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));

    }
}
