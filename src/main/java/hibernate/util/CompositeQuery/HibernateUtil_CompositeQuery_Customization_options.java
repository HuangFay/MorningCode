package hibernate.util.CompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query;

import com.morning.cust.model.CustVO;

public class HibernateUtil_CompositeQuery_Customization_options {

    public static Predicate getPredicate(CriteriaBuilder builder, Root<CustVO> root, String columnName, String value) {
        Predicate predicate = null;

        if ("custId".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } else if ("custName".equals(columnName) || "custPrice".equals(columnName) || "custStatus".equals(columnName)) {
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        }

        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<CustVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<CustVO> list = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<CustVO> criteriaQuery = builder.createQuery(CustVO.class);
            Root<CustVO> root = criteriaQuery.from(CustVO.class);

            List<Predicate> predicateList = new ArrayList<>();
            Set<String> keys = map.keySet();
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    predicateList.add(getPredicate(builder, root, key, value.trim()));
                }
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("custId")));

            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null)
                tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return list;
    }
}
