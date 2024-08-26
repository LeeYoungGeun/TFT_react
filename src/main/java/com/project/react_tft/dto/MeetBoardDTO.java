package com.project.react_tft.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetBoardDTO {

    private Long meetId;

    private String meetTitle;

    private String meetWriter;

    private String meetContent;

    private int personnel;

    private LocalDateTime meetTime;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
