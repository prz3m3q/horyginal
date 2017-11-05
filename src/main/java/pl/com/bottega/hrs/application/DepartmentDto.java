package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.DepartmentAssignment;

import java.time.LocalDate;

public class DepartmentDto {
    private LocalDate fromDate, toDate;
    private String deptNo;

    public DepartmentDto(DepartmentAssignment departmentAssignment) {
        this.fromDate = departmentAssignment.getFromDate();
        this.toDate = departmentAssignment.getToDate();
        this.deptNo = departmentAssignment.getDepartment().getNumber();
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }
}
