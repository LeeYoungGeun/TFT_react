package com.project.react_tft.controller;

import com.project.react_tft.domain.MeetBoard;
import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.BoardDTO;
import com.project.react_tft.dto.MeetBoardDTO;
import com.project.react_tft.dto.PageRequestDTO;
import com.project.react_tft.dto.PageResponseDTO;
import com.project.react_tft.service.MeetBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/meet")
public class MeetBoardController {

    private final MeetBoardService meetBoardService;


    @GetMapping(value = "/list")
    public ResponseEntity<PageResponseDTO<MeetBoardDTO>> getList(PageRequestDTO pageRequestDTO) {
        log.info("리스트에 접근했음");
        PageResponseDTO<MeetBoardDTO> responseDTO = meetBoardService.list(pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/register")
    public ResponseEntity<?> getRegister() {
        log.info("meet board register 접근완료 ");

        return ResponseEntity.status(200).body("접근완료");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute MeetBoardDTO meetBoardDTO){
        meetBoardService.registerMeetBoard(meetBoardDTO);
        return ResponseEntity.status(200).body("작성완료");
    }


    @GetMapping("/modify/{meetNum}")
    public ResponseEntity<?> getModify(@PathVariable Long meetNum){

        MeetBoardDTO meetBoardDTO = meetBoardService.getDetail(meetNum);

        return ResponseEntity.ok(meetBoardDTO);
    }


    @PutMapping("/modify/{meetNum}")
    public ResponseEntity<?> modify(@RequestBody MeetBoardDTO meetBoardDTO){

        meetBoardService.modify(meetBoardDTO);

        return ResponseEntity.ok(meetBoardDTO);
    }

    @GetMapping("/read/{meetNum}")
    public ResponseEntity<?> read(@PathVariable Long meetNum){

        MeetBoardDTO meetBoardDTO = meetBoardService.getDetail(meetNum);


        return ResponseEntity.ok(meetBoardDTO);

    }

    @DeleteMapping("/delete/{meetNum}")
    public ResponseEntity<?> delete(@PathVariable Long meetNum){

        meetBoardService.remove(meetNum);

        return ResponseEntity.ok("삭제완료");
    }


}
