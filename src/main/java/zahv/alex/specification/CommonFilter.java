package zahv.alex.specification;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author azakhvalinskiy
 * @date 14.08.17
 */
public class CommonFilter<T> extends AbstractFilter<T> {

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        criteriaQuery.distinct(true);
        return getCommonPredicate(root, criteriaQuery, criteriaBuilder);
    }
}
