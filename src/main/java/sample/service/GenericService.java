/**
 *
 */
package sample.service;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;

import sample.domain.BaseEntity;
import sample.exceptions.BusinessLogicException;

/**
 * Interface for injecting service layer common functionality implementation
 * class. Its methods replicate JPA DAO interface. Specific service class
 * implementations can override some default methods like save(), update(),
 * delete() etc.
 *
 * Methods that may perform checks of business logic before making changes to
 * database raise BusinessLogicException
 */
public interface GenericService {

	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Map<String, Object> predicateMap);

	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Order order,
			Map<String, Object> predicateMap);

	public <T extends BaseEntity, V> Map<String, Object> getEqualPredicateMap(Class<T> type, String field, V value);

	public CriteriaBuilder getCriteriaBuilder();

	public <T extends BaseEntity, V> void addEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap);

	public <T extends BaseEntity, V> Map<String, Object> getNotEqualPredicateMap(Class<T> type, String field, V value);

	public <T extends BaseEntity, V> void addNotEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap);

	public <T extends BaseEntity> T findById(Class<T> type, Long id);

	public <T extends BaseEntity> List<T> findAll(Class<T> type);

	public <T extends BaseEntity> List<T> findAll(Class<T> type, Order order);

	public <T extends BaseEntity> T save(T entity) throws BusinessLogicException;

	public <T extends BaseEntity> T update(T entity) throws BusinessLogicException;

	public <T extends BaseEntity> void delete(T entity) throws BusinessLogicException;

	public <T extends BaseEntity> boolean exists(Class<T> type, Long id);

	public <T> void refresh(T entity);

}
