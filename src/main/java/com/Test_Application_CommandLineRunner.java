package com;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import com.morning.meals.model.MealsRepository;
import com.morning.meals.model.MealsService;
import com.morning.meals.model.MealsVO;



//@SpringBootApplication
public class Test_Application_CommandLineRunner implements CommandLineRunner {
    
//	@Autowired
//	EmpRepository repository ;
	
	@Autowired
	MealsRepository repository ;
	
	@Autowired
	MealsService mealsSvc;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public static void main(String[] args) {
        SpringApplication.run(Test_Application_CommandLineRunner.class);
    }

    @Override
    public void run(String...args) throws Exception {
//    	Optional<MealsVO> optional = repository.findById(4);
//		MealsVO mealsVO = optional.get();
//		
//		Integer mealsnumber = mealsSvc.getmealsnumber();
//		for(int i=1; i <= mealsnumber ; i++) {
//
//			Double mealsavg = mealsSvc.getavgscore(i);
//			System.out.println(mealsavg);
//			
//			if(mealsavg == null) {
//				System.out.println("123");
//				continue;
//			}
//			System.out.println(i);
//			mealsSvc.updateMealsScore(mealsavg, i); // 更新 meals 實

    	//● 新增
//		DeptVO deptVO = new DeptVO(); // 部門POJO
//		deptVO.setDeptno(1);

    	
//    	EmpVO empVO1 = new EmpVO();
//    	empVO1.setEmpName("吳永志");
//    	empVO1.setEmpAccount("WuGod");
//    	empVO1.setEmpPassword("67676ss");
//    	empVO1.setEmpPhone("0977333333");
//    	empVO1.setEmpAddress("桃園市中壢區復興路46號");
//    	empVO1.setEmpEmail("WuGod@gmail.com");
//    	empVO1.setEmpHiredate(java.sql.Date.valueOf("2005-01-01"));
//    	empVO1.setEmpStatus((byte)1);
//    	repository.save(empVO1);
    	
    	


		//● 修改  多主鍵就是修改
//		EmpVO empVO2 = new EmpVO();
//		empVO2.setEmpno(7001);
//		empVO2.setEname("吳永志2");
//		empVO2.setJob("MANAGER2");
//		empVO2.setHiredate(java.sql.Date.valueOf("2002-01-01"));
//		empVO2.setSal(new Double(20000));
//		empVO2.setComm(new Double(200));
//		empVO2.setDeptVO(deptVO);
//		repository.save(empVO2);
		
		//● 刪除   //●●● --> EmployeeRepository.java 內自訂的刪除方法
//		repository.deleteByEmpId(4);
		
		//● 刪除   //XXX --> Repository內建的刪除方法目前無法使用，因為有@ManyToOne
		//System.out.println("--------------------------------");
		//repository.deleteById(7001);      
		//System.out.println("--------------------------------");

    	//● 查詢-findByPrimaryKey (多方emp2.hbm.xml必須設為lazy="false")(優!)
//    	Optional<EmpVO> optional = repository.findById(1);
//		EmpVO empVO3 = optional.get();
//		System.out.print(empVO3.getEmpId() + ",");
//		System.out.print(empVO3.getEmpName() + ",");
//		System.out.print(empVO3.getEmpAccount() + ",");
//		System.out.print(empVO3.getEmpPassword() + ",");
//		System.out.print(empVO3.getEmpPhone() + ",");
//		System.out.print(empVO3.getEmpAddress() + ",");
//		System.out.print(empVO3.getEmpEmail() + ",");
//		System.out.print(empVO3.getEmpHiredate() + ",");
//		System.out.print(empVO3.getEmpStatus() + ",");
//		// 注意以下三行的寫法 (優!)
//		System.out.print(empVO3.getDeptVO().getDeptno() + ",");
//		System.out.print(empVO3.getDeptVO().getDname() + ",");
//		System.out.print(empVO3.getDeptVO().getLoc());
//		System.out.println("\n---------------------");
      
    	
		//● 查詢-getAll (多方emp2.hbm.xml必須設為lazy="false")(優!)
//    	List<EmpVO> list = repository.findAll();
//		for (EmpVO aEmp : list) {
//			System.out.print(aEmp.getEmpId() + ",");
//			System.out.print(aEmp.getEmpName() + ",");
//			System.out.print(aEmp.getEmpAccount() + ",");
//			System.out.print(aEmp.getEmpPassword() + ",");
//			System.out.print(aEmp.getEmpPhone() + ",");
//			System.out.print(aEmp.getEmpAddress() + ",");
//			System.out.print(aEmp.getEmpEmail() + ",");
//			System.out.print(aEmp.getEmpHiredate() + ",");
//			System.out.print(aEmp.getEmpStatus() + ",");
			// 注意以下三行的寫法 (優!)
//			System.out.print(aEmp.getDeptVO().getDeptno() + ",");
//			System.out.print(aEmp.getDeptVO().getDname() + ",");
//			System.out.print(aEmp.getDeptVO().getLoc());
//			System.out.println();
//		}


		//● 複合查詢-getAll(map) 配合 req.getParameterMap()方法 回傳 java.util.Map<java.lang.String,java.lang.String[]> 之測試
//		Map<String, String[]> map = new TreeMap<String, String[]>();
//		map.put("empno", new String[] { "7001" });
//		map.put("ename", new String[] { "KING" });
//		map.put("job", new String[] { "PRESIDENT" });
//		map.put("hiredate", new String[] { "1981-11-17" });
//		map.put("sal", new String[] { "5000.5" });
//		map.put("comm", new String[] { "0.0" });
//		map.put("deptno", new String[] { "1" });
//		
//		List<EmpVO> list2 = HibernateUtil_CompositeQuery_Emp3.getAllC(map,sessionFactory.openSession());
//		for (EmpVO aEmp : list2) {
//			System.out.print(aEmp.getEmpno() + ",");
//			System.out.print(aEmp.getEname() + ",");
//			System.out.print(aEmp.getJob() + ",");
//			System.out.print(aEmp.getHiredate() + ",");
//			System.out.print(aEmp.getSal() + ",");
//			System.out.print(aEmp.getComm() + ",");
//			// 注意以下三行的寫法 (優!)
//			System.out.print(aEmp.getDeptVO().getDeptno() + ",");
//			System.out.print(aEmp.getDeptVO().getDname() + ",");
//			System.out.print(aEmp.getDeptVO().getLoc());
//			System.out.println();
//		}
		

		//● (自訂)條件查詢
//		List<EmpVO> list3 = repository.findByOthers(7001,"%K%",java.sql.Date.valueOf("1981-11-17"));
//		if(!list3.isEmpty()) {
//			for (EmpVO aEmp : list3) {
//				System.out.print(aEmp.getEmpno() + ",");
//				System.out.print(aEmp.getEname() + ",");
//				System.out.print(aEmp.getJob() + ",");
//				System.out.print(aEmp.getHiredate() + ",");
//				System.out.print(aEmp.getSal() + ",");
//				System.out.print(aEmp.getComm() + ",");
//				// 注意以下三行的寫法 (優!)
//				System.out.print(aEmp.getDeptVO().getDeptno() + ",");
//				System.out.print(aEmp.getDeptVO().getDname() + ",");
//				System.out.print(aEmp.getDeptVO().getLoc());
//				System.out.println();
//			}
//		} else System.out.print("查無資料\n");
//		System.out.println("--------------------------------");

    }
}
