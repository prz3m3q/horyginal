package pl.com.bottega.hrs.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;

import javax.persistence.EntityManager;

@Configuration
public class HrsConfiguration {

    @Value("${hrs.departmentRepository}")
    private String departmentStrategy;

    @Bean
    DepartmentRepository departmentRepository(EntityManager entityManager) {
        if (departmentStrategy.equals("csv")) {
            return new CSVDepartmentRepository();
        }
        if (departmentStrategy.equals("jpa")) {
            return new JPADepartmentRepository(entityManager);
        }
        throw new IllegalArgumentException();
    }
}
