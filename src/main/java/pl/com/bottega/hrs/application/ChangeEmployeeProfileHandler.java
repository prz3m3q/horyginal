package pl.com.bottega.hrs.application;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.ChangeEmployeeProfileCommand;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

import javax.transaction.Transactional;

@Component
public class ChangeEmployeeProfileHandler {
    private EmployeeRepository repository;

    public ChangeEmployeeProfileHandler(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handle(ChangeEmployeeProfileCommand cmd) {
        Employee employee = repository.get(cmd.getEmpNo());
        employee.updateProfile(cmd.getFirstName(), cmd.getLastName(), cmd.getBirthDate(), cmd.getAddress(), cmd.getGender());
        repository.save(employee);

    }
}
