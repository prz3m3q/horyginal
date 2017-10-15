package pl.com.bottega.hrs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @Column(name = "dept_no", columnDefinition="char(4)")
    private String  deptNo;

    @Column(name = "dept_name")
    private String deptName;

}
