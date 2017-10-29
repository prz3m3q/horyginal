package pl.com.bottega.hrs.instractructure;

import org.junit.Test;
import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResult;
import pl.com.bottega.hrs.infrastructure.JPACriteriaEmployeeFinder;
import pl.com.bottega.hrs.infrastructure.JPQLEmployeeFinder;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.StandardTimeProvider;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class EmployeeFinderTest extends InfrastructureTest {

    private int employeeNumber = 1;
//    private EmployeeFinder employeeFinder = new JPQLEmployeeFinder(createEntityManager());
    private EmployeeFinder employeeFinder = new JPACriteriaEmployeeFinder(createEntityManager());
    private EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
    private EmployeeSearchResult results;

    @Test
    public void shouldFindByLastNameQuery() {
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");

        criteria.setLastNameQuery("nowa");
        searech();

        assetLastNames("Nowak", "Nowacki");
    }

    @Test
    public void shouldFindByFirstNameAndLastNameQuery() {
        createEmployee("Jan", "Nowak");
        createEmployee("Stefan", "Nowacki");
        createEmployee("Kowalski");

        criteria.setLastNameQuery("nowa");
        criteria.setFirstNameQuery("Ja");
        searech();

        assetLastNames("Nowak");
    }

    @Test
    public void shouldFindByFirstNameQuery() {
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");

        criteria.setFirstNameQuery("Cze");
        searech();

        assetLastNames("Nowak", "Nowacki", "Kowalski");
    }

    private void searech() {
        results = employeeFinder.search(criteria);
    }

    private void assetLastNames(String... lastNames) {
        assertEquals(Arrays.asList(lastNames), results.getResults().stream().map(BasicEmployeeDto::getLastName).collect(Collectors.toList()));
    }

    private Employee createEmployee(String lastName) {
        Employee employee = createEmployee("Czesiek", lastName);
        return employee;
    }

    private Employee createEmployee(String firstName, String lastName) {
        Address address = new Address("Kunickiego", "Lublin");
        Employee employee = new Employee(employeeNumber++, firstName, lastName, LocalDate.now(), address, new StandardTimeProvider());
        executeInTransaction((em) -> {
            em.persist(employee);
        });
        return employee;
    }
}
