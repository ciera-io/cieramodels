package csv.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.ciera.runtime.api.domain.Domain;

public class CSV {

    private List<String> headers;
    private List<Map<String, String>> rows;

    public CSV() {
    }

    public CSV(Domain domain) {
        this();
    }

    public void load_file(final String p_filename) {
        headers = null;
        rows = null;
        try {
            Files.lines(Path.of(p_filename)).forEach(line -> {
                List<String> row = List.of(line.split(","));
                if (headers == null) {
                    headers = Collections.unmodifiableList(row);
                } else {
                    if (rows == null) {
                        rows = new ArrayList<>();
                    }
                    rows.add(IntStream.range(0, Math.min(headers.size(), row.size())).boxed()
                            .collect(Collectors.toMap(headers::get, row::get)));
                }
            });
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
