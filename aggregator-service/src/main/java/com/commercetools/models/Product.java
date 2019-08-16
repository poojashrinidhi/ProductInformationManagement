package com.commercetools.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PoojaShankar
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product extends AbstractEntity {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "id", unique = true, nullable = false, updatable = false)
	private UUID uuid;

	private String name;
	private String description;
	private String provider;
	private boolean available;
	private String measurementUnits;

}
