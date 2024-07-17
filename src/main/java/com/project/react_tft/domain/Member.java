package com.project.react_tft.domain;

import com.project.react_tft.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "roleSet")
@Table(name="Member")
public class Member extends BaseEntity {

    @Id
    private String mid;

    private String mname;
    private String mnick;
    private String mpw;
    private String memail;
    private String mphone;
    private boolean del;
    private boolean social;


    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public Member(MemberDTO dto) {
        this.mid = dto.getMid();
        this.mpw = dto.getMpw();
        this.mname = dto.getMname();
        this.mnick = dto.getMnick();
        this.memail = dto.getMemail();
        this.mphone = dto.getMphone();
    }

    //addRoll 역할 추가
    public void addRole(MemberRole role) {
        this.roleSet.add(role);
    }

}
