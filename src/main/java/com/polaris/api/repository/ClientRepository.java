package com.polaris.api.repository;

import com.polaris.api.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId);
    List<Client> findByClientType(String clientType);
    List<Client> findByParentId(String parentId);
    List<Client> findByStatus(String status);

    @Query("SELECT c FROM Client c WHERE c.clientType = 'parent' ORDER BY c.name")
    List<Client> findAllParentCompanies();

    @Query("SELECT c FROM Client c ORDER BY c.name")
    List<Client> findAllOrderByName();
}
