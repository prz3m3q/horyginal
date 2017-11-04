package pl.com.bottega.hrs.model.commands;

public class ChangeSalaryCommand {
    private Integer empNo;
    private Integer salary;

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
