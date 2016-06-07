/**
 *
 */
package sample.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;

import sample.domain.BaseEntity;

/**
 * Fully generic DAO for Entity Manager interface
 */
public interface JpaDao {

	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Map<String, Object> predicateMap);

	public <T extends BaseEntity, V> Map<String, Object> getEqualPredicateMap(Class<T> type, String field, V value);

	public CriteriaBuilder getCriteriaBuilder();

	public <T extends BaseEntity, V> void addEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap);

	public <T extends BaseEntity, V> Map<String, Object> getNotEqualPredicateMap(Class<T> type, String field, V value);

	public <T extends BaseEntity, V> void addNotEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap);

	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Order order,
			Map<String, Object> predicateMap);

	public <T extends BaseEntity> T findById(Class<T> type, Long id);

	public <T extends BaseEntity> List<T> findAll(Class<T> type);

	public <T extends BaseEntity> List<T> findAll(Class<T> type, Order order);

	public <T extends BaseEntity> T save(T entity);

	public <T extends BaseEntity> T update(T entity);

	public <T extends BaseEntity> void delete(T entity);

	public <T extends BaseEntity> boolean exists(Class<T> type, Long id);

	public <T> void refresh(T entity);

	public void flush();
}
