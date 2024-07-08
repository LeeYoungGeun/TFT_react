package com.project.react_tft.service;

import com.project.react_tft.Repository.MemberRepository;
import com.project.react_tft.domain.Member;
import com.project.react_tft.domain.MemberRole;
import com.project.react_tft.dto.MemberDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Member signUp(MemberDTO dto) throws MemberMidExistException{
        String mpw = dto.getMpw();
        String chekcmpw = dto.getCheckMpw();
        if(memberRepository.existsById(dto.getMid())){
            log.info("이미 있는 아이디인데요.");
            throw new MemberMidExistException();
        }
        if (memberRepository.existsByMemail(dto.getMemail())){
            log.info("이미 있는 이메일인데요");
            throw new MemberMidExistException();
        }

        if(!mpw.equals(chekcmpw)) {
            log.info("비밀번호가 노일치");
            return null;
        }

        Member member = modelMapper.map(dto, Member.class);

        member.setMpw(passwordEncoder.encode(dto.getMpw()));
        member.addRole(MemberRole.USER);

        memberRepository.save(member);

        return member;
    }



    @Override
    public Member login(String mid, String mpw) {
        Optional<Member> member = memberRepository.findById(mid);

        if (member.isPresent()) {
            if (passwordEncoder.matches(mpw, member.get().getMpw())) {
                return member.get();
            } else {
                log.info("비밀번호가 틀렸는데요.");
                return null;
            }
        } else {
            log.info("아이디가 없는데요.");
            return null;
        }
    }
}
