package com.project.react_tft.repository;

import com.project.react_tft.Repository.MeetBoardRepository;
import com.project.react_tft.domain.Board;
import com.project.react_tft.domain.MeetBoard;
import com.project.react_tft.domain.MeetBoardImage;
import com.project.react_tft.dto.MeetBoardListAllDTO;
import com.project.react_tft.dto.MeetBoardListReplyCountDTO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.UUID;

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

    @Test
    public void testSearchReplyCount() {

        String[] types = {"t","w","c"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("meetId").descending());

        Page<MeetBoardListReplyCountDTO> result = meetBoardRepository.searchWithMeetReplyCount(types,keyword,pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + ":=+ " + result.hasNext());
        result.getContent().forEach(meetBoard -> log.info(meetBoard));
    }

    @Test
    public void testInsertWithImage() {

        MeetBoard meetBoard = MeetBoard.builder()
                .meetTitle("이미지테스트")
                .meetContent("첨부파일테스트")
                .meetWriter("쳐들어가라좀")
                .build();

        for (int i = 0; i < 3; i++){
            meetBoard.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        }
        meetBoardRepository.save(meetBoard);
    }

    @Test
    public void testReadWithImages() {

        Optional<MeetBoard> result = meetBoardRepository.findByIdWithImages(4L);

        MeetBoard meetBoard = result.orElseThrow();

        log.info(meetBoard);
        log.info("************************");
        for (MeetBoardImage meetBoardImage : meetBoard.getImageSet()){
            log.info(meetBoardImage);
        }
    }


    @Transactional
    @Test
    public void testSearchImageReplyCount(){

        Pageable pageable = PageRequest.of(0, 10,Sort.by("meetId").descending());

        Page<MeetBoardListReplyCountDTO> result = meetBoardRepository.searchWithMeetReplyCount(null, null, pageable);

        log.info("----------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(meetBoardListAllDTO -> log.info(meetBoardListAllDTO));

    }

}


