package com.java.securecoding.repository;

import com.java.securecoding.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {


    boolean existsByUsername(String username);

    Member findByUsername(String username);

//    Optional<Member> findByUsername(String username);
}
