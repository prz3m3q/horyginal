package pl.com.bottega.hrs.infrastructure;

import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
        return entityManager.find(Employee.class, empNo);
    }
}
