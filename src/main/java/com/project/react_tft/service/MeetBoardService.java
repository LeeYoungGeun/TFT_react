package com.project.react_tft.service;

import com.project.react_tft.Repository.search.BoardSearch;
import com.project.react_tft.domain.MeetBoard;
import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface MeetBoardService {


    class MeetIdExistException extends Exception{
        public MeetIdExistException() {}
    }

    Long registerMeet(MeetBoardDTO meetBoardDTO);

    void modify(MeetBoardDTO meetBoardDTO);

    MeetBoardDTO getDetail(Long meetId);

    PageResponseDTO<MeetBoardDTO> list(PageRequestDTO pageRequestDTO);

    void remove(Long meetId);

    //댓글 숫자처리
    PageResponseDTO<MeetBoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    PageResponseDTO<MeetBoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    default MeetBoard dtoToEntity(MeetBoardDTO meetBoardDTO) {
        MeetBoard meetBoard = MeetBoard.builder()
                .meetId(meetBoardDTO.getMeetId())
                .meetTitle(meetBoardDTO.getMeetTitle())
                .meetContent(meetBoardDTO.getMeetContent())
                .meetWriter(meetBoardDTO.getMeetWriter())
                .meetTime(meetBoardDTO.getMeetTime())
                .personnel(meetBoardDTO.getPersonnel())
                .build();

        if (meetBoardDTO.getFileNames() != null) {
            Long ord = 0L;
            for (String fileName : meetBoardDTO.getFileNames()) {
                String[] arr = fileName.split("_");

                // 배열 길이 확인 및 uuid 설정
                if (arr.length == 2) {
                    meetBoard.addImage(arr[0], arr[1]);
                } else if (arr.length == 1) {
                    meetBoard.addImage(UUID.randomUUID().toString(), arr[0]); // UUID를 String으로 변환
                } else {
                    // 로그 또는 예외 처리
                    System.err.println("Unexpected file name format: " + fileName);
                }
                ord++;
            }
        }
        return meetBoard;
    }




    default MeetBoardDTO entityToDTO(MeetBoard meetBoard) {

        MeetBoardDTO meetBoardDTO = MeetBoardDTO.builder()
                .meetId(meetBoard.getMeetId())
                .meetTitle(meetBoard.getMeetTitle())
                .meetContent(meetBoard.getMeetContent())
                .meetWriter(meetBoard.getMeetWriter())
                .meetTime(meetBoard.getMeetTime())
                .personnel(meetBoard.getPersonnel())
                .regDate(meetBoard.getRegDate())
                .modDate(meetBoard.getModDate())
                .build();

        List<String> fileNames =
                meetBoard.getImageSet().stream().sorted().map(boardImage ->
                        boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());

        meetBoardDTO.setFileNames(fileNames);

        return meetBoardDTO;
    }


}
