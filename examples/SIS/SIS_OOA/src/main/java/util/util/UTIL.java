package util.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import io.ciera.runtime.api.domain.Domain;
import io.ciera.runtime.api.types.Date;
import io.ciera.runtime.api.types.TimeStamp;

public class UTIL {

    public UTIL(Domain domain) {
    }

    public TimeStamp parse_date(final String p_s, final String p_pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(p_pattern);
        LocalDate date = LocalDate.parse(p_s, format);
        return new Date((date.getLong(ChronoField.EPOCH_DAY) * 24 * 3600 * 1000000000l));
    }

}
