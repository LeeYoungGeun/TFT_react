package com.project.react_tft.service;

import com.project.react_tft.Repository.search.BoardSearch;
import com.project.react_tft.domain.MeetBoard;
import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface MeetBoardService {


    class MeetIdExistException extends Exception{
        public MeetIdExistException() {}
    }

    Long registerMeetBoard(MeetBoardDTO meetBoardDTO);

    void modify(MeetBoardDTO meetBoardDTO);

    MeetBoardDTO getDetail(Long meetId);

    PageResponseDTO<MeetBoardDTO> list(PageRequestDTO pageRequestDTO);

    void remove(Long meetId);

    default MeetBoard dtoToEntity(MeetBoardDTO meetBoardDTO){

        MeetBoard meetBoard = MeetBoard.builder()
                .meetId(meetBoardDTO.getMeetId())
                .meetTitle(meetBoardDTO.getMeetTitle())
                .meetContent(meetBoardDTO.getMeetContent())
                .meetWriter(meetBoardDTO.getMeetWriter())
                .meetTime(meetBoardDTO.getMeetTime())

                .build();

        if(meetBoardDTO.getFileNames() != null){
            meetBoardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                meetBoard.addImage(arr[0], arr[1]);
            });
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
