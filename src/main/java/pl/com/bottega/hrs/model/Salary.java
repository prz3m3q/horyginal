package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "salaries")
public class Salary {

    public Salary(SalaryId id, Integer salary, LocalDate toDate) {
        this.id = id;
        this.salary = salary;
        this.toDate = toDate;
    }

    public Salary(Integer empNo, Integer salary, TimeProvider timeProvider) {
        id = new SalaryId(empNo, timeProvider);
        this.salary = salary;
        this.timeProvider = timeProvider;
        this.toDate = Constans.MAX_DATE;
    }

    public boolean isCurrent() {
        return this.toDate.isAfter(timeProvider.today());
    }

    public void terminate() {
        toDate = timeProvider.today();
    }

    public boolean startsToday() {
        return id.startsToday();
    }

    public int getValue() {
        return salary;
    }

    @Embeddable
    public static class SalaryId implements Serializable {
        @Column(name = "emp_no")
        private Integer empNo;

        @Column(name = "from_date")
        private LocalDate fromDate;

        @Transient
        private TimeProvider timeProvider;

        public SalaryId(Integer empNo, LocalDate fromDate) {
            this.empNo = empNo;
            this.fromDate = fromDate;
        }

        public SalaryId() {}

        public SalaryId(Integer empNo, TimeProvider timeProvider) {
            this.empNo = empNo;
            this.timeProvider = timeProvider;
            this.fromDate = LocalDate.now();
        }

        @Override
        public String toString() {
            return String.format("%s %s", empNo, fromDate.toString());
        }

        public boolean startsToday() {
            return fromDate.isEqual(timeProvider.today());
        }
    }

    public Salary() {}

    @EmbeddedId
    private SalaryId id;

    @Column(name = "salary")
    private Integer salary;

    @Transient
    private TimeProvider timeProvider;

    @Column(name = "to_date")
    private LocalDate toDate;

    public Integer getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Salary{" +
            "pk=" + id +
            ", fromDate=" + salary +
            ", toDate=" + toDate +
            '}';
    }
}