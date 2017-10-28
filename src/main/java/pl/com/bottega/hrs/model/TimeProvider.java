package pl.com.bottega.hrs.model;

import java.time.Clock;
import java.time.LocalDate;

public interface TimeProvider {
    default LocalDate today() {
        return LocalDate.now(clock());
    }

    Clock clock();
}
