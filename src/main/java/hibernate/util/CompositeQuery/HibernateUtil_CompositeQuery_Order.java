package hibernate.util.CompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query;
import java.util.*;

import com.morning.mem.model.MemVO;
import com.morning.order.model.OrderVO;

public class HibernateUtil_CompositeQuery_Order {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<OrderVO> root, String columnName, String value) {
        Predicate predicate = null;
        switch (columnName) {
            case "ordStatus":
                predicate = builder.equal(root.get("ordStatus"), Byte.valueOf(value));
                break;
            case "ordDateStart":
                predicate = builder.greaterThanOrEqualTo(root.get("ordBuilddate"), java.sql.Timestamp.valueOf(value + " 00:00:00"));
                break;
            case "ordDateEnd":
                predicate = builder.lessThanOrEqualTo(root.get("ordBuilddate"), java.sql.Timestamp.valueOf(value + " 23:59:59"));
                break;
        }
        if (predicate == null) {
            predicate = builder.conjunction(); // 建立true表示該查詢條件始終成立。
        }
        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<OrderVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<OrderVO> list = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<OrderVO> criteriaQuery = builder.createQuery(OrderVO.class);
            Root<OrderVO> root = criteriaQuery.from(OrderVO.class);

            List<Predicate> predicateList = new ArrayList<>();

            Set<String> keys = map.keySet();
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && !value.trim().isEmpty() && !"action".equals(key)) {
                    Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, value.trim());
                    if (predicate != null) {
                        predicateList.add(predicate);
                    }
                }
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("ordId")));
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        } finally {
            session.close();
        }

        return list;
    }
}
