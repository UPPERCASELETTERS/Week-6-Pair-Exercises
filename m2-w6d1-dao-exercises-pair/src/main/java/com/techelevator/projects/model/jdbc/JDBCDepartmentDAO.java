package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		ArrayList<Department> departments = new ArrayList<>();
		String sqlFindAllDepartments = "SELECT department_id, name FROM department";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindAllDepartments);
		while(results.next()) {
			Department theDepartment = mapRowToDepartment(results);
			departments.add(theDepartment);
		} return departments;	
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		ArrayList<Department> departments = new ArrayList<>();
		String sqlFindDepartmentsByName = "SELECT department_id, name FROM department WHERE name ILIKE ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindDepartmentsByName, "%" + nameSearch + "%");
		while(results.next()) {
			Department theDepartment = mapRowToDepartment(results);
			departments.add(theDepartment);
		} return departments;
	}
	

	@Override
	public void updateDepartmentName(Long departmentId, String newDepartmentName) {
		String updateDepartmentName = "UPDATE department SET name =? WHERE department_id =?";
		jdbcTemplate.update(updateDepartmentName, newDepartmentName, departmentId);
	}

	@Override
	public Department createDepartment(String departmentName) {
		Department newDept = new Department();
		newDept.setName(departmentName);
		String sqlCreateDepartment = "INSERT INTO department (name) VALUES (?) RETURNING department_id";
		newDept.setId(jdbcTemplate.queryForObject(sqlCreateDepartment, Long.class, departmentName));
		return newDept;
	}
			

	@Override
	public Department getDepartmentById(Long id) {
		Department theDepartment = null;
		String sqlSelectDepartment = "SELECT department_id, name FROM department WHERE department_id =?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectDepartment, id);
		if(results.next()) {
			theDepartment = mapRowToDepartment(results);
		} return theDepartment;
	}
			
	private Department mapRowToDepartment(SqlRowSet results) {
		Department theDepartment;
		theDepartment = new Department();
		theDepartment.setId(results.getLong("department_id"));
		theDepartment.setName(results.getString("name"));
		
		return theDepartment;
	}

}
