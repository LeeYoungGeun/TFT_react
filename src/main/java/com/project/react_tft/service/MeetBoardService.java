package com.project.react_tft.service;

import com.project.react_tft.domain.MeetBoard;
import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.MeetBoardDTO;

public interface MeetBoardService {


    class MeetIdExistException extends Exception {
        public MeetIdExistException() {}
    }

    Long registerMeetBoard(MeetBoardDTO meetBoardDTO);

    void modify(MeetBoardDTO meetBoardDTO);

    MeetBoard getDetail(Long meetId);

    void remove(Long meetId);
}
