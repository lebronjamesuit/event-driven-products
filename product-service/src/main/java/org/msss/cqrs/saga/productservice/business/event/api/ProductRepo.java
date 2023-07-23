package org.msss.cqrs.saga.productservice.business.event.api;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<ProductEntity, String> {

    ProductEntity findProductEntitiesByProductIdOrTitle(String id, String title);

}

