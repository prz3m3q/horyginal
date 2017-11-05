package pl.com.bottega.hrs.application;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.ChangeSalaryCommand;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

import javax.transaction.Transactional;

@Component
public class ChangeSalaryHandler {
    private EmployeeRepository employeeRepository;

    public ChangeSalaryHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void handler(ChangeSalaryCommand cmd) {
        Employee employee = employeeRepository.get(cmd.getEmpNo());
        employee.changeSalary(cmd.getSalary());
        employeeRepository.save(employee);
    }
}
