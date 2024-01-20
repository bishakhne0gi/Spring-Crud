package com.crud.serverside.controller;

import com.crud.serverside.exception.ResourceNotFoundException;
import com.crud.serverside.model.Employee;
import com.crud.serverside.model.Project;
import com.crud.serverside.repository.EmployeeRepository;
import com.crud.serverside.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class ProjectController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Get All Projects
    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Create New Project
    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        for (Employee employee : project.getAssignedEmployees()){
            Optional<Employee> employeeInDb = employeeRepository.findById(employee.getId());
            if (employeeInDb.isEmpty() || !employeeInDb.get().equals(employee))
                return ResponseEntity.badRequest().body(project);
        }
        return ResponseEntity.ok(projectRepository.save(project));
    }

    // Get Project by ID
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exist with id:" + id));

        return ResponseEntity.ok(project);
    }

    // Update Project by ID
    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exist with id:" + id));

        for (Employee employee : project.getAssignedEmployees()){
            Optional<Employee> employeeInDb = employeeRepository.findById(employee.getId());
            if (employeeInDb.isEmpty() || !employeeInDb.get().equals(employee))
                return ResponseEntity.badRequest().body(project);
        }

        project.setProjectName(projectDetails.getProjectName());
        project.setProjectDescription(project.getProjectDescription());
        project.setAssignedEmployees(projectDetails.getAssignedEmployees());

        Project updateProject = projectRepository.save(project);

        return ResponseEntity.ok(updateProject);
    }

    // Delete Project by ID
    @DeleteMapping("/projects/{id}")
    public Map<String, Boolean> deleteProject(@PathVariable Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exist with id:" + id));
        projectRepository.delete(project);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return response;
    }

    // Add Employee to Project by ID
    @PutMapping("/projects/{projectId}/employees")
    public ResponseEntity<Project> assignEmployeeToProject(@PathVariable Long projectId, @RequestBody Map<String, Long> data) {
        long employeeId = data.containsKey("employeeId") ? data.get("employeeId") : -1;

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exist with id:" + projectId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with id:" + employeeId));

        boolean assigned = project.assignEmployee(employee);
        Project updatedProject = projectRepository.save(project);
        if (assigned)
            return ResponseEntity.ok(updatedProject);
        else
            return ResponseEntity.badRequest().body(updatedProject);
    }

    // Remove Employee from Project
    @DeleteMapping("/projects/{projectId}/employees/{employeeId}")
    public ResponseEntity<Project> removeEmployeeFromProject(@PathVariable Long projectId, @PathVariable Long employeeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project does not exist with id:" + projectId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with id:" + employeeId));

        boolean removed = project.removeEmployeeAssignment(employee);
        Project updatedProject = projectRepository.save(project);
        if (removed)
            return ResponseEntity.ok(updatedProject);
        else
            return ResponseEntity.badRequest().body(updatedProject);
    }
}
