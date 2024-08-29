package com.project.react_tft.service;

import com.project.react_tft.dto.MeetBoardDTO;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class MeetBoardServiceTests {

    @Autowired
    MeetBoardService meetBoardService;

    @Test
    public void testRegisterWithImages(){
        log.info(meetBoardService.getClass().getName());

        MeetBoardDTO meetBoardDTO = MeetBoardDTO.builder()
                .meetTitle("테스트")
                .meetContent("테스트")
                .meetWriter("테스트")
                .personnel(4)
                .build();

        meetBoardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_ccc.jpg"
                ));

        Long meetId = meetBoardService.registerMeet(meetBoardDTO);

        log.info(meetId);
    }

}
