package pl.com.bottega.hrs.model;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TitleTest {

    private final Address address = new Address("Północna", "Lublin");
    private final TimeMachine timeMachine = new TimeMachine();
    private final Employee sut = new Employee(1, "Jan", "Nowak", LocalDate.parse("1960-01-01"), address, timeMachine);
    public static final String TITLE1 = "Tytuł1";
    public static final String TITLE2 = "Tytuł2";
    public static final String TITLE3 = "Tytuł3";

    @Test
    public void shouldReturnNoTitleIfTitleNoDefined() {
        assertFalse(getCurrentTitle().isPresent());
    }

    @Test
    public void shouldAddAndReturnTitle() {
        sut.changeTitle(TITLE1);
        assertTrue(getCurrentTitle().isPresent());
        assertEquals(TITLE1, getCurrentTitleValue());
    }

    @Test
    public void shouldAllowMultipleChangeSalary() {
        sut.changeTitle(TITLE1);
        sut.changeTitle(TITLE2);
        assertEquals(TITLE2, getCurrentTitleValue());
    }

    @Test
    public void shouldKeepTitleHistory() {
        timeMachine.travel(Duration.ofDays(-365 * 2));
        LocalDate t0 = timeMachine.today();
        sut.changeTitle(TITLE1);
        timeMachine.travel(Duration.ofDays(365));
        LocalDate t1 = timeMachine.today();
        sut.changeTitle(TITLE2);
        timeMachine.travel(Duration.ofDays(100));
        LocalDate t2 = timeMachine.today();
        sut.changeTitle(TITLE3);

        Collection<Title> history = sut.getTitles();
        assertEquals(3, history.size());
        assertEquals(Arrays.asList(TITLE1, TITLE2, TITLE3), history.stream().map((s) -> s.getValue()).collect(Collectors.toList()));
        assertEquals(Arrays.asList(t0, t1, t2), history.stream().map((s) -> s.getFromDate()).collect(Collectors.toList()));
        assertEquals(Arrays.asList(t1, t2, Constans.MAX_DATE), history.stream().map((s) -> s.getToDate()).collect(Collectors.toList()));
    }

    private Optional<Title> getCurrentTitle() {
        return sut.getCurrentTitle();
    }

    private String getCurrentTitleValue() {
        return getCurrentTitle().get().getValue();
    }
}