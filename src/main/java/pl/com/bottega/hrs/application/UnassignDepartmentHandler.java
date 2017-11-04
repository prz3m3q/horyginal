package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.UnassignDepartmentCommand;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

public class UnassignDepartmentHandler {
    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    public UnassignDepartmentHandler(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public void handle(UnassignDepartmentCommand cmd) {
        Employee employee = employeeRepository.get(cmd.getEmpNo());
        employee.unAssignDepartment(departmentRepository.get(cmd.getDeptNo()));
        employeeRepository.save(employee);
    }
}
