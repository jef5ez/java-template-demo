package demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.RedirectView;
import review.ReviewModel;
import templates.Demo;
import templates.Review;
import templates.Single;
import view.StringView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Controller
public class DemoController {

	@Autowired
	VisitsRepository visitsRepository;

	AtomicReference<ReviewModel> currentReviews = new AtomicReference<>(null);

	@Value("${spring.profiles.active:PROFILE_NOT_SET}")
	private String profile;

	@ResponseBody
	@GetMapping(value = "/profile")
	public String profile() {
		return profile;
	}

	@PostMapping("/upload-csv")
	public RedirectView uploadCsv(@RequestParam("csv") MultipartFile csv,
								  @RequestParam(value = "mainCol", defaultValue = "0") int mainCol,
								  @RequestParam(value = "charset", defaultValue = "Cp1252") String charset //windows ansi
	) throws IOException {
		Reader reader = new InputStreamReader(csv.getInputStream(), charset);
		CSVFormat format = CSVFormat.EXCEL.builder().setHeader().setSkipHeaderRecord(true).build();
		try (CSVParser csvParser = new CSVParser(reader, format)) {
			List<String> headerNames = csvParser.getHeaderNames();
//			System.out.println("header: " + String.join(",", headerNames));
			List<CSVRecord> records = csvParser.getRecords();
//			System.out.println("first: " + String.join(",", records.get(0).toList()));
			currentReviews.set(new ReviewModel(Instant.now(), headerNames, records, mainCol));
		}
		return new RedirectView("/reviews");
	}

	@GetMapping("/reviews")
	public View view() {
		return new StringView(() -> Review.render(currentReviews.get()));
	}

	@GetMapping("/reviews/{id}")
	public View viewSingle(@PathVariable("id") int id) throws Exception {
		ReviewModel reviews = currentReviews.get();
		if (reviews == null) {
			throw new Exception("404");
		}
		return new StringView(() -> Single.render(reviews.records.get(id), reviews.header, id, reviews.records.size()));
	}

	@GetMapping("/")
	public View viewDemo() {
		visitsRepository.add();
		return new StringView(() -> Demo.render(new DemoModel("mystérieux visiteur", visitsRepository.get())));
	}
}
