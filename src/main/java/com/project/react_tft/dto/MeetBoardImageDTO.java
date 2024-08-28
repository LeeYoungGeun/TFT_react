package com.project.react_tft.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetBoardImageDTO {
    private String uuid;
    private String fileName;
    private int ord;
    @Lob
    private byte[] data; // 이미지 데이터를 저장할 필드
}
