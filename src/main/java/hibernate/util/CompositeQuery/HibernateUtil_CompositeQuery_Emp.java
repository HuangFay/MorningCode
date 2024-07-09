
//複合式查詢 員工版

package hibernate.util.CompositeQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.morning.emp.model.EmpVO;

public class HibernateUtil_CompositeQuery_Emp {
//Predicate 是 Java 中用來定義條件判斷的函數接口
//根據給定的 columnName 和 value，使用 CriteriaBuilder 創建並返回一個適當的 Predicate 對象
	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder,Root<EmpVO> root,String columnName,String value) {
		Predicate predicate = null;
		
		
		
		if("empId".equals(columnName)) {
			predicate = builder.equal(root.get(columnName),Integer.valueOf(value));
			
		}else if("empName".equals(columnName)) {
			predicate =builder.like(root.get(columnName),"%"+value+"%");
		}
		return predicate;
	}
	
	
	
	
//	未檢查的類型轉換，可以使用這個註釋來抑制警告
	@SuppressWarnings("unchecked")
	public static List<EmpVO> getAllC(Map<String, String[]> map,Session session){
		
		Transaction tx = session.beginTransaction();
		List<EmpVO> list = null;
		try {
			 //創建CriteriaBuilder 根據21行
			CriteriaBuilder builder = session.getCriteriaBuilder();
			//創建CriteriaQuery,指定查詢結果是EmpVO實體
			CriteriaQuery<EmpVO> criteriaQuery =builder.createQuery(EmpVO.class);
			//創建Root將 所需要的實體放入
			Root<EmpVO> root =criteriaQuery.from(EmpVO.class);
			
			List<Predicate> predicateList =new ArrayList<Predicate>();
			
			Set<String> keys =map.keySet();
			int count=0;
			
//			遍歷 map 中的所有的KEY值，動態生成查詢條件
			for(String key :keys) {
				String value =map.get(key)[0];
				if(value != null && value.trim().length() != 0 && !"action".equals(key)) {
					count++;
					predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
					System.out.println("有送出查詢資料的欄位數count = " + count);
				}
			}
			System.out.println("predicateList.size()="+predicateList.size());
			criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
			criteriaQuery.orderBy(builder.asc(root.get("empId")));
			
			Query query=session.createQuery(criteriaQuery);//javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面
			list=query.getResultList();
			
			tx.commit();
		}catch(RuntimeException ex) {
			if(tx != null) {
				tx.rollback();
				throw ex;
			}
		}finally {
			session.close();
		}
		return list;
	}
}
