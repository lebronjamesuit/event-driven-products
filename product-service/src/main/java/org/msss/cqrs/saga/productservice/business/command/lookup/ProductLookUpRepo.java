package org.msss.cqrs.saga.productservice.business.command.lookup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductLookUpRepo extends JpaRepository<ProductLookUpEntity, String> {

    Optional<ProductLookUpEntity> findByProductIdOrTitle(String productId, String title);

}
