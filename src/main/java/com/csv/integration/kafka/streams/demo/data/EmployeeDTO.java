package com.csv.integration.kafka.streams.demo.data;

import java.io.Serializable;
import java.util.List;

public class EmployeeDTO implements Serializable {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private Integer yearsOfService;
    private String departmentId;
    private Long managerId;
    private List<Address> addresses;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(Integer yearsOfService) {
        this.yearsOfService = yearsOfService;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public EmployeeDTO() {}
    public EmployeeDTO(Long employeeId, String firstName, String lastName) {
       new EmployeeDTO(employeeId, firstName, lastName, null, null, null);
    }

    public EmployeeDTO(Long employeeId, String firstName, String lastName, Integer yearsOfService, String departmentId, Long managerId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearsOfService = yearsOfService;
        this.departmentId = departmentId;
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        String address = null;
        if (addresses == null || addresses.size() == 0) {
            address = "NA";
        } else {
            address = addresses.get(0).toString();
        }
        return "EmployeeDTO{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", yearsOfService=" + yearsOfService +
                ", departmentId='" + departmentId + '\'' +
                ", managerId='" + managerId + '\'' +
                '}';
    }

    public static EmployeeDTO newInstance(String value) {
        String[] fields = value.split(",");
        EmployeeDTO employeeDTO = new EmployeeDTO();

        switch (fields.length) {
            case 3:
                employeeDTO.setEmployeeId(Long.valueOf(fields[0].trim()));
                employeeDTO.setFirstName(fields[1].trim());
                employeeDTO.setLastName(fields[2].trim());
                break;
            case 6:
                employeeDTO.setEmployeeId(Long.valueOf(fields[0].trim()));
                employeeDTO.setFirstName(fields[1].trim());
                employeeDTO.setLastName(fields[2].trim());
                employeeDTO.setYearsOfService(Integer.valueOf(fields[3]));
                employeeDTO.setDepartmentId(fields[4]);
                employeeDTO.setManagerId(Long.valueOf(fields[5]));
                break;
            default:

        }
        return employeeDTO;
    }

    public static boolean validate(String input) {
        String[] fields = input.split(",");
        boolean valid = false;
        switch (fields.length) {
            case 3:
            case 6:
                valid = true;
                break;
            default:

        }
        return valid;
    }
}
