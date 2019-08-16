package com.commercetools.service;

/**
 * PlaceHolder Interface to fit Different object formats converter service
 * 
 * @author PoojaShankar
 */
public interface ObjectConverterService {

	void formatAndSendToKafka(String line);

}
