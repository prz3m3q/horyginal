package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Department;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collector;

public class EmployeeSearchCriteria {
    private String lastNameQuery;
    private String firstNameQuery;
    private LocalDate birthDateFrom, birthDateTo;
    private LocalDate hireDateFrom, hireDateTo;
    private Integer salaryDateFrom, salaryDateTo;
    private Collection<String> title;
    private Collection<String> departmentNumbers;
    private int pageSize = 20;
    private int pageNumber = 1;

    public String getLastNameQuery() {
        return lastNameQuery;
    }

    public void setLastNameQuery(String lastNameQuery) {
        this.lastNameQuery = lastNameQuery;
    }

    public String getFirstNameQuery() {
        return firstNameQuery;
    }

    public void setFirstNameQuery(String firstNameQuery) {
        this.firstNameQuery = firstNameQuery;
    }

    public LocalDate getBirthDateFrom() {
        return birthDateFrom;
    }

    public void setBirthDateFrom(LocalDate birthDateFrom) {
        this.birthDateFrom = birthDateFrom;
    }

    public LocalDate getBirthDateTo() {
        return birthDateTo;
    }

    public void setBirthDateTo(LocalDate birthDateTo) {
        this.birthDateTo = birthDateTo;
    }

    public LocalDate getHireDateFrom() {
        return hireDateFrom;
    }

    public void setHireDateFrom(LocalDate hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    public LocalDate getHireDateTo() {
        return hireDateTo;
    }

    public void setHireDateTo(LocalDate hireDateTo) {
        this.hireDateTo = hireDateTo;
    }

    public Integer getSalaryDateFrom() {
        return salaryDateFrom;
    }

    public void setSalaryDateFrom(Integer salaryDateFrom) {
        this.salaryDateFrom = salaryDateFrom;
    }

    public Integer getSalaryDateTo() {
        return salaryDateTo;
    }

    public void setSalaryDateTo(Integer salaryDateTo) {
        this.salaryDateTo = salaryDateTo;
    }

    public Collection<String> getTitle() {
        return title;
    }

    public void setTitle(Collection<String> title) {
        this.title = title;
    }

    public Collection<String> getDepartmentNumbers() {
        return departmentNumbers;
    }

    public void setDepartmentNumbers(Collection<String> departmentNumbers) {
        this.departmentNumbers = departmentNumbers;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

}
