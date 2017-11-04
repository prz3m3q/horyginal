package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.ChangeSalaryCommand;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

public class ChangeSalaryHandler {
    private EmployeeRepository employeeRepository;

    public ChangeSalaryHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void handler(ChangeSalaryCommand cmd) {
        Employee employee = employeeRepository.get(cmd.getEmpNo());
        employee.changeSalary(cmd.getSalary());
        employeeRepository.save(employee);
    }
}
