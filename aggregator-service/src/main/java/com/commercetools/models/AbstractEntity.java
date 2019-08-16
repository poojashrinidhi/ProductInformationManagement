package com.commercetools.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Base class for all entities
 * 
 * @author PoojaShankar
 */

@MappedSuperclass
@Data
public abstract class AbstractEntity {

	@Column(name = "createdTimeStamp", unique = false, nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdTimeStamp;

	@Column(name = "lastModifiedTimeStamp", unique = false, nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date lastModifiedTimeStamp;

	protected AbstractEntity() {
		createdTimeStamp = lastModifiedTimeStamp = new Date();
	}
}
