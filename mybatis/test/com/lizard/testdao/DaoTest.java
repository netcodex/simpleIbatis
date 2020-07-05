package com.lizard.testdao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.net.SyslogAppender;

import com.lizard.mybatis.mapper.DeptMapper;
import com.lizard.mybatis.mapper.EmpMapper;
import com.lizard.mybatis.model.Department;
import com.lizard.mybatis.model.Employee;

import junit.framework.TestCase;

public class DaoTest extends TestCase {

	public SqlSessionFactory getSqlSessionFactory() {
		String resource = "mybatis-conf.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	public SqlSessionFactory getSqlSessionFactoryByResource(String resource) {
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	public void test() throws IOException {
		String resource = "mybatis-conf.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		SqlSession session = sqlSessionFactory.openSession();
		try {
			Employee emp = session.selectOne("selectEmp", 101);
			System.out.println(emp);
			// Empolyee [id=0, lastName=null, email=NKOCHHAR, salary=17000.0,
			// hiredate=Fri Apr 03 00:00:00 CST 1992]
		} finally {
			session.close();
		}
	}

	/**
	 * 测试接口绑定映射文件
	 * 
	 * @throws IOException
	 */
	public void testInterfaceBindMapper() throws IOException {
		String resource = "mybatis-conf.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		SqlSession session = sqlSessionFactory.openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			System.out.println(empMapper);
			Employee emp = empMapper.getOne(101);
			System.out.println(emp);
		} finally {
			session.close();
		}
	}

	/**
	 * 测试根据不同数据库厂商标识执行不同SQL语句
	 * 
	 * @throws IOException
	 */
	public void testDatabaseIdProvider() throws IOException {
		String resource = "mybatis-conf-dbprvd.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		SqlSession session = sqlSessionFactory.openSession();
		String env = session.getConfiguration().getEnvironment().getId();
		String dbid = session.getConfiguration().getDatabaseId();
		// 注意给databaseid取别名时，名称一定要正确，不然这里sqlSession不会绑定到对应的databaseid上，而且还会导致接口绑定SQL映射文件失败
		System.out.println("数据库Environment是：" + env + "；DatabaseId是：" + dbid);
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			System.out.println(empMapper);
			Employee emp = null;
			if ("mysql".equals(dbid)) {
				emp = empMapper.getOne(101);
			}
			if ("orcl".equals(dbid)) {
				emp = empMapper.getEmpById(203);
			}
			System.out.println(emp);
		} finally {
			session.close();
		}
	}

	/**
	 * 测试增删改查
	 * 
	 * @throws IOException
	 */

	public void testCurd() {
		SqlSession session = getSqlSessionFactory().openSession();
		try {
			// 查询
			Employee emp = session.selectOne("getEmpById", 101);
			System.out.println(emp);
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp1 = empMapper.getEmpById(103);
			System.out.println(emp1);
			// 增加
			// Empolyee emp2 = new Empolyee("zhangsan", "zhangs@163.com", 5600);
			// int addEmp = empMapper.addEmp(emp2);
			// System.out.println(addEmp);

			// 修改
			// Empolyee emp3 = new Empolyee("zhangsan", "lisi@163.com", 5800);
			// emp3.setId(208);
			// int updateEmp = empMapper.saveOrUpdateEmp(emp3);
			// System.out.println(updateEmp);

			// 删除
			boolean removeEmp = empMapper.removeEmp(221);
			System.out.println(removeEmp);

			session.commit();
		} finally {
			session.close();
		}
	}

	/**
	 * 测试获取自增长主键
	 */
	public void testKeyGenerator() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp2 = new Employee("zhangsan", "zhangs@163.com", 5600.00);
			emp2.setHiredate(new Date());
			int addEmp = empMapper.addEmp(emp2);
			System.out.println(addEmp);
			// 获取添加完后主键，注意insert语句要插入当前主键id，不然始终比插入后的id小1
			System.out.println(emp2.getId());

			session.commit();
		} finally {
			session.close();
		}
	}

	/**
	 * 测试参数传递
	 */
	public void testParam() {
		SqlSession session = getSqlSessionFactory().openSession();
		try {
			// 查询
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp1 = empMapper.getEmpByIdAndName(122, "Kaufling");
			System.out.println(emp1);

			Employee emp = new Employee();
			emp.setId(122);
			emp.setLastName("Kaufling");
			Employee emp2 = empMapper.getEmpByIdAndName(emp);
			System.out.println(emp2);

			Map<String, Object> map = new HashMap<>();
			map.put("id", 122);
			map.put("lastName", "Kaufling");
			Employee emp3 = empMapper.getEmpByIdAndName(map);
			System.out.println(emp3);
		} finally {
			session.close();
		}
	}
	
	/*
	 * 测试返回结果集包装
	 */
	
	public void testResultMapWrap() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			// 结果集封装成list
			List<Employee> list = empMapper.getEmpsByLastNameLike("%ra%");
			// list.forEach((e) -> System.out.println(e));
			
			// 结果集封装成map
			Map<String, Object> map = empMapper.getEmpMapById(202);
			// System.out.println(map);
			
			// 结果集封装成map，并指定Key关联的字段
			Map<Integer, Employee> map2 = empMapper.getEmpPOJOMapByLastName("%ra%");
			System.out.println(map2);
