package com.project.react_tft.service;

import com.project.react_tft.dto.BoardDTO;
import com.project.react_tft.dto.PageRequestDTO;
import com.project.react_tft.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample test...")
                .content("Sample Content~~")
                .writer("user1")
                .build();
        long bno = boardService.register(boardDTO);

        log.info("bno : "+bno);
    }

    @Test
    public void testModify(){
        //변경에 필요한 데이터만..
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(21L)
                .title("update~~102")
                .content("update content~~102")
                .build();
        boardService.modify(boardDTO);
        // 확인..
        log.info(boardService.readOne(boardDTO.getBno()));
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
    }
}
