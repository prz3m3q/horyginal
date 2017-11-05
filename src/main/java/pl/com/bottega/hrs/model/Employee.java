package pl.com.bottega.hrs.model;

import pl.com.bottega.hrs.application.DepartmentDto;
import pl.com.bottega.hrs.application.SalaryDto;
import pl.com.bottega.hrs.application.TitleDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "employees")
public class Employee extends HrsModel {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "enum('M', 'F')")
    private Gender gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private Collection<Salary> salaries = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private Collection<DepartmentAssignment> departmentAssigments = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private Collection<Title> titles = new LinkedList<>();

    @Transient
    private TimeProvider timeProvider;

    public Employee() {
    }

    public Employee(Integer empNo, String firstName, String lastName, LocalDate birthDate, Address address, TimeProvider timeProvider) {
        this.empNo = empNo;
        this.birthDate = birthDate;
        this.address = address;
        this.timeProvider = timeProvider;
        this.hireDate = timeProvider.today();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void updateProfile(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public List<String> getCurrentDepartmentsAsList() {
        return this.getCurrentDepartments().stream().map((dept) -> dept.getNumber()).collect(Collectors.toList());
    }

    public Integer getEmpNo() {
        return empNo;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public Gender getGender() {
        return gender;
    }

    public Collection<DepartmentAssignment> getDepartmentAssigments() {
        return departmentAssigments;
    }

    public String getFirstName() {
        return firstName;
    }

    public Address getAddress() {
        return address;
    }

    public Collection<Salary> getSalaries() {
        return salaries;
    }

    public void changeSalary(Integer salary) {
        Optional<Salary> currentSalaryOptional = getCurrentSalary();
        if (currentSalaryOptional.isPresent()) {
            removeOrTerminateSalary(currentSalaryOptional);
        }
        addNewSalary(salary);
    }

    private void addNewSalary(Integer salary) {
        salaries.add(new Salary(empNo, salary, timeProvider));
    }

    private void removeOrTerminateSalary(Optional<Salary> currentSalaryOptional) {
        Salary currentSalary = currentSalaryOptional.get();
        if (currentSalary.startsToday()) {
            salaries.remove(currentSalary);
        } else {
            currentSalary.terminate();
        }
    }

    private boolean isCurrentlyAssignment(Department department) {
        return getCurrentDepartments().contains(department);
    }

    public Optional<Salary> getCurrentSalary() {
        return salaries.stream().filter(Salary::isCurrent).findFirst();
    }

    public void assignDepartment(Department department) {
        departmentAssigments.add(new DepartmentAssignment(empNo, department, timeProvider));
    }

    public void unAssignDepartment(Department department) {
        departmentAssigments.stream().filter((as) -> as.isAssigned(department)).findFirst().ifPresent((as) -> as.unassign());
    }

    public Collection<Department> getCurrentDepartments() {
        return departmentAssigments.stream().filter(DepartmentAssignment::isCurrent).map(DepartmentAssignment::getDepartment).collect(Collectors.toList());
    }

    public Optional<Title> getCurrentTitle() {
        return titles.stream().filter(Title::isCurrent).findFirst();
    }

    public void changeTitle(String title) {
        Optional<Title> currentTitleOptional = getCurrentTitle();
        if (currentTitleOptional.isPresent()) {
            removeOrTerminateTitle(currentTitleOptional);
        }
        addNewTitle(title);
    }

    private void removeOrTerminateTitle(Optional<Title> currentTitleOptional) {
        Title currentTitle = currentTitleOptional.get();
        if (currentTitle.startsToday()) {
            titles.remove(currentTitle);
        } else {
            currentTitle.terminate();
        }
    }

    public void updateProfile(String firstName, String lastName, LocalDate birthDate, Address address, Gender gender) {
        updateProfile(firstName, lastName, birthDate);
        this.address = address;
        this.gender = gender;
    }

    private void addNewTitle(String title) {
        titles.add(new Title(empNo, title, timeProvider));
    }

    @Override
    public String toString() {
        return "Employee{" +
            "empNo=" + empNo +
            ", birthDate=" + birthDate +
            ", hireDate=" + hireDate +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", gender=" + gender +
            '}';
    }

    public Collection<DepartmentAssignment> getDepartmentsHistory() {
        return departmentAssigments;
    }

    public Collection<Title> getTitles() {
        return titles;
    }

    public String getLastName() {
        return lastName;
    }

    public List<SalaryDto> getSalariesHistoryAsDto() {
        return getSalaries().stream().map(SalaryDto::new).collect(Collectors.toList());
    }

    public List<DepartmentDto> getDepartmentsHistoryAsDto() {
        return getDepartmentsHistory().stream().map(DepartmentDto::new).collect(Collectors.toList());
    }

    public List<TitleDto> getTitlesAsDto() {
        return getTitles().stream().map(TitleDto::new).collect(Collectors.toList());
    }
}
