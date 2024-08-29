package com.project.react_tft.service;


import com.project.react_tft.dto.MeetReplyDTO;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class MeetReplyServiceTests {

    @Autowired
    private MeetReplyService meetReplyService;

    @Test
    public void testRegister(){

        MeetReplyDTO meetReplyDTO = MeetReplyDTO.builder()
                .replyText("테스트 댓글")
                .replyer("테스트 유져")
                .meetId(1L)
                .build();
        log.info(meetReplyService.register(meetReplyDTO));
    }
}
