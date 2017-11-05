package pl.com.bottega.hrs.accept;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.hrs.application.*;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Gender;
import pl.com.bottega.hrs.model.commands.AddDepartmentCommand;
import pl.com.bottega.hrs.model.commands.AddEmployeeCommand;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateEmployeeTest {

    @Autowired
    private AddEmployeeHandler addEmployeeHandler;

    @Autowired
    private AddDepartmentHandler addDepartmentHandler;

    @Autowired
    private EmployeeFinder employeeFinder;

    @Test
    public void shouldCreateEmpoloyee() {
        //given
        AddDepartmentCommand addDepartmentCommand = new AddDepartmentCommand();
        addDepartmentCommand.setName("Markieting");
        addDepartmentCommand.setNumber("d1");
        addDepartmentHandler.handle(addDepartmentCommand);

        //when
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("Janek");
        addEmployeeCommand.setLastName("Nowak");
        addEmployeeCommand.setAddress(new Address("al. Warszawska 10", "Lublin"));
        addEmployeeCommand.setBirthDate(LocalDate.parse("1990-01-01"));
        addEmployeeCommand.setDeptNo("d1");
        addEmployeeCommand.setGender(Gender.M);
        addEmployeeCommand.setSalary(5000);
        addEmployeeCommand.setTitle("CEO");
        addEmployeeHandler.handler(addEmployeeCommand);

        //then
        DetailedEmployeeDto detailedEmployeeDto = employeeFinder.getEmployeeDetails(1);
        assertEquals(addEmployeeCommand.getFirstName(), detailedEmployeeDto.getFirstName());
        assertEquals(addEmployeeCommand.getLastName(), detailedEmployeeDto.getLastName());
        assertEquals(addEmployeeCommand.getAddress(), detailedEmployeeDto.getAddress());
        assertEquals(addEmployeeCommand.getBirthDate(), detailedEmployeeDto.getBirthDate());
        assertEquals(addEmployeeCommand.getDeptNo(), detailedEmployeeDto.getDeptNo());
        assertEquals(addEmployeeCommand.getGender(), detailedEmployeeDto.getGender());
        assertEquals(addEmployeeCommand.getSalary(), detailedEmployeeDto.getSalary());
//
//
//        EmployeeSearchCriteria employeeSearchCriteria = new EmployeeSearchCriteria();
//        employeeSearchCriteria.setFirstNameQuery("Janek");
//        EmployeeSearchResult employeeSearchResult = employeeFinder.search(employeeSearchCriteria);
//
//        assertEquals("Janek", employeeSearchResult.getResults().get(0).getFirstName());
    }
}
