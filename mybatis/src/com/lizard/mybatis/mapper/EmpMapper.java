package com.lizard.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.lizard.mybatis.model.Employee;

public interface EmpMapper {
	Employee getOne(int id);

	// 测试mybatis增删改查

	Employee getEmpById(int id);

	int saveOrUpdateEmp(Employee emp);

	int addEmp(Employee emp);

	boolean removeEmp(int id);

	// 测试mybatis传参

	Employee getEmpByIdAndName(@Param("id") int id, @Param("lastName") String lastName);

	Employee getEmpByIdAndName(Employee emp);

	Employee getEmpByIdAndName(Map<String, Object> map);

	// 测试返回结果集封装

	List<Employee> getEmpsByLastNameLike(String lastLikeFiex);

	Map<String, Object> getEmpMapById(int id);

	@MapKey("id")
	Map<Integer, Employee> getEmpPOJOMapByLastName(String lastLikeFiex);

	// 测试多表关联嵌套查询、分步查询

	Employee getEmpAndDeptName(int id);

	Employee getEmpAndDeptStep(int id);

	List<Employee> getEmpsByDeptIdStep(int dept);

	List<Employee> getEmpsByDeptIdAndNameStep(int deptId, int mngId);

	// 测试动态SQL

	List<Employee> getEmpByCondition(Map<String, Object> map);

	List<Employee> getEmpByConditionChoose(Map<String, Object> map);

	int updateEmp(Employee emp);

	List<Employee> getEmpByConditionForEach(int[] ids);

	int batchInsert(List<Employee> emps);
}
