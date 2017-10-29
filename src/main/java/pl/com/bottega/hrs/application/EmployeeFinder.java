package pl.com.bottega.hrs.application;

public interface EmployeeFinder {
    EmployeeSearchResult search(EmployeeSearchCriteria criteria);
}