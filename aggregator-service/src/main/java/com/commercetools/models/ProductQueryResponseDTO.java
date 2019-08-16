package com.commercetools.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PoojaShankar
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQueryResponseDTO {

	private String date;
	private Long count;

}
