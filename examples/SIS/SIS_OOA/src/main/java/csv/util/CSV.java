package csv.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

import io.ciera.runtime.api.domain.Domain;

public class CSV {

    private List<String> headers;
    private List<CSVRecord> rows;

    public CSV() {
    }

    public CSV(Domain domain) {
        this();
    }

    public void load_file(final String p_filename) {
        try {
            CSVParser parser = CSVParser.parse(new File(p_filename), Charset.defaultCharset(),
                    CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).build());
            headers = parser.getHeaderNames();
            rows = parser.getRecords();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Set<String> get_headers() {
        return new HashSet<>(headers);
    }

    public String get_cell(final int p_row_index, final String p_column_name) {
        return rows.get(p_row_index).get(p_column_name);
    }

    public int num_rows() {
        return rows.size();
    }

}
