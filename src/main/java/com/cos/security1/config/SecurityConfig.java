package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터(=SecurityConfig)가 스프링 필터체인에 등록됨
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http
	            .csrf((auth) -> auth.disable());
		
		
	    http
        .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/user/**").authenticated() //인증 필요
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") //인증 뿐만아니라 액세스도 필요
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll()); //다른 요청들
                
        
        http
        .formLogin((auth) -> auth.loginPage("/loginForm")); 
        
		
		
		return http.build();
	}
	
}
