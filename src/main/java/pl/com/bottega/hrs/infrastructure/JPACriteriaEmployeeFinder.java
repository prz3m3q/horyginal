package pl.com.bottega.hrs.infrastructure;

import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResult;
import pl.com.bottega.hrs.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
