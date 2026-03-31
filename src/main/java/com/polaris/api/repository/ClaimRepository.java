package com.polaris.api.repository;

import com.polaris.api.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByClientId(String clientId);
    List<Claim> findByStatus(String status);
    List<Claim> findByClaimType(String claimType);

    @Query("SELECT c.category, COUNT(c), SUM(c.amountUsd) FROM Claim c WHERE c.clientId = :clientId GROUP BY c.category")
    List<Object[]> getCategoriesBreakdown(@Param("clientId") String clientId);

    @Query("SELECT c.hospital, COUNT(c), SUM(c.amountUsd) FROM Claim c GROUP BY c.hospital ORDER BY SUM(c.amountUsd) DESC")
    List<Object[]> getTopHospitals();

    @Query("SELECT c.hospitalCountry, COUNT(c), SUM(c.amountUsd) FROM Claim c GROUP BY c.hospitalCountry")
    List<Object[]> getGeographicDistribution();

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.claimType = :type")
    Long countByClaimType(@Param("type") String type);

    @Query("SELECT COALESCE(SUM(c.amountUsd), 0) FROM Claim c WHERE c.claimType = :type")
    Double sumCostByClaimType(@Param("type") String type);

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.memberType = :type")
    Long countByMemberType(@Param("type") String type);

    @Query("SELECT COALESCE(SUM(c.amountUsd), 0) FROM Claim c")
    double totalClaimsCost();

    @Query("SELECT COALESCE(AVG(c.amountUsd), 0) FROM Claim c")
    double averageClaimCost();
}