//			list2.forEach((key, value) -> System.out.println(key + ": " + value.toString()));

		} finally {
			session.close();
		}
	}
	
	/**
	 * 测试结果集封装，resultMap的使用
	 */
	public void testResultSetsWrap() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp = empMapper.getOne(202);
			System.out.println(emp);
		} finally {
			session.close();
		}
	}
	
	/**
	 * 测试resultMap实现关联查询
	 */
	
	public void testAssociateQuery() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp = empMapper.getEmpAndDeptName(110);
			Department dept = emp.getDepartment();
			System.out.println(emp);
			System.out.println(dept);
		} finally {
			session.close();
		}
	}
	
	/**
	 * 测试resultMap实现关联分步查询
	 */
	
	public void testAssociateQueryStep() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp = empMapper.getEmpAndDeptStep(110);
			System.out.println(emp.getLastName());
			 Department dept = emp.getDepartment();
			 System.out.println(dept);
		} finally {
			session.close();
		}
	}
	
	/**
	 * 测试resultMap实现集合的嵌套多对一查询
	 */
	
	public void testCollectionQuery() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			DeptMapper deptMapper = session.getMapper(DeptMapper.class);
			Department dept = deptMapper.getEmpsByDeptId(100);
			System.out.println("dept: " + dept);
			System.out.println(dept.getEmps());
		} finally {
			session.close();
		}
	}
	
	/**
	 * 测试resultMap实现集合的嵌套多对一查询
	 */
	
	public void testCollectionQueryStep() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			DeptMapper deptMapper = session.getMapper(DeptMapper.class);
			Department dept = deptMapper.getEmpsByDeptIdStep(100);
			System.out.println("dept: " + dept.getDeptName());
			System.out.println(dept.getEmps());
		} finally {
			session.close();
		}
	}
	
	/**
	 * 测试条件查询动态SQL
	 */
	
	public void testDynamicSQLIf(){
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Map<String,Object> map = new HashMap<>();
			map.put("id", 110);
			map.put("lastName", "%en%");
			List<Employee> emps = empMapper.getEmpByCondition(map);
			System.out.println(emps);
		} finally {
			session.close();
		}
	}
	
	public void testDynamicSQLChoose(){
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Map<String,Object> map = new HashMap<>();
			map.put("id", 110);
			map.put("lastName", "%en%");
			List<Employee> emps = empMapper.getEmpByConditionChoose(map);
			System.out.println(emps);
		} finally {
			session.close();
		}
	}
	
	
	public void testDynamicSQLSet() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp = new Employee("Gietz", null, 8600.00);//Gietz
			emp.setId(206);
			int i = empMapper.updateEmp(emp);
			System.out.println(i);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	public void testDynamicSQLForEach() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			List<Employee> list = empMapper.getEmpByConditionForEach(new int[]{110,111,112});
			System.out.println(list);
		} finally {
			session.close();
		}
	}
	
	public void testDynamicSQLForEachBatchInsert() {
		SqlSession session = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml").openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp1 = new Employee("jack", "jack@163.com", 4500.00);
			emp1.setHiredate(new Date());
			Employee emp2 = new Employee("rose", "rose@163.com", 3500.00);
			emp2.setHiredate(new Date());
			int i = empMapper.batchInsert(Arrays.asList(emp1, emp2));
			System.out.println(i);
			session.commit();
		} finally {
			session.close();
		}
	}
	
	/**
	 * 测试mybatis的缓存机制
	 * 
	 */
	
	/**
	 * 测试结果集封装，resultMap的使用
	 */
	public void testSessionCache() {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryByResource("mybatis-conf-dbprvd.xml");
		SqlSession session = sqlSessionFactory.openSession();
		try {
			EmpMapper empMapper = session.getMapper(EmpMapper.class);
			Employee emp = empMapper.getEmpById(202);
			System.out.println(emp);
			
			session.clearCache();
			session.close();
			
			SqlSession session2 = sqlSessionFactory.openSession();
			
			EmpMapper empMapper2 = session2.getMapper(EmpMapper.class);
			Employee emp2 = empMapper2.getEmpById(202);
			System.out.println(emp2);
			
			session2.close();
		} finally {
			session.close();
		}
	}
}
