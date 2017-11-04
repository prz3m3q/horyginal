package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Department;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.TimeProvider;
import pl.com.bottega.hrs.model.commands.AddEmployeeCommand;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

public class AddEmployeeHandler {
    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;
    private TimeProvider timeProvider;

    public AddEmployeeHandler(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, TimeProvider timeProvider) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.timeProvider = timeProvider;
    }

    public void handler(AddEmployeeCommand cmd) {
        Employee employee = new Employee(
            employeeRepository.generateNumber(),
            cmd.getFirstName(),
            cmd.getLastName(),
            cmd.getBirthDate(),
            cmd.getAddress(),
            timeProvider
        );
        employee.changeTitle(cmd.getTitle());
        employee.changeSalary(cmd.getSalary());
        Department department = departmentRepository.get(cmd.getDeptNo());
        employee.assignDepartment(department);
        employeeRepository.save(employee);
    }
}
