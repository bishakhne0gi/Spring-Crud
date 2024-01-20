package com.crud.serverside.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;

    @ManyToMany
    @JoinTable(
            name = "employee_project",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")}
    )
    private List<Employee> assignedEmployees;

    public Project(){

    }

    public Project(long id, String projectName, String projectDescription) {
        this.id = id;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.assignedEmployees = new ArrayList<>();
    }

    public Project(long id, String projectName, String projectDescription, List<Employee> assignedEmployees) {
        this.id = id;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.assignedEmployees = assignedEmployees;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public List<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void setAssignedEmployees(List<Employee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    public boolean assignEmployee(Employee employee){
        if (assignedEmployees.stream().anyMatch(o -> o.getId() == employee.getId()))
            return false;

        return this.assignedEmployees.add(employee);
    }

    public boolean removeEmployeeAssignment(Employee employee){
        if (assignedEmployees.stream().anyMatch(o ->  o.getId() == employee.getId()))
            return this.assignedEmployees.remove(employee);

        return false;
    }
}
