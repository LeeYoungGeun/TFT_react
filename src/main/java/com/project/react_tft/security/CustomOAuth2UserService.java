package com.project.react_tft.security;

import com.project.react_tft.Repository.MemberRepository;
import com.project.react_tft.domain.Member;
import com.project.react_tft.domain.MemberRole;
import com.project.react_tft.dto.MemberSecurityDTO;
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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("유저 리퀘스트: {}", userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("Client Name: {}", clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String memail = null;

        if ("kakao".equals(clientName)) {
            memail = getKakaoEmail(attributes);
        }

        log.info("사용자 이메일: {}", memail);

        return generateDTO(memail, attributes);
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> attributes) {
        Optional<Member> result = memberRepository.findByMemail(email);

        if (result.isEmpty()) {
            log.info("새로운 소셜 사용자 생성: {}", email);
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .memail(email)
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    email,
                    "1111",
                    email,
                    false,
                    true,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            memberSecurityDTO.setProps(attributes);

            return memberSecurityDTO;
        } else {
            log.info("기존 소셜 사용자 로드: {}", email);
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getMemail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toList())
            );
            memberSecurityDTO.setProps(attributes);

            return memberSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String, Object> attributes) {
        log.info("KAKAO 계정 정보: {}", attributes);
        Object value = attributes.get("kakao_account");

        if (value instanceof LinkedHashMap) {
            LinkedHashMap accountMap = (LinkedHashMap) value;
            return (String) accountMap.get("email");
        }

        return null;
    }
}
