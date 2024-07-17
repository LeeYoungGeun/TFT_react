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
        //소셜에 등록된 이메일을 통해서 사용자 구분을 하나 없는 경우 .. 멤버 추가설정을 위해 의존성(DI) 주입
        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;
        private final JWTUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {



        log.info("User Request....."+userRequest);
        log.info(userRequest);

        log.info("oauth2 user....................................");
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("Name : "+clientName); //어떤 소셜을 사용했니?

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String memail = null;

        switch (clientName){
            case "kakao":
                memail = getKakaoEmail(paramMap);
                break;
        }





//        paramMap.forEach((k,v) ->{
//            log.info("---------------------------------------------");
//            log.info(k+":"+v);
//        });

        log.info("===============================================");
        log.info(memail);
        log.info("===============================================");
        return generateDTO(memail, paramMap); //MemberSecurity 로 반환처리
    }

    private MemberSecurityDTO generateDTO(String memail, Map<String, Object> parmas){
        Optional<Member> result = memberRepository.findByMemail(memail);

        //데이터 베이스에 해당 이메일 사용자가 없는 경우...
        if(result.isEmpty()){
            //회원 추가... mid는 이메일 주소 / 패스워드 1111
            Member member = Member.builder()
                    .mid(memail)
                    .mpw(passwordEncoder.encode("1111"))
                    .memail(memail)
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            // MemberSecurityDTO 로 반환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(memail,"1111",memail,false,true,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(parmas);
        return memberSecurityDTO;
        }else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getMemail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet().stream().map(memberRole ->
                                    new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                            .collect(Collectors.toList())
            );
            return memberSecurityDTO;
        }
    }

    //getKakaoEmail() 만들기... : KAKAO에서 전달된 정보를 통해서 Email 반환 처리
    private String getKakaoEmail(Map<String, Object> paramMap){
        log.info("Kakao ....................................");
        Object value = paramMap.get("kakao_account");
        log.info("----------------------"+value);

        LinkedHashMap accountMap = (LinkedHashMap)value;
        String email = (String)accountMap.get("email");
        log.info("Email................... : "+email);
        return email;
    }
}
