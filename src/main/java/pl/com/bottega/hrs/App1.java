package pl.com.bottega.hrs;

import pl.com.bottega.hrs.model.Department;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.Salary;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Hello world!
 *
 */
public class App1
{
    public static void main( String[] args )
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HRS");
        EntityManager em = emf.createEntityManager();
        Employee employee = em.find(Employee.class, 10001);
        System.out.println(employee.getAddress().toString());
        for (Salary salary: employee.getSalaries()) {
            System.out.println(salary.toString());
        }
        Department department = em.find(Department.class, "d009");
        emf.close();
    }
}
