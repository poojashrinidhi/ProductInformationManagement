package com.commercetools.service.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.commercetools.service.InputFileService;
import com.commercetools.service.ObjectConverterService;

import lombok.extern.slf4j.Slf4j;

/**
 * Reads file from CSV and sends for Kafka Queue
 * 
 * @author PoojaShankar
 */
@Slf4j
public class CsvInputFileServiceImpl implements InputFileService {

	private String csvFileInputPath;
	private ObjectConverterService objectService;

	public CsvInputFileServiceImpl(String csvFileInputPath, ObjectConverterService objectService) {
		this.csvFileInputPath = csvFileInputPath;
		this.objectService = objectService;
	}

	@Override
	public void readFromFile() throws IOException {

		if (StringUtils.isNotBlank(csvFileInputPath)) {
			log.info("Reading CSV file from path : " + csvFileInputPath);
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream(csvFileInputPath)))) {
				br.readLine(); // Skip header
				String line;
				while ((line = br.readLine()) != null) {
					objectService.formatAndSendToKafka(line);
				}
			}
		} else {
			throw new FileNotFoundException("Failed to find the CSV File Input Path for reading the data");
		}
	}
}