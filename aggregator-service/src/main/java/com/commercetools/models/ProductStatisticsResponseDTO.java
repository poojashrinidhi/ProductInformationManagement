package com.commercetools.models;

import java.util.Map;

import lombok.Data;

/**
 * Response class for Product Statistics
 * 
 * @author PoojaShankar
 */
@Data
public class ProductStatisticsResponseDTO {

	private Map<String, Statistics> productStatistics;

}
