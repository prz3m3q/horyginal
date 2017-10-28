package pl.com.bottega.hrs;

import pl.com.bottega.hrs.model.Department;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.Salary;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App3
{
    public static void main( String[] args )
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HRS");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.birthDate BETWEEN :birthDateFrom AND :birthDateTo AND e.lastName LIKE :lastName");
        query.setParameter("birthDateFrom", LocalDate.parse("1960-06-01"));
        query.setParameter("birthDateTo", LocalDate.parse("1961-05-01"));
        query.setParameter("lastName", "A%");
        List<Employee> result = query.getResultList();
        for (Employee employee: result) {
            System.out.println(employee);
        }
        emf.close();
    }
}
