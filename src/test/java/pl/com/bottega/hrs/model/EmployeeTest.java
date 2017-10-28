package pl.com.bottega.hrs.model;

import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeTest {

    private final Address address = new Address("Północna", "Lublin");
    private final TimeMachine timeMachine = new TimeMachine();
    private final Employee sut = new Employee(1, "Jan", "Nowak", LocalDate.parse("1960-01-01"), address, timeMachine);
    public static final int SALARY = 50000 * 12;
    private Department department1 = Mockito.mock(Department.class);
    private Department department2 = Mockito.mock(Department.class);

    @Test
    public void shouldReturnNoSalaryIfSakaryNoDefined() {
        assertFalse(getCurrentSalary().isPresent());
    }

    @Test
    public void shouldAddAndReturnEmployeeAndSalary() {
        sut.changeSalary(SALARY);
        assertTrue(getCurrentSalary().isPresent());
        assertEquals(SALARY, getCurrentSalaryValue());
    }

    @Test
    public void shouldAllowMultipleChangeSalary() {
        sut.changeSalary(SALARY);
        sut.changeSalary(SALARY / 2);
        assertEquals(SALARY / 2, getCurrentSalaryValue());
    }

    @Test
    public void shouldKeepSalaryHistory() {
        timeMachine.travel(Duration.ofDays(-365 * 2));
        LocalDate t0 = timeMachine.today();
        sut.changeSalary(SALARY);
        timeMachine.travel(Duration.ofDays(365));
        LocalDate t1 = timeMachine.today();
        sut.changeSalary(SALARY / 2);
        timeMachine.travel(Duration.ofDays(100));
        LocalDate t2 = timeMachine.today();
        sut.changeSalary(SALARY * 2);

        Collection<Salary> history = sut.getSalaries();
        assertEquals(3, history.size());
        assertEquals(Arrays.asList(SALARY, SALARY / 2, SALARY * 2), history.stream().map((s) -> s.getValue()).collect(Collectors.toList()));
        assertEquals(Arrays.asList(t0, t1, t2), history.stream().map((s) -> s.getFromDate()).collect(Collectors.toList()));
        assertEquals(Arrays.asList(t1, t2, Constans.MAX_DATE), history.stream().map((s) -> s.getToDate()).collect(Collectors.toList()));
    }

    private int getCurrentSalaryValue() {
        return getCurrentSalary().get().getValue();
    }

    private Optional<Salary> getCurrentSalary() {
        return sut.getCurrentSalary();
    }

    @Test
    public void shouldReturnEmptyDepartmentsWhenNoAssigment() {
        assertEquals(0, sut.getCurrentDepartments().size());
    }

    @Test
    public void shouldAssigmentToManyDepartments() {
        sut.assignDepartment(department1);
        sut.assignDepartment(department2);

        assertEquals(Arrays.asList(department1, department2), sut.getCurrentDepartments());
        assertEquals(2, sut.getCurrentDepartments().size());
    }

    public void shouldUnassignedDepartments() {
        sut.assignDepartment(department1);
        sut.assignDepartment(department2);
        sut.unAssignDepartment(department2);

        assertEquals(Arrays.asList(department1), sut.getCurrentDepartments());
    }

    @Test
    public void shouldKeepDepartmentHistory() {
        timeMachine.travel(Duration.ofDays(-365 * 2));
        LocalDate t0 = timeMachine.today();
        sut.assignDepartment(department1);
        timeMachine.travel(Duration.ofDays(365));
        LocalDate t1 = timeMachine.today();
        sut.assignDepartment(department2);
        timeMachine.travel(Duration.ofDays(100));
        LocalDate t2 = timeMachine.today();
        sut.unAssignDepartment(department2);

        Collection<DepartmentAssignment> history = sut.getDepartmentsHistory();
        assertEquals(Arrays.asList(t0, t1), history.stream().map(DepartmentAssignment::getFromDate).collect(Collectors.toList()));
        assertEquals(Arrays.asList(Constans.MAX_DATE, t2), history.stream().map(DepartmentAssignment::getToDate).collect(Collectors.toList()));
    }
}
