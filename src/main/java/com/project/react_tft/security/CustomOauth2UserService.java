package com.project.react_tft.security;

import com.project.react_tft.Repository.MemberRepository;
import com.project.react_tft.domain.Member;
import com.project.react_tft.domain.MemberRole;
import com.project.react_tft.dto.MemberSecurityDTO;
import com.project.react_tft.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("User Request....." + userRequest);
        log.info(userRequest);

        log.info("oauth2 user....................................");
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("Name : " + clientName); // 어떤 소셜을 사용했는지 확인

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String memail = null;
        String mname = null; // 사용자 이름 필드
        String mnick = null; // 닉네임 필드

        switch (clientName) {
            case "kakao":
                memail = getKakaoEmail(paramMap);
                mname = getKakaoFullName(paramMap); // 사용자 이름을 가져오는 메서드
                mnick = getKakaoNickName(paramMap); // 닉네임을 가져오는 메서드
                break;
        }

        log.info("===============================================");
        log.info("Email: " + memail);
        log.info("Name: " + mname);
        log.info("Nick: " + mnick);
        log.info("===============================================");
        return generateDTO(memail, mname, mnick, paramMap); // 이름, 이메일, 닉네임을 전달하여 MemberSecurityDTO 로 반환 처리
    }

    private MemberSecurityDTO generateDTO(String memail, String mname, String mnick, Map<String, Object> parmas) {
        Optional<Member> result = memberRepository.findByMemail(memail);

        // 데이터베이스에 해당 이메일 사용자가 없는 경우
        if (result.isEmpty()) {
            Member member = Member.builder()
                    .mid(mname)  // 사용자 이름을 mid로 사용
                    .mname(mname)
                    .mpw(passwordEncoder.encode("1111"))
                    .memail(memail)
                    .mnick(mnick) // 닉네임 설정
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            // MemberSecurityDTO 로 반환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    mname,  // 사용자 이름을 사용하여 생성된 mid
                    "1111",
                    mnick,
                    memail,
                    false,
                    true,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            memberSecurityDTO.setProps(parmas);
            return memberSecurityDTO;
        } else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(),  // 데이터베이스에 저장된 사용자 이름을 가져옴
                    member.getMpw(),
                    member.getMnick(), // 데이터베이스에서 닉네임 가져오기
                    member.getMemail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet().stream()
                            .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                            .collect(Collectors.toList())
            );
            return memberSecurityDTO;
        }
    }

    // getKakaoEmail() 만들기... : KAKAO에서 전달된 정보를 통해서 Email 반환 처리
    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("Kakao ....................................");
        Object value = paramMap.get("kakao_account");
        log.info("----------------------" + value);

        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String) accountMap.get("email");
        log.info("Email................... : " + email);
        return email;
    }

    // getKakaoFullName() 만들기... : KAKAO에서 전달된 정보를 통해서 전체 이름 반환 처리
    private String getKakaoFullName(Map<String, Object> paramMap) {
        Object value = paramMap.get("kakao_account");
        if (value instanceof LinkedHashMap) {
            LinkedHashMap accountMap = (LinkedHashMap) value;
            LinkedHashMap profileMap = (LinkedHashMap) accountMap.get("profile");
            String fullName = (String) profileMap.get("nickname");  // 카카오에서 제공하는 닉네임을 전체 이름 필드로 사용
            log.info("Full Name................... : " + fullName);
            return fullName;
        }
        return null; // 전체 이름이 없을 경우 null 반환
    }

    // getKakaoNickName() 만들기... : KAKAO에서 전달된 정보를 통해서 닉네임 반환 처리
    private String getKakaoNickName(Map<String, Object> paramMap) {
        Object value = paramMap.get("kakao_account");
        if (value instanceof LinkedHashMap) {
            LinkedHashMap accountMap = (LinkedHashMap) value;
            LinkedHashMap profileMap = (LinkedHashMap) accountMap.get("profile");
            String nickName = (String) profileMap.get("nickname");  // 카카오에서 제공하는 닉네임 필드
            log.info("Nick Name................... : " + nickName);
            return nickName;
        }
        return null; // 닉네임이 없을 경우 null 반환
    }
}
