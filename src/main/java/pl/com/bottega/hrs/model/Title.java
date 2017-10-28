package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "titles")
public class Title {


    public boolean startsToday() {
        return id.startsToday();
    }

    public void terminate() {
        toDate = timeProvider.today();
    }

    public String getValue() {
        return id.title;
    }

    public LocalDate getFromDate() {
        return id.fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    @Embeddable
    private static class TitleId implements Serializable{

        @Column(name = "emp_no")
        private Integer empNo;

        private String title;

        @Column(name = "from_date")
        private LocalDate fromDate;

        @Transient
        private TimeProvider timeProvider;

        public TitleId(Integer empNo, String title, TimeProvider timeProvider) {
            this.empNo = empNo;
            this.title = title;
            this.fromDate = timeProvider.today();
            this.timeProvider = timeProvider;
        }

        TitleId() {
        }

        public boolean startsToday() {
            return fromDate.isEqual(timeProvider.today());
        }
    }

    @EmbeddedId
    private TitleId id;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Transient
    private TimeProvider timeProvider;

    Title() {}

    public Title(Integer empNo, String title, TimeProvider timeProvider) {
        this.id = new TitleId(empNo, title, timeProvider);
        this.toDate = Constans.MAX_DATE;
        this.timeProvider = timeProvider;
    }

    public boolean isCurrent() {
        return this.toDate.isAfter(LocalDate.now());
    }
}
