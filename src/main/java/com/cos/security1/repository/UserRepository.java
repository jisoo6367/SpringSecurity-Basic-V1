package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// CRUD 함수를 JpaRepository가 갖고 있음
// @Repository 라는 어노테이션이 없어도 IoC된다. ->JpaRepository를 상속했기 때문
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// findBy 규칙 + Username 문법 (select * from user where username = (메서드의 파라미터)? )
	public User findByUsername(String username); //Jpa Query Methods
	
	// select * from user where email = ?
	// public User fidByEmail(); 

}
