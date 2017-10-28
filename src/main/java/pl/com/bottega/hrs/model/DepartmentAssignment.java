package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "dept_emp")
public class DepartmentAssignment {

    private TimeProvider timeProvider;

    public DepartmentAssignment(Integer empNo, Department department, TimeProvider timeProvider) {
        id = new DepartmentAssignmentId(empNo, department);
        fromDate = timeProvider.today();
        this.timeProvider = timeProvider;
        toDate = Constans.MAX_DATE;
    }

    public Department getDepartment() {
        return id.department;
    }

    public DepartmentAssignment() {}

    public boolean isAssigned(Department department) {
        return isCurrent() && department.equals(id.department);
    }

    public void unassign() {
        toDate = timeProvider.today();
    }

    public boolean isCurrent() {
        return toDate.isAfter(timeProvider.today());
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    @Embeddable
    public static class DepartmentAssignmentId implements Serializable {

        @Column(name = "emp_no")
        private Integer empNo;

        @ManyToOne
        @JoinColumn(name = "dept_no")
        private Department department;

        public DepartmentAssignmentId(Integer empNo, Department department) {
            this.empNo = empNo;
            this.department = department;
        }

        public DepartmentAssignmentId() {}
    }

    @EmbeddedId
    private DepartmentAssignmentId id;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;
}
