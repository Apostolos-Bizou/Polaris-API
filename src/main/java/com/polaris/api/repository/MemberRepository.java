package com.polaris.api.repository;

import com.polaris.api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByClientId(String clientId);
    List<Member> findByStatus(String status);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.clientId = :clientId AND m.status = 'active'")
    Long countActiveByClientId(@Param("clientId") String clientId);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.memberType = :type AND m.status = 'active'")
    Long countActiveByMemberType(@Param("type") String type);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.status = 'active'")
    Long countAllActive();

    @Query("SELECT m.plan, COUNT(m) FROM Member m WHERE m.status = 'active' GROUP BY m.plan")
    List<Object[]> countByPlan();
}
