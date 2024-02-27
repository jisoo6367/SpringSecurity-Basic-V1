package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	// 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); //registrationId로 어떤 Oauth로그인인지 알 수 있음
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
		//구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴(Oauth-Client라이브러리) -> Access Token 요청
		//userRepuest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받아옴
		System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
		//getAttributes : {sub=113341760605204311861, name=홍지수, given_name=지수, family_name=홍, picture=https://lh3.googleusercontent.com/a/ACg8ocKiSpCZIJTJlb37vzWuanMgGixX8dq01r7dTkL9N3Qk=s96-c, email=jisunalazzang@gmail.com, email_verified=true, locale=ko}

		OAuth2User oAuth2User = super.loadUser(userRequest);
		//회원가입을 강제로 진행
		return super.loadUser(userRequest);
	}
}
