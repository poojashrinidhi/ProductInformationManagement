package com.commercetools.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.commercetools.service.InputFileService;
import com.commercetools.service.ObjectConverterService;
import com.commercetools.service.impl.CsvInputFileServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Rest endpoint for importing resources
 * 
 * @author PoojaShankar
 */

@Slf4j
@RestController
@RequestMapping("/import")
public class ImportResourceController {

	@Value("${product.csv.file.input.path}")
	private String productCsvFileInputPath;

	@Qualifier("productService")
	@Autowired
	private ObjectConverterService objectService;

	private InputFileService myService;

	@RequestMapping(value = "/productCsv", method = RequestMethod.GET)
	public ResponseEntity<String> importFromCSV() {
		myService = new CsvInputFileServiceImpl(productCsvFileInputPath, objectService);
		String response;
		try {
			myService.readFromFile();
			response = "Successfully Imported Product CSV file and sent to Kafka Queue";
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (IOException e) {
			response = "Failed to import Product CSV file and send to Kafka Queue";
			if (e instanceof FileNotFoundException) {
				log.error(e.getMessage(), e);
				return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
			}
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = "Failed to import Product CSV file and send to Kafka Queue";
			return new ResponseEntity<String>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}