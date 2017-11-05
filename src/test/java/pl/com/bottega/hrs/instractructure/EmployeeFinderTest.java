package pl.com.bottega.hrs.instractructure;

import org.junit.Test;
import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResult;
import pl.com.bottega.hrs.infrastructure.JPACriteriaEmployeeFinder;
import pl.com.bottega.hrs.infrastructure.StandardTimeProvider;
import pl.com.bottega.hrs.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class EmployeeFinderTest extends InfrastructureTest {

    private int employeeNumber = 1;
//    private EmployeeFinder employeeFinder = new JPQLEmployeeFinder(createEntityManager());
    private EmployeeFinder employeeFinder = new JPACriteriaEmployeeFinder(createEntityManager());
    private EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
    private final TimeMachine timeMachine = new TimeMachine();
    private EmployeeSearchResult results;

    @Test
    public void shouldFindByLastNameQuery() {
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");

        criteria.setLastNameQuery("nowa");
        search();

        assertLastNames("Nowak", "Nowacki");
    }

    @Test
    public void shouldFindByFirstNameAndLastNameQuery() {
        createEmployee("Jan", "Nowak");
        createEmployee("Stefan", "Nowacki");
        createEmployee("Kowalski");

        criteria.setLastNameQuery("nowa");
        criteria.setFirstNameQuery("Ja");
        search();

        assertLastNames("Nowak");
    }

    @Test
    public void shouldFindByFirstNameQuery() {
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");

        criteria.setFirstNameQuery("Cze");
        search();

        assertLastNames("Nowak", "Nowacki", "Kowalski");
    }

    @Test
    public void shouldFindByBirthDateQuery() {
        createEmployee("Jan", "Nowak", LocalDate.parse("1960-01-01"));
        createEmployee("Stefan", "Nowacki", LocalDate.parse("1980-01-01"));
        createEmployee("Janusz", "Jankowski", LocalDate.parse("1990-01-01"));

        criteria.setBirthDateFrom(LocalDate.parse("1970-01-01"));
        criteria.setBirthDateTo(LocalDate.parse("1991-01-01"));
        search();

        assertLastNames("Nowacki", "Jankowski");
    }

    @Test
    public void shouldFindByBirthDateGratherQuery() {
        createEmployee("Jan", "Nowak", LocalDate.parse("1960-01-01"));
        createEmployee("Stefan", "Nowacki", LocalDate.parse("1980-01-01"));
        createEmployee("Janusz", "Jankowski", LocalDate.parse("1990-01-01"));

        criteria.setBirthDateFrom(LocalDate.parse("1981-01-01"));
        search();

        assertLastNames("Jankowski");
    }

    @Test
    public void shouldFindByBirthDateLowerQuery() {
        createEmployee("Jan", "Nowak", LocalDate.parse("1960-01-01"));
        createEmployee("Stefan", "Nowacki", LocalDate.parse("1980-01-01"));
        createEmployee("Janusz", "Jankowski", LocalDate.parse("1990-01-01"));

        criteria.setBirthDateTo(LocalDate.parse("1981-01-01"));
        search();

        assertLastNames("Nowak", "Nowacki");
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
        search();

        assertLastNames("Kowalski", "Kowalska");
        assertEquals(5, results.getTotalCount());
        assertEquals(3, results.getPagesCount());
        assertEquals(2, results.getPageNumber());
    }

    @Test
    public void shouldSearchBySalaryTo() {
        Employee nowak = createEmployee("Nowak", timeMachine);
        Employee nowacki = createEmployee("Nowacki", timeMachine);
        createEmployee("Kowalski");

        timeMachine.travel(Duration.ofDays(-365 * 2));
        changeSalaryForEmployee(nowak, 5000);
        changeSalaryForEmployee(nowacki, 6000);

        timeMachine.travel(Duration.ofDays(365));
        changeSalaryForEmployee(nowak, 7000);
        changeSalaryForEmployee(nowacki, 8000);

        timeMachine.travel(Duration.ofDays(100));
        changeSalaryForEmployee(nowak, 1000);
        changeSalaryForEmployee(nowacki, 2000);

        criteria.setSalaryTo(2000);
        search();

        assertLastNames("Nowak", "Nowacki");
    }

    @Test
    public void shouldSearchBySalaryFromTo() {
        Employee nowak = createEmployee("Nowak", timeMachine);
        Employee nowacki = createEmployee("Nowacki", timeMachine);
        createEmployee("Kowalski");

        timeMachine.travel(Duration.ofDays(-365 * 2));
        changeSalaryForEmployee(nowak, 5000);
        changeSalaryForEmployee(nowacki, 6000);

        timeMachine.travel(Duration.ofDays(365));
        changeSalaryForEmployee(nowak, 7000);
        changeSalaryForEmployee(nowacki, 8000);

        timeMachine.travel(Duration.ofDays(100));
        changeSalaryForEmployee(nowak, 1000);
        changeSalaryForEmployee(nowacki, 2000);

        criteria.setSalaryFrom(1500);
        criteria.setSalaryTo(10000);
        search();

        assertLastNames("Nowacki");
    }

    @Test
    public void shouldSearchByTile() {
        Employee nowak = createEmployee("Nowak", timeMachine);
        Employee nowacki = createEmployee("Nowacki", timeMachine);

        timeMachine.travel(Duration.ofDays(-365 * 2));
        changeTitleForEmployee(nowak, "title1");
        changeTitleForEmployee(nowacki, "title2");

        timeMachine.travel(Duration.ofDays(365));
        changeTitleForEmployee(nowak, "title3");
        changeTitleForEmployee(nowacki, "title4");

        timeMachine.travel(Duration.ofDays(100));
        changeTitleForEmployee(nowak, "title5");
        changeTitleForEmployee(nowacki, "title2");

        criteria.setTitles(Arrays.asList("title5"));
        search();

        assertLastNames("Nowak");
    }

    @Test
    public void shouldSearchByDepartmentNumbers() {
        Employee nowak = createEmployee("Nowak", timeMachine);
        Employee nowacki = createEmployee("Nowacki", timeMachine);

        Department dept1 = createDepartment("d001", "Dept1");
        Department dept2 = createDepartment("d002", "Dept2");
        Department dept3 = createDepartment("d003", "Dept3");
        Department dept4 = createDepartment("d004", "Dept4");

        timeMachine.travel(Duration.ofDays(-365 * 2));
        assignDepartmentToEmployee(nowak, dept1);
        assignDepartmentToEmployee(nowacki, dept2);

        timeMachine.travel(Duration.ofDays(365));
        assignDepartmentToEmployee(nowak, dept3);
        assignDepartmentToEmployee(nowacki, dept4);
        unassignDepartmentToEmployee(nowak, dept1);

        timeMachine.travel(Duration.ofDays(100));

        criteria.setDepartmentNumbers(Arrays.asList("d001", "d004", "d002"));
        search();

        assertLastNames("Nowacki");
    }

    @Test
    public void shouldSearchByHistoricalDepartments() {
        //given
        Department dept1 = createDepartment("d001", "Dept1");
        Department dept2 = createDepartment("d002", "Dept2");
        Department dept3 = createDepartment("d003", "Dept3");
        Department dept4 = createDepartment("d004", "Dept4");

        employee().withLastName("Nowak").withDepartment(dept1).withDepartment(dept3).withoutDepartment(dept1).create();
        employee().withLastName("Nowacki").withDepartment(dept2, "1960-01-01").withDepartment(dept4).create();

        //when
        criteria.setDepartmentNumbers(Arrays.asList(dept1.getNumber(), dept2.getNumber(), dept4.getNumber()));
        search();

        //then
        assertLastNames("Nowacki");
    }



    @Test
    public void np1Demo() {
        int n = 5;
        for (int i = 0; i <= 5; i++) {
            employee().withSalary(5000).withLastName("Nowak" + i).create();
        }

        executeInTransaction((em) -> {
            List<Employee> emps = em.createQuery("SELECT e FROM Employee e JOIN FETCH e.salaries JOIN FETCH e.address").getResultList();
            for (Employee e: emps) {
                System.out.println(e.getCurrentSalary().get());
            }
        });
    }

    private void search() {
        results = employeeFinder.search(criteria);
    }

    private void assertLastNames(String... lastNames) {
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

    private void changeSalaryForEmployee(Employee employee, int salary) {
        executeInTransaction((em) -> {
            employee.changeSalary(salary);
            em.merge(employee);
        });
    }

    private void changeTitleForEmployee(Employee employee, String title) {
        executeInTransaction((em) -> {
            employee.changeTitle(title);
            em.merge(employee);
        });
    }

    private void assignDepartmentToEmployee(Employee employee, Department department) {
        executeInTransaction((em) -> {
            employee.assignDepartment(department);
            em.merge(employee);
        });
    }

    private void unassignDepartmentToEmployee(Employee employee, Department department) {
        executeInTransaction((em) -> {
            employee.unAssignDepartment(department);
            em.merge(employee);
        });
    }

    private Employee createEmployee(String firstName, String lastName, LocalDate birthDate) {
        Address address = new Address("Kunickiego", "Lublin");
        Employee employee = new Employee(employeeNumber++, firstName, lastName, birthDate, address, new StandardTimeProvider());
        executeInTransaction((em) -> {
            em.persist(employee);
        });
        return employee;
    }

    private Employee createEmployee(String lastName, TimeProvider timeProvider) {
        Address address = new Address("Kunickiego", "Lublin");
        Employee employee = new Employee(employeeNumber++, "Czesiek", lastName, LocalDate.now(), address, timeProvider);
        executeInTransaction((em) -> {
            em.persist(employee);
        });
        return employee;
    }

    private Department createDepartment(String deptNo, String name) {
        Department department = new Department(deptNo, name);
        executeInTransaction((em) -> {
            em.persist(department);
        });
        return department;
    }

    class EmployeeBuilder {
        private String firstName = "Czesiek";
        private String lastName = "Nowak";
        private String birthDate = "1990-01-01";
        private Address address = new Address("al. Warszawska 10", "Lublin");
        private List<Consumer<Employee>> consumers = new LinkedList<>();

        EmployeeBuilder withDepartment(Department department) {
            consumers.add(employee -> {
                employee.assignDepartment(department);
            });
            return this;
        }

        EmployeeBuilder withoutDepartment(Department department) {
            consumers.add(employee -> {
                employee.unAssignDepartment(department);
            });
            return this;
        }

        EmployeeBuilder withDepartment(Department department, String fromDate) {
            consumers.add(employee -> {
                timeMachine.travel(LocalDate.parse(fromDate));
                employee.assignDepartment(department);
                timeMachine.reset();
            });
            return this;
        }

        EmployeeBuilder withName(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            return this;
        }

        EmployeeBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        EmployeeBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        EmployeeBuilder withBirthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        EmployeeBuilder withSalary(Integer salary) {
            consumers.add(employee -> employee.changeSalary(salary));
            return this;
        }

        EmployeeBuilder withSalary(Integer salary, String fromDate) {
            consumers.add(employee -> {
                timeMachine.travel(LocalDate.parse(fromDate));
                employee.changeSalary(salary);
                timeMachine.reset();
            });
            return this;
        }

        Employee create() {
            Employee employee = new Employee(employeeNumber++, firstName, lastName, LocalDate.parse(birthDate), address, timeMachine);
            consumers.forEach(c -> c.accept(employee));
            executeInTransaction((em) -> {
                em.persist(employee);
            });
            return employee;
        }
    }

    private EmployeeBuilder employee() {
        return new EmployeeBuilder();
    }
}
