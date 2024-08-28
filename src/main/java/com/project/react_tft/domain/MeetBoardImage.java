package com.project.react_tft.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "meetBoard")
public class MeetBoardImage implements Comparable<MeetBoardImage> {
    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private MeetBoard meetBoard;

    @Override
    public int compareTo(MeetBoardImage other) {
            return this.ord - other.ord;
    }

    public void changeBoard(MeetBoard meetBoard) {
            this.meetBoard = meetBoard;
    }
}


