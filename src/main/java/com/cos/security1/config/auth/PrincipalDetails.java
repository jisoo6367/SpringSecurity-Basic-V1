package com.cos.security1.config.auth;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

// 시큐리가 /login 주소 요총이 오면 낚아채서 로그인을 진행하는데
// 완료가 되면 시큐리티 session을 만들어 줌 (Security ContextHolder라는 키값에 세션 정보를 저장)
// 세션 정보는 Authentication 타입의 객체여야함
// Authentication 안에는 User정보가 있어야 함
// User 오브젝트 타입 => UserDetails 타입 객체여야함

// Security Session => Authentication => UserDetails (PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user; // 콤포지션 (객체를 품고 있는 것)
	private Map<String, Object> attributes;
	
	//일반 로그인시 사용하는 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	// OAuth 로그인시 사용하는 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	//해당 유저의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// user.getRole(); -> 뜻은 이렇게하면 되지만,이건 리턴타입이 String 타입이라 이렇게만 할 수 없다!
		
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
		collect.add(new GrantedAuthority() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				
				return user.getRole();
			}
		});
		return collect;
	}

	
	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getUsername();
	}

	// 계정 만료 여부 (ture : 아니요)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 잠겼니
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 계정 비밀번호가 1년 지났니 (true : 아니요)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정 활성화 되어있니 (true : 아니요)
	@Override
	public boolean isEnabled() {
		
		// 1년동안 회원로그인을 안할시 휴면 계정 처리 하기로 했다면
		// user.getLoginDate(); (이건 User model에 추가하고)
		// 현재시간- 로그인시간 => 1년 초과하면 return false로 하면 됨
		
		return true;
	}

	// OAuth2User도 추가로 implements하고 오버라이드해서 생기는 메서드 2개 
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

	
}
