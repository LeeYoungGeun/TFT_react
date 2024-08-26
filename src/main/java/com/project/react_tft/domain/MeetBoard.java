package com.project.react_tft.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MeetBoard extends BaseEntity{


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long meetId;

   private String meetTitle;

   private String meetWriter;

   private String meetContent;

   private int personnel;

   private LocalDateTime meetTime;


}
