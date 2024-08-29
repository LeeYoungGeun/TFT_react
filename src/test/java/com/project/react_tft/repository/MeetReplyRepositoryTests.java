package com.project.react_tft.repository;


import com.project.react_tft.Repository.MeetReplyRepository;
import com.project.react_tft.domain.MeetBoard;
import com.project.react_tft.domain.MeetReply;
import com.project.react_tft.dto.MeetBoardListReplyCountDTO;
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
public class MeetReplyRepositoryTests {

    @Autowired
    private MeetReplyRepository meetReplyRepository;

    @Test
    public void testInsert() {

        Long meetId = 1L;

        MeetBoard meetBoard = MeetBoard.builder().meetId(meetId).build();

        MeetReply meetReply = MeetReply.builder()
                .meetBoard(meetBoard)
                .replyText("댓글2")
                .replyer("테스트유저2")
                .build();

        meetReplyRepository.save(meetReply);
    }

    @Test
    public void testBoardReplyes() {
        Long meetId = 1L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("meetRid").descending());

        Page<MeetReply> result = meetReplyRepository.listOfMeetBoard(meetId, pageable);

        result.getContent().forEach(meetReply -> {
            log.info(meetReply);
        });
    }
}

