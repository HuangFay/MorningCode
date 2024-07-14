package hibernate.util.CompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery; //Hibernate 5.2 開始 取代原 org.hibernate.Criteria 介面
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面

import com.morning.mem.model.MemVO;
import com.morning.order.model.OrderVO;

public class HibernateUtil_CompositeQuery_Order {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<OrderVO> root, String columnName,
            String value) {

        Predicate predicate = null;

        if ("ord_id".equals(columnName) || "ord_amount".equals(columnName)) 
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        
        else if ("ord_type".equals(columnName) || "ord_status".equals(columnName) || "ord_payment_status".equals(columnName))
            predicate = builder.equal(root.get(columnName), Byte.valueOf(value));    
        
        else if ("ord_builddate".equals(columnName) || "ord_reserve_time".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), java.sql.Timestamp.valueOf(value));
        }
        
        else if ("mem_no".equals(columnName)) {
            MemVO memVO = new MemVO();
            memVO.setMemNo(Integer.valueOf(value));
            predicate = builder.equal(root.get("memVO"), memVO);
        }

        //在構建查詢條件時，可能會遇到沒有任何匹配條件的情況，即predicate可能為空值
        //Predicate是用於表示查詢條件的物件，概念如SQL查詢中的where子句
        if (predicate == null) {
            predicate = builder.conjunction();//建立true表示該查詢條件始終成立。
        }
        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<OrderVO> getAllC(Map<String, String[]> map, Session session) {

//                Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<OrderVO> list = null;
        try {
            // 【●創建 CriteriaBuilder】
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 【●創建 CriteriaQuery】
            CriteriaQuery<OrderVO> criteriaQuery = builder.createQuery(OrderVO.class);
            // 【●創建 Root】
            Root<OrderVO> root = criteriaQuery.from(OrderVO.class);

            List<Predicate> predicateList = new ArrayList<Predicate>();

            Set<String> keys = map.keySet();
            int count = 0;
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    count++;
                    Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, value.trim());
                    if (predicate != null) {
                        predicateList.add(predicate);
                    }
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }
            System.out.println("predicateList.size()=" + predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("ordId")));
            // 【●最後完成創建 javax.persistence.Query●】
            Query query = session.createQuery(criteriaQuery); // javax.persistence.Query; //Hibernate 5 開始 取代原
                                                                // org.hibernate.Query 介面
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null)
                tx.rollback();
            throw ex; // System.out.println(ex.getMessage());
        } finally {
            session.close();
            // HibernateUtil.getSessionFactory().close();
        }

        return list;
    }
}
