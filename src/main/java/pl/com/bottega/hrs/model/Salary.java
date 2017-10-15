package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "salaries")
public class Salary {

    @Embeddable
    public static class SalaryId implements Serializable {
        @Column(name = "emp_no")
        private Integer empNo;

        @Column(name = "from_date")
        private LocalDate fromDate;

        public SalaryId(Integer empNo, LocalDate fromDate) {
            this.empNo = empNo;
            this.fromDate = fromDate;
        }

        public SalaryId() {}

        @Override
        public String toString() {
            return String.format("%s %s", empNo, fromDate.toString());
        }
    }

    public Salary() {}

    @EmbeddedId
    private SalaryId id;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Override
    public String toString() {
        return "Salary{" +
            "pk=" + id +
            ", fromDate=" + salary +
            ", toDate=" + toDate +
            '}';
    }
}