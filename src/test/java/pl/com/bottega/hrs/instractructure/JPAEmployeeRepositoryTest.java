package pl.com.bottega.hrs.instractructure;

import org.junit.Test;
import pl.com.bottega.hrs.infrastructure.JPAEmployeeRepository;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.StandardTimeProvider;
import pl.com.bottega.hrs.model.exception.NoSuchEmployee;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JPAEmployeeRepositoryTest extends InfrastructureTest {

    private EntityManager entityManager = createEntityManager();
    private JPAEmployeeRepository sut = new JPAEmployeeRepository(entityManager);

    @Test
    public void firstNumberShouldBeOne() {
        Integer number = sut.generateNumber();
        assertEquals(new Integer(1), number);
    }

    @Test
    public void shpouldSaveEmployee() {
        //given
        Employee employee = createEmployee(1, "Kowalski");

        //when
        executeInTransaction(entityManager, () -> sut.save(employee));

        //then

        executeInTransaction(entityManager, () -> {
            Employee employeeFromRepo = null;
            try {
                employeeFromRepo = sut.get(1);
            } catch (NoSuchEmployee noSuchEmployee) {
                noSuchEmployee.printStackTrace();
            }
            assertNotNull(employeeFromRepo);
            assertEquals("Kowalski", employeeFromRepo.getLastName());
        });
    }

    @Test
    public void shouldGenerateNextEmpNo() {
        //given
        Employee e1 = createEmployee(1, "Kowalski");
        Employee e2 = createEmployee(2, "Nowak");
        executeInTransaction(entityManager, () -> sut.save(e1));
        executeInTransaction(entityManager, () -> sut.save(e2));

        //when
        Integer number = sut.generateNumber();

        //then
        assertEquals(new Integer(3), number);
    }

    @Test(expected = NoSuchEmployee.class)
    public void checkNoExistsEmployee() {
        sut.get(1);
    }

    private Employee createEmployee(int empNo, String lastName) {
        Address address = new Address("Kunickiego", "Lublin");
        Employee employee = new Employee(empNo, "Jan", lastName, LocalDate.now(), address, new StandardTimeProvider());
        return employee;
    }
}
