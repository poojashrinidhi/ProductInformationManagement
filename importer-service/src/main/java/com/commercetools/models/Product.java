package com.commercetools.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PoojaShankar
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	private UUID uuid;
	private String name;
	private String description;
	private String provider;
	private boolean available;
	private String measurementUnits;
}