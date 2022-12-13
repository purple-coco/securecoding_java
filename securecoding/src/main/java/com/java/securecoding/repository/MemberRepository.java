package com.java.securecoding.repository;

import com.java.securecoding.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    boolean existsByUsername(String username);

    Member findByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.count = m.count + 1 WHERE m.username = :username")
    int updateCountFailure(@Param("username") String username);

}
