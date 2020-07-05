package com.lizard.mybatis.mapper;

import com.lizard.mybatis.model.Department;

public interface DeptMapper {

	Department getDeptById(int id);

	Department getEmpsByDeptId(int id);

	Department getEmpsByDeptIdStep(int deptId);
}
