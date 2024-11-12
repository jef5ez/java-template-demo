package review;

import org.apache.commons.csv.CSVRecord;

import java.time.Instant;
import java.util.List;

public class ReviewModel {

    public Instant createdOn;

    public List<String> header;

    public List<CSVRecord> records;

    public int mainCol;

    public ReviewModel(Instant createdOn, List<String> header, List<CSVRecord> records, int mainCol) {
        this.createdOn = createdOn;
        this.header = header;
        this.records = records;
        this.mainCol = mainCol;
    }

    public ReviewModel(Instant createdOn) {
        this.createdOn = createdOn;
    }
}
