package pl.com.bottega.hrs.application;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.ChangeEmployeeTitleCommand;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

import javax.transaction.Transactional;

@Component
public class ChangeEmployeeTitleHandler {
    private EmployeeRepository employeeRepository;

    public ChangeEmployeeTitleHandler(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void handle(ChangeEmployeeTitleCommand cmd) {
        Employee employee = employeeRepository.get(cmd.getEmpNo());
        employee.changeTitle(cmd.getTitle());
        employeeRepository.save(employee);

    }
}
