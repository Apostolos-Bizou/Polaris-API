package com.polaris.api.repository;

import com.polaris.api.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<Offer> findByOfferId(String offerId);

    List<Offer> findByClientIdOrderByCreatedDateDesc(String clientId);

    List<Offer> findByStatusOrderByCreatedDateDesc(String status);

    List<Offer> findByOfferTypeOrderByCreatedDateDesc(String offerType);

    @Query("SELECT o FROM Offer o ORDER BY o.createdDate DESC")
    List<Offer> findAllOrderByCreatedDateDesc();

    @Query("SELECT o FROM Offer o WHERE o.status = :status AND o.offerType = :type ORDER BY o.createdDate DESC")
    List<Offer> findByStatusAndType(@Param("status") String status, @Param("type") String type);

    @Query("SELECT o FROM Offer o WHERE LOWER(o.clientName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY o.createdDate DESC")
    List<Offer> searchByClientName(@Param("name") String name);

    @Query("SELECT COUNT(o) FROM Offer o WHERE o.status = :status")
    long countByStatus(@Param("status") String status);

    @Query("SELECT COALESCE(SUM(o.grandTotalUsd), 0) FROM Offer o WHERE o.status IN ('accepted', 'signed', 'contract')")
    double totalRevenuePipeline();

    @Query("SELECT COALESCE(SUM(o.grandTotalUsd), 0) FROM Offer o WHERE o.status = 'draft' OR o.status = 'sent'")
    double totalPendingValue();
}
