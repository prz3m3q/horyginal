package pl.com.bottega.hrs.instractructure;

import org.hibernate.LazyInitializationException;
import org.junit.Test;
import pl.com.bottega.hrs.infrastructure.StandardTimeProvider;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EntityManagerTest extends InfrastructureTest {

    @Test
    public void trackChangesToEntities() {
        //given
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.persist(employee);
        });

        //when
        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            updateFirstName("Janusz", employee);
        });

        //then
        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            assertEquals("Janusz", employee.getFirstName());
        });
    }

    @Test
    public void mergeEntities() {
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.persist(employee);
        });

        executeInTransaction((em) -> {
            Employee employee = createEmployee("Janusz");
            Employee employeeCopy = em.merge(employee);
            updateFirstName("Stefan", employee);
            updateFirstName("Eustachy", employeeCopy);
        });

        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            assertEquals("Eustachy", employee.getFirstName());
        });
    }

    private Employee createEmployee(String name) {
        Address address = new Address("Kunickiego", "Lublin");
        return new Employee(1, name, "Nowak", LocalDate.now(), address, new StandardTimeProvider());
    }

    private void updateFirstName(String firstName, Employee employee) {
        employee.updateProfile(firstName, "Nowak", LocalDate.now());
    }

    @Test
    public void removeEntities() {
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.persist(employee);
        });

        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            em.remove(employee);
        });

        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            assertNull(employee);
        });
    }

    @Test
    public void cascadesOperations() {
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Janek");
            em.persist(employee);
        });

        executeInTransaction((em) -> {
            Address address = em.find(Address.class, 1);
            assertNotNull(address);
        });
    }

    private Employee tmpEmployee;

    @Test(expected = LazyInitializationException.class)
    public void throwsInitException() {
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Janek");
            em.persist(employee);
        });

        executeInTransaction((em) -> {
            tmpEmployee = em.find(Employee.class, 1);
        });

        tmpEmployee.getSalaries().size();
    }
}