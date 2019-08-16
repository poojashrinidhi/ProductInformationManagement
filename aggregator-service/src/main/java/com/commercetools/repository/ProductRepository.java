package com.commercetools.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.TemporalType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.commercetools.models.Product;
import com.commercetools.models.ProductQueryResponseDTO;

/**
 * @author PoojaShankar
 */

@Transactional
@Repository
public interface ProductRepository extends CrudRepository<Product, UUID> {

	List<Product> findAllByCreatedTimeStamp(@Temporal(TemporalType.DATE) Date Date);

	List<Product> findAllByLastModifiedTimeStamp(@Temporal(TemporalType.DATE) Date Date);

	@Query("SELECT new com.commercetools.models.ProductQueryResponseDTO(to_char(createdTimeStamp, 'yyyy-mm-dd') as date , COUNT(*) as count) FROM Product GROUP BY to_char(createdTimeStamp, 'yyyy-mm-dd')")
	List<ProductQueryResponseDTO> getTotalCountOfProductsCreatedPerDayBasis();

	@Query("SELECT new com.commercetools.models.ProductQueryResponseDTO(to_char(lastModifiedTimeStamp, 'yyyy-mm-dd') as date , COUNT(*) as count) FROM Product WHERE createdTimeStamp <> lastModifiedTimeStamp GROUP BY to_char(lastModifiedTimeStamp, 'yyyy-mm-dd')")
	List<ProductQueryResponseDTO> getTotalCountOfProductsModifiedPerDayBasis();

	Product findByName(String name);
}