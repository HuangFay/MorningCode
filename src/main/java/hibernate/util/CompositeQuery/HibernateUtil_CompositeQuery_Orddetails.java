package hibernate.util.CompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.morning.ordd.model.OrddVO;

public class HibernateUtil_CompositeQuery_Orddetails {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<OrddVO> root, String columnName, String value) {
        Predicate predicate = null;

        if ("orddId".equals(columnName) || "ordId".equals(columnName) || "mealsId".equals(columnName) ||
            "orddMealsQuantity".equals(columnName) || "orddMealsAmount".equals(columnName) ||
            "orddMealsStatus".equals(columnName) || "mealsCommentId".equals(columnName) ||
            "mealsScore".equals(columnName) || "mealsStatus".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } else if ("mealsContent".equals(columnName)) {
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        } else if ("mealsTime".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), java.sql.Timestamp.valueOf(value));
        }

        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<OrddVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<OrddVO> list = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<OrddVO> criteriaQuery = builder.createQuery(OrddVO.class);
            Root<OrddVO> root = criteriaQuery.from(OrddVO.class);

            List<Predicate> predicateList = new ArrayList<>();

            Set<String> keys = map.keySet();
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
                }
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("orddId")));

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
