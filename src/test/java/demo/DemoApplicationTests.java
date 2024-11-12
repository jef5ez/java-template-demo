package demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import review.ReviewModel;

import java.io.*;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Test
	void contextLoads() {
		ResponseEntity<String> value = restTemplate.getForEntity("/", String.class);
		assertThat(value.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(value.getBody()).contains("mystérieux visiteur");
	}

//	@Test
//	void loadCSV() throws IOException {
//		FileInputStream csv = new FileInputStream("/tmp/selfAssessment.csv");
//		Reader reader = new InputStreamReader(csv, "Cp1252");
//		CSVFormat format = CSVFormat.DEFAULT.builder().setAllowMissingColumnNames(true).setHeader().setSkipHeaderRecord(true).build();
//		AtomicReference<ReviewModel> currentReviews = new AtomicReference<>(null);
//
//		try (CSVParser csvParser = new CSVParser(reader, format)) {
//			List<String> headerNames = csvParser.getHeaderNames();
////			System.out.println("header: " + String.join(",", headerNames));
//			List<CSVRecord> records = csvParser.getRecords();
////			System.out.println("first: " + String.join(",", records.get(0).toList()));
//			currentReviews.set(new ReviewModel(Instant.now(), headerNames, records, 0));
//		}
//	}
}
