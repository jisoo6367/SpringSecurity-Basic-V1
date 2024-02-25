package com.cos.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login"); 
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행 -> 그냥 규칙임
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	
	// 시큐리티 session (내부 Authentication (내부 UserDatails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//로그인 하는 폼태그 안 인풋의 name이 username이 아닌 경우에는 못 받아오기 때문에
		//securityConfig 에서 .usernameParameter("username2") 이렇게 바꿔줘야함!
		
		User userEntity = userRepository.findByUsername(username); //이런 함수는 기본 제공이 아니므로 만들어줘야함
		if (userEntity != null ) {
			return new PrincipalDetails(userEntity);
		}
		
		return null;
	}

}
