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

    @Test
    public void shouldFindByBirthDateQuery() {
        createEmployee("Jan", "Nowak", LocalDate.parse("1960-01-01"));
        createEmployee("Stefan", "Nowacki", LocalDate.parse("1980-01-01"));
        createEmployee("Janusz", "Jankowski", LocalDate.parse("1990-01-01"));

        criteria.setBirthDateFrom(LocalDate.parse("1970-01-01"));
        criteria.setBirthDateTo(LocalDate.parse("1991-01-01"));
        searech();

        assetLastNames("Nowacki", "Jankowski");
    }

    @Test
    public void shouldFindByBirthDateGratherQuery() {
        createEmployee("Jan", "Nowak", LocalDate.parse("1960-01-01"));
        createEmployee("Stefan", "Nowacki", LocalDate.parse("1980-01-01"));
        createEmployee("Janusz", "Jankowski", LocalDate.parse("1990-01-01"));

        criteria.setBirthDateFrom(LocalDate.parse("1981-01-01"));
        searech();

        assetLastNames("Jankowski");
    }

    @Test
    public void shouldFindByBirthDateLowerQuery() {
        createEmployee("Jan", "Nowak", LocalDate.parse("1960-01-01"));
        createEmployee("Stefan", "Nowacki", LocalDate.parse("1980-01-01"));
        createEmployee("Janusz", "Jankowski", LocalDate.parse("1990-01-01"));

        criteria.setBirthDateTo(LocalDate.parse("1981-01-01"));
        searech();

        assetLastNames("Nowak", "Nowacki");
    }

    @Test
    public void shouldPaginateResults() {
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");
        createEmployee("Kowalska");
        createEmployee("Kowalskiego");

        criteria.setPageSize(2);
        criteria.setPageNumber(2);
        criteria.setFirstNameQuery("Cze");
        searech();

        assetLastNames("Kowalski", "Kowalska");
        assertEquals(5, results.getTotalCount());
        assertEquals(3, results.getPagesCount());
        assertEquals(2, results.getPageNumber());
    }

    @Test
    public void shouldSearchBySalary() {
        Employee nowak = createEmployee("Nowak");
        Employee nowacki = createEmployee("Nowacki");
        createEmployee("Kowalski");

        executeInTransaction((em) -> {
            nowak.changeSalary(5000);
            em.merge(nowak);
        });

        executeInTransaction((em) -> {
            nowacki.changeSalary(6000);
            em.merge(nowacki);
        });

        //////////////////

        criteria.setBirthDateTo(LocalDate.parse("1981-01-01"));
        searech();

        assetLastNames("Nowak", "Nowacki");
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
        Employee employee = createEmployee(firstName, lastName, LocalDate.now());
        return employee;
    }

    private Employee createEmployee(String firstName, String lastName, LocalDate birthDate) {
        Address address = new Address("Kunickiego", "Lublin");
        Employee employee = new Employee(employeeNumber++, firstName, lastName, birthDate, address, new StandardTimeProvider());
        executeInTransaction((em) -> {
            em.persist(employee);
        });
        return employee;
    }
}
