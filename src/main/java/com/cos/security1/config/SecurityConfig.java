package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(=SecurityConfig)가 스프링 필터체인에 등록됨
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// secured 어노테이션 활성화 (컨트롤러에서 걸면 그 인증 조건으로들어감)
// preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig {

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http
	            .csrf((auth) -> auth.disable());
		
		
	    http
        		.authorizeHttpRequests((authorize ) -> authorize 
                .requestMatchers("/user/**").authenticated() //인증 필요
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") //인증 뿐만아니라 액세스도 필요
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll()); //다른 요청들
                
        
        http
		        .formLogin((auth) -> auth.loginPage("/loginForm")
		        .loginProcessingUrl("/login") // 해당 주소 호출시 시큐리티가 낚아채서 대신 로그인을 진행
		        .defaultSuccessUrl("/")); // /loginForm 페이지로 와서 로그인하면 뜨는 페이지 설정 (특정페이지를 요청해서 로그인시에는 그 페이지 열어줌)
        
        // 1.코드받기(인증) 2.엑세스 토큰을 받음(권한) 3.사용자 프로필 정보를 가져옴
        // 4-1.그 정보를 토대로 회원가입 자동진행 / 4-2.정보가 부족할 때
        http
  
        .oauth2Login((oauth2) -> oauth2 // OAuth2 로그인 설정시작
        		.loginPage("/loginForm")
        		.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
        				.userService(principalOauth2UserService))); //OAuth 로그인 시 사용자 정보를 가져오는 엔드포인트와 사용자 서비스를 설정


        //구글 로그인이 완료된 다음 후처리가 필요함. Tip! 코드X (엑세스토큰+사용자프로필정보 O)
        

       

		
		
		return http.build();
	}
	
}
