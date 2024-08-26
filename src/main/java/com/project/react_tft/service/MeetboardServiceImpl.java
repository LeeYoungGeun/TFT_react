package com.project.react_tft.service;

import com.project.react_tft.Repository.MeetBoardRepository;
import com.project.react_tft.domain.Board;
import com.project.react_tft.domain.MeetBoard;
import com.project.react_tft.domain.Member;
import com.project.react_tft.dto.MeetBoardDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class MeetboardServiceImpl implements MeetBoardService {

    private final MeetBoardRepository meetBoardRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long registerMeetBoard(MeetBoardDTO meetBoardDTO) {
        MeetBoard meetBoard = modelMapper.map(meetBoardDTO, MeetBoard.class);

        Long meetId = meetBoardRepository.save(meetBoard).getMeetId();
        return meetId;
    }

    @Override
    public void modify(MeetBoardDTO meetBoardDTO) {
        Optional<MeetBoard> result = meetBoardRepository.findById(meetBoardDTO.getMeetId());
        if (result.isPresent()) {
            MeetBoard meetBoard = result.get();
            modelMapper.map(meetBoardDTO, meetBoard);
            meetBoardRepository.save(meetBoard);
            log.info("meet 수정완료");
        } else {
            log.info("게시물이 없는데요?");
            throw new RuntimeException("게시글이 없는데요?");
        }
    }

    @Override
    public MeetBoard getDetail(Long meetId) {
        Optional<MeetBoard> result = meetBoardRepository.findById(meetId);
        log.info("게시글을를 불러오겠음");
        if (result.isPresent()) {
            MeetBoard meetBoard = result.get();
            log.info("상세 개시물을 불러왔음");
            log.info(meetId);
            return meetBoard;
        } else {
            throw new NoSuchElementException("비어있음...................... " + meetId);
        }
    }

    @Override
    public void remove(Long meetId) {
        meetBoardRepository.deleteById(meetId);
    }
}



