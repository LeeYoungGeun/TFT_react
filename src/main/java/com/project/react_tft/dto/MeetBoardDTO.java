package com.project.react_tft.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetBoardDTO {

    private Long meetId;

    private String meetTitle;

    private String meetWriter;

    private String meetContent;

    private int personnel;

    private LocalDateTime meetTime;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private List<String> fileNames;

    private MultipartFile image;

}
