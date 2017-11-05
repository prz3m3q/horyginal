package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DetailedEmployeeDto extends BasicEmployeeDto {

    private LocalDate birthDate, hireDate;
    private Gender gender;
    private Address address;
    private Optional<Integer> salary;
    private Optional<String> title;
    private List<String> deptNo;
    private List<SalaryDto> salaryHistory;
    private List<DepartmentDto> departmentHistory;
    private List<TitleDto> titleHistory;

    public DetailedEmployeeDto(Employee employee) {
        super(employee.getEmpNo(), employee.getFirstName(), employee.getLastName());
        this.birthDate = employee.getBirthDate();
        this.hireDate = employee.getHireDate();
        this.gender = employee.getGender();
        this.address = employee.getAddress();
        this.salary = employee.getCurrentSalary().map(Salary::getValue);
        this.title = employee.getCurrentTitle().map(Title::getValue);
        this.deptNo = employee.getCurrentDepartmentsAsList();
        this.salaryHistory = employee.getSalariesHistoryAsDto();
        this.departmentHistory = employee.getDepartmentsHistoryAsDto();
        this.titleHistory = employee.getTitlesAsDto();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Optional<Integer> getSalary() {
        return salary;
    }

    public void setSalary(Optional<Integer> salary) {
        this.salary = salary;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public List<String> getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(List<String> deptNo) {
        this.deptNo = deptNo;
    }

    public List<SalaryDto> getSalaryHistory() {
        return salaryHistory;
    }

    public void setSalaryHistory(List<SalaryDto> salaryHistory) {
        this.salaryHistory = salaryHistory;
    }

    public List<DepartmentDto> getDepartmentHistory() {
        return departmentHistory;
    }

    public void setDepartmentHistory(List<DepartmentDto> departmentHistory) {
        this.departmentHistory = departmentHistory;
    }

    public List<TitleDto> getTitleHistory() {
        return titleHistory;
    }

    public void setTitleHistory(List<TitleDto> titleHistory) {
        this.titleHistory = titleHistory;
    }
}
