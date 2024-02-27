package com.cos.security1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(//DI (의존성 주입)
    		Authentication authentication, 
    		@AuthenticationPrincipal PrincipalDetails userDetails) {
    							//PrincipalDetails.java 에서 implements UserDetails했기 때문에 UserDetails 타입말고 PrincipalDetails로 해도 되는거임
    	System.out.println("/test/login============");
    	System.out.println("authentication: "+ authentication); //리턴타입: object
    	System.out.println("authentication.getPrincipal(): "+ authentication.getPrincipal());
    	
    	PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal(); //down casting
    	System.out.println("getUser: "+ principalDetails.getUser());
    	
    	System.out.println("userDetails: "+ userDetails.getUser());
    	
    	return "세션 정보 확인하기";
    }
    
    
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(//DI (의존성 주입)
    		Authentication authentication) {
    	
    	System.out.println("/test/oauth/login============");
    	OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
    	System.out.println("authentication: "+ oauth2User.getAttributes());
    	
    	return "OAuth 세션 정보 확인하기";
    }
    
	@GetMapping({"","/"})
	public String index() {
		// 머스테치 기본폴더 src/main/resources/
		// 뷰리졸버 설정: templates를 prefix로, .mustache를 suffix로 (생략가능)
		return "index";
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	//스프링 시큐리티가 해당 주소를 먼저 낚아챔 -> SecurityConfig 파일 생성 후에는 안 낚아챔
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		user.setRole("ROLE_USER");
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save(user); //얘만하면 회원가입은 되지만 비밀번호가 암호화되지않아 시큐리티로 로그인 할 수 없기 때문에 비크립트 먼저하기
		return "redirect:/loginForm";
	}
	
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}

	@PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')") // 메서드 실행 직전 실행됨, "ROLE_ADMIN"이렇게 적는거 아님!
	//하나 할거면 @Secured 쓰고 여러개 할거면 @PreAuthorize가 낫다
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보";
	}

	
}
