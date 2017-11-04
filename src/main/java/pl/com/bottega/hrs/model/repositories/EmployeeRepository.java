package pl.com.bottega.hrs.model.repositories;

import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.exception.NoSuchEmployee;

public interface EmployeeRepository {
    Integer generateNumber();
    void save(Employee employee);
    Employee get(Integer empNo);
}
