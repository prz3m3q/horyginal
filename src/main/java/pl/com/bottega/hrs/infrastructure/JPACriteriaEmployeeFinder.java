package pl.com.bottega.hrs.infrastructure;

import org.hibernate.engine.profile.Fetch;
import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResult;
import pl.com.bottega.hrs.model.Constans;
import pl.com.bottega.hrs.model.Department;
import pl.com.bottega.hrs.model.DepartmentAssignment;
import pl.com.bottega.hrs.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JPACriteriaEmployeeFinder implements EmployeeFinder {

    private EntityManager entityManager;

    public JPACriteriaEmployeeFinder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EmployeeSearchResult search(EmployeeSearchCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BasicEmployeeDto> criteriaQuery = criteriaBuilder.createQuery(BasicEmployeeDto.class);
        Root employee = criteriaQuery.from(Employee.class);
        criteriaQuery.select(criteriaBuilder.construct(
            BasicEmployeeDto.class,
            employee.get("empNo"),
            employee.get("firstName"),
            employee.get("lastName")
        ));
        Predicate predicate = buildPredicate(criteria, criteriaBuilder, employee);
        criteriaQuery.where(predicate);
        Query query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(criteria.getPageSize());
        query.setFirstResult(criteria.getPageSize() * (criteria.getPageNumber() - 1));
        List<BasicEmployeeDto> results = query.getResultList();
        EmployeeSearchResult employeeSearchResult = new EmployeeSearchResult();
        employeeSearchResult.setResults(results);
        int totalCount = searchTotalCount(criteria);
        employeeSearchResult.setTotalCount(totalCount);
        employeeSearchResult.setPageNumber(criteria.getPageNumber());
        employeeSearchResult.setPagesCount(totalCount / criteria.getPageSize() + (totalCount % criteria.getPageSize() == 0 ? 0 : 1));
        return employeeSearchResult;
    }

    private int searchTotalCount(EmployeeSearchCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root employee = criteriaQuery.from(Employee.class);
        criteriaQuery.select(criteriaBuilder.count(employee));
        Predicate predicate = buildPredicate(criteria, criteriaBuilder, employee);
        criteriaQuery.where(predicate);
        Query query = entityManager.createQuery(criteriaQuery);
        return ((Long)query.getSingleResult()).intValue();
    }

    private Predicate buildPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root employee) {
        Predicate predicate = criteriaBuilder.conjunction();
        predicate = addFirstNamePredicate(criteria, criteriaBuilder, employee, predicate);
        predicate = addLastNamePredicate(criteria, criteriaBuilder, employee, predicate);
        predicate = addBirthDatePredicate(criteria, criteriaBuilder, employee, predicate);
        predicate = addHireDatePredicate(criteria, criteriaBuilder, employee, predicate);
        predicate = addSalaryPredicate(criteria, criteriaBuilder, employee, predicate);
        predicate = addTitlesPredicate(criteria, criteriaBuilder, employee, predicate);
        predicate = addDepartmentNumbersPredicate(criteria, criteriaBuilder, employee, predicate);
        return predicate;
    }

    private Predicate addDepartmentNumbersPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root employee, Predicate predicate) {
        if (criteria.getDepartmentNumbers() == null) {
            return predicate;
        }
        Join departmentAssigments = employee.join("departmentAssigments");
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(departmentAssigments.get("toDate"), Constans.MAX_DATE));
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.in(departmentAssigments.get("id").get("department").get("deptNo")).value(criteria.getDepartmentNumbers()));
        return predicate;
    }

    private Predicate addTitlesPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root employee, Predicate predicate) {
        if (criteria.getTitles() == null) {
            return predicate;
        }
        Join titles = employee.join("titles");
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(titles.get("toDate"), Constans.MAX_DATE));
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.in(titles.get("id").get("title")).value(criteria.getTitles()));
        return predicate;
    }

    private Predicate addSalaryPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root employee, Predicate predicate) {
        if (criteria.getSalaryFrom() == null && criteria.getSalaryTo() == null) {
            return predicate;
        }
        Join salary = employee.join("salaries");
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(salary.get("toDate"), Constans.MAX_DATE));
        if (criteria.getSalaryFrom() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(salary.get("salary"), criteria.getSalaryFrom()));
        }
        if (criteria.getSalaryTo() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(salary.get("salary"), criteria.getSalaryTo()));
        }
        return predicate;
    }

    private Predicate addHireDatePredicate(EmployeeSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root employee, Predicate predicate) {
        if (criteria.getHireDateFrom() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(employee.get("hireDate"), criteria.getHireDateFrom()));
        }
        if (criteria.getHireDateTo() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(employee.get("hireDate"), criteria.getHireDateTo()));
        }
        return predicate;
    }

    private Predicate addBirthDatePredicate(EmployeeSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root employee, Predicate predicate) {
        if (criteria.getBirthDateFrom() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(employee.get("birthDate"), criteria.getBirthDateFrom()));
        }
        if (criteria.getBirthDateTo() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(employee.get("birthDate"), criteria.getBirthDateTo()));
        }
        return predicate;
    }

    private Predicate addLastNamePredicate(EmployeeSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root employee, Predicate predicate) {
        if (criteria.getLastNameQuery() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(employee.get("lastName"), criteria.getLastNameQuery() + "%"));
        }
        return predicate;
    }

    private Predicate addFirstNamePredicate(EmployeeSearchCriteria criteria, CriteriaBuilder criteriaBuilder, Root employee, Predicate predicate) {
        if (criteria.getFirstNameQuery() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(employee.get("firstName"), criteria.getFirstNameQuery() + "%"));
        }
        return predicate;
    }
}
