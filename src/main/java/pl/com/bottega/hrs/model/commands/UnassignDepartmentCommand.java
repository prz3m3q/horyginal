package pl.com.bottega.hrs.model.commands;

public class UnassignDepartmentCommand {
    private Integer empNo;
    private String deptNo;

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public Integer getEmpNo() {
        return empNo;
    }

    public String getDeptNo() {
        return deptNo;
    }
}
