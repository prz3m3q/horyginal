package pl.com.bottega.hrs.infrastructure;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.exception.NoSuchEmployee;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class JPAEmployeeRepository implements EmployeeRepository {

    private EntityManager entityManager;

    public JPAEmployeeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Integer generateNumber() {
        Query query = entityManager.createQuery("SELECT MAX(e.empNo) FROM Employee e");
        Integer maxId = (Integer) query.getSingleResult();
        if (maxId == null) {
            return 1;
        }
        return maxId + 1;
    }

    @Override
    public void save(Employee employee) {
        entityManager.persist(employee);
    }

    @Override
    public Employee get(Integer empNo) {
        Employee employee = entityManager.find(Employee.class, empNo);
        if (employee == null) {
            throw new NoSuchEmployee();
        }
        return employee;
    }
}
