package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

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

    @Transient
    private TimeProvider timeProvider;

    public Employee() {}

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
            removeOrTerminateSalary(salary, currentSalaryOptional);
        }
        addNewSalary(salary);
    }

    private void addNewSalary(Integer salary) {
        salaries.add(new Salary(empNo, salary, timeProvider));
    }

    private void removeOrTerminateSalary(Integer salary, Optional<Salary> currentSalaryOptional) {
        Salary currentSalary = currentSalaryOptional.get();
        if (currentSalary.startsToday()) {
            salaries.remove(currentSalary);
        } else {
            currentSalary.terminate();
        }
    }

    public Optional<Salary> getCurrentSalary() {
        return salaries.stream().filter((salary) -> salary.isCurrent()).findFirst();
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
}
