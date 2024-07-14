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

import com.morning.meal.model.MealVO;

public class HibernateUtil_CompositeQuery_Meal_customization_details {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<MealVO> root, String columnName, String value) {
        Predicate predicate = null;

        if ("customId".equals(columnName) || "orddId".equals(columnName) || "custId".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        }

        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<MealVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<MealVO> list = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MealVO> criteriaQuery = builder.createQuery(MealVO.class);
            Root<MealVO> root = criteriaQuery.from(MealVO.class);

            List<Predicate> predicateList = new ArrayList<>();

            Set<String> keys = map.keySet();
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
                }
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("customId")));

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
