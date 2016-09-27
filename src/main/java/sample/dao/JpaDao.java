/**
 *
 */
package sample.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;

import sample.domain.BaseEntity;

/**
 * Fully generic singleton DAO for JPA 2.
 */
public interface JpaDao {

	/**
	 * Returns {@link EntityManager} to perform, i.e. JPQL and native queries.
	 *
	 * @return {@link EntityManager}
	 */
	public EntityManager getEntityManager();

	/**
	 * Method is used to retrieve a list of entities filtered with specified
	 * predicateMap and sorted in ascending order by field that is specified as
	 * "id"-field.
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param predicateMap
	 *            - HashMap with special keys to access objects needed to use a
	 *            predicate. Map keys are:
	 *
	 *            criteriaQuery : CriteriaQuery<T>
	 *
	 *            entityRoot : Root<T>
	 *
	 *            predicateList : List<Predicate>
	 *
	 * @return generic list of entities.
	 */
	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Map<String, Object> predicateMap);

	/**
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param field
	 *            - name of the POJO field to be filtered.
	 * @param value
	 *            - value of the field to be filtered.
	 * @return predicateMap with list of Predicates. It is a HashMap with
	 *         special keys to access objects needed to use a predicate. Map
	 *         keys are:
	 *
	 *         criteriaQuery : CriteriaQuery<T>
	 *
	 *         entityRoot : Root<T>
	 *
	 *         predicateList : List<Predicate>
	 */
	public <T extends BaseEntity, V> Map<String, Object> getEqualPredicateMap(Class<T> type, String field, V value);

	/**
	 *
	 * @return Single line method that returns CriteriaBuilder from
	 *         EntityManager
	 */
	public CriteriaBuilder getCriteriaBuilder();

	/**
	 * Method adds new equal Predicate to existing HashMap with Predicate list.
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param field
	 *            - name of the POJO field to be filtered.
	 * @param value
	 *            - value of the field to be filtered.
	 * @param resultMap
	 *            - HashMap where new Predicate will be added. It is a HashMap
	 *            with special keys to access objects needed to use a predicate.
	 *            Map keys are:
	 *
	 *            criteriaQuery : CriteriaQuery<T>
	 *
	 *            entityRoot : Root<T>
	 *
	 *            predicateList : List<Predicate>
	 *
	 */
	public <T extends BaseEntity, V> void addEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap);

	/**
	 * The same as getEqualPredicateMap() method but returns not equal Predicate
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param field
	 *            - name of the POJO field to be filtered.
	 * @param value
	 *            - value of the field to be filtered.
	 * @return predicateMap with list of Predicates. It is a HashMap with
	 *         special keys to access objects needed to use a predicate. Map
	 *         keys are:
	 *
	 *         criteriaQuery : CriteriaQuery<T>
	 *
	 *         entityRoot : Root<T>
	 *
	 *         predicateList : List<Predicate>
	 */
	public <T extends BaseEntity, V> Map<String, Object> getNotEqualPredicateMap(Class<T> type, String field, V value);

	/**
	 * The same as addEqualPredicateToMap but adds not equal Predicate.
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param field
	 *            - name of the POJO field to be filtered.
	 * @param value
	 *            - value of the field to be filtered.
	 * @param resultMap
	 *            - HashMap where new Predicate will be added. It is a HashMap
	 *            with special keys to access objects needed to use a predicate.
	 *            Map keys are:
	 *
	 *            criteriaQuery : CriteriaQuery<T>
	 *
	 *            entityRoot : Root<T>
	 *
	 *            predicateList : List<Predicate>
	 */
	public <T extends BaseEntity, V> void addNotEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap);

	/**
	 * This method is similar to findAllWithRestrictions() method but
	 * additionally an javax.persistence.criteria.Order can be specified other
	 * than ascending one by field that is specified as "id".
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param order
	 *            - javax.persistence.criteria.Order - order to sort list of
	 *            entities.
	 * @param predicateMap
	 *            - HashMap with special keys to access objects needed to use a
	 *            predicate. Map keys are:
	 *
	 *            criteriaQuery : CriteriaQuery<T>
	 *
	 *            entityRoot : Root<T>
	 *
	 *            predicateList : List<Predicate>
	 * @return generic list of entities.
	 */
	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Order order,
			Map<String, Object> predicateMap);

	/**
	 * Simple wrapper method to get entity with EntityManager by its class and
	 * id.
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param id
	 *            - entity identifier in database.
	 * @return returns entity found, equals to EntityManager find() method.
	 */
	public <T extends BaseEntity> T findById(Class<T> type, Long id);

	/**
	 * Method retrieves list of entities using JPA 2 API ordered by id field in
	 * ascending order.
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @return list of entities found in ascending order by "id"-field.
	 */
	public <T extends BaseEntity> List<T> findAll(Class<T> type);

	/**
	 * Similar to method findAll() but one can specify a different
	 * javax.persistence.criteria.Order for list of entities.
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param order
	 *            - javax.persistence.criteria.Order
	 * @return list of entities found in javax.persistence.criteria.Order
	 *         specified.
	 */
	public <T extends BaseEntity> List<T> findAll(Class<T> type, Order order);

	/**
	 * Simple wrapper method to EntityManager persist() method. Only new,
	 * unsaved POJO should be specified.
	 *
	 * @param entity
	 *            - POJO to be persisted to database.
	 * @return modified POJO after persisting to database.
	 */
	public <T extends BaseEntity> T save(T entity);

	/**
	 * Simple wrapper method to EntityManager merge() method.
	 *
	 * @param entity
	 *            - POJO to be updated. Only POJOs from persistence context
	 *            should be passed.
	 * @return modified POJO after persisting to database.
	 */
	public <T extends BaseEntity> T update(T entity);

	/**
	 * Simple wrapper method for EntityManager remove() method.
	 *
	 * @param entity
	 *            - POJO to be deleted. Only POJOs from persistence context
	 *            should be passed.
	 */
	public <T extends BaseEntity> void delete(T entity);

	/**
	 * Method to check if the given identifier exists in database for the class
	 * specified.
	 *
	 * @param type
	 *            - class to be found, for example, Customer.class
	 * @param id
	 *            - entity identifier in database.
	 * @return boolean value if entity has been found in database and is not
	 *         null.
	 */
	public <T extends BaseEntity> boolean exists(Class<T> type, Long id);

	/**
	 * Simple wrapper method for EntityManager refresh() method.
	 *
	 * @param entity
	 *            - POJO to be refreshed from database.
	 */
	public <T> void refresh(T entity);

	/**
	 * Simple wrapper method for EntityManager flush() method.
	 */
	public void flush();
}
