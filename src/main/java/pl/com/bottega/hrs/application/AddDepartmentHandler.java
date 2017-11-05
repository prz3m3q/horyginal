package pl.com.bottega.hrs.application;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Department;
import pl.com.bottega.hrs.model.commands.AddDepartmentCommand;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;

import javax.transaction.Transactional;

@Component
public class AddDepartmentHandler {
    private DepartmentRepository departmentRepository;

    public AddDepartmentHandler(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public void handle(AddDepartmentCommand cmd) {
        Department department = new Department(cmd.getNumber(), cmd.getName());
        departmentRepository.save(department);
    }
}
