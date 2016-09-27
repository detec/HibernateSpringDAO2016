
package sample.dao.jpaimpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import sample.dao.JpaDao;
import sample.domain.BaseEntity;

/**
 * Implementation of {@link JpaDao} interface
 *
 * @author Andrii Duplyk
 */
@Repository
public class JpaDaoImpl implements JpaDao {

	private static final Logger logger = LoggerFactory.getLogger(JpaDaoImpl.class);

	private EntityManager entityManager;

	private static final String CRITERIA_QUERY_CONSTANT = "criteriaQuery";

	private static final String PREDICATE_LIST_CONSTANT = "predicateList";

	private static final String ENTITY_ROOT_CONSTANT = "entityRoot";

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();

	}

	@Override
	public <T extends BaseEntity, V> Map<String, Object> getEqualPredicateMap(Class<T> type, String field, V value) {

		Map<String, Object> resultMap = new HashMap<>();

		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<T> metaT = m.entity(type);
		Root<T> entityRoot = criteriaQuery.from(metaT);

		Predicate predicate = criteriaBuilder.equal(entityRoot.get(field.toLowerCase()), value);

		resultMap.put(CRITERIA_QUERY_CONSTANT, criteriaQuery);
		resultMap.put(ENTITY_ROOT_CONSTANT, entityRoot);

		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(predicate);
		resultMap.put(PREDICATE_LIST_CONSTANT, predicateList);

		return resultMap;

	}

	@Override
	public <T extends BaseEntity, V> void addEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap) {

		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

		if (resultMap == null || resultMap.keySet().isEmpty()) {
			// initializing Map if it doesn't meet requirements
			resultMap = new HashMap<>();

			CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
			Metamodel m = getEntityManager().getMetamodel();
			EntityType<T> metaT = m.entity(type);
			Root<T> entityRoot = criteriaQuery.from(metaT);

			Predicate predicate = criteriaBuilder.equal(entityRoot.get(field.toLowerCase()), value);
			resultMap.put("predicateList", Arrays.asList(new Predicate[] { predicate }));

		} else {
			// construct and add a new predicate.
			@SuppressWarnings("unchecked")
			Root<T> entityRoot = (Root<T>) resultMap.get(ENTITY_ROOT_CONSTANT);
			Predicate predicate = criteriaBuilder.equal(entityRoot.get(field.toLowerCase()), value);

			@SuppressWarnings("unchecked")
			List<Predicate> predicateList = (List<Predicate>) resultMap.get(PREDICATE_LIST_CONSTANT);
			predicateList.add(predicate);
		}

	}

	@Override
	public <T extends BaseEntity, V> Map<String, Object> getNotEqualPredicateMap(Class<T> type, String field, V value) {

		Map<String, Object> resultMap = getEqualPredicateMap(type, field, value);
		@SuppressWarnings("unchecked")
		List<Predicate> predicateList = (List<Predicate>) resultMap.get(PREDICATE_LIST_CONSTANT);
		predicateList.get(0).not(); // negate predicate

		return resultMap;
	}

	@Override
	public <T extends BaseEntity, V> void addNotEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap) {

		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

		if (resultMap == null || resultMap.keySet().isEmpty()) {
			// initializing Map if it doesn't meet requirements
			resultMap = new HashMap<>();

			CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
			Metamodel m = getEntityManager().getMetamodel();
			EntityType<T> metaT = m.entity(type);
			Root<T> entityRoot = criteriaQuery.from(metaT);
			Predicate predicate = criteriaBuilder.notEqual(entityRoot.get(field.toLowerCase()), value);

			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(predicate);
			resultMap.put(PREDICATE_LIST_CONSTANT, predicate);

		} else {
			// construct and add a new predicate.
			@SuppressWarnings("unchecked")
			Root<T> entityRoot = (Root<T>) resultMap.get(ENTITY_ROOT_CONSTANT);
			Predicate predicate = criteriaBuilder.notEqual(entityRoot.get(field.toLowerCase()), value);

			@SuppressWarnings("unchecked")
			List<Predicate> predicateList = (List<Predicate>) resultMap.get(PREDICATE_LIST_CONSTANT);
			predicateList.add(predicate);
		}

	}

	@Override
	public <T extends BaseEntity> T findById(Class<T> type, Long id) {
		return getEntityManager().find(type, id);
	}

	@Override
	public <T extends BaseEntity> T save(T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	@Override
	public <T extends BaseEntity> T update(T entity) {
		return getEntityManager().merge(entity);

	}

	@Override
	public <T extends BaseEntity> void delete(T entity) {
		entity = getEntityManager().merge(entity);
		getEntityManager().remove(entity);

	}

	@Override
	public <T extends BaseEntity> boolean exists(Class<T> type, Long id) {
		return findById(type, id) != null;
	}

	@Override
	public <T extends BaseEntity> List<T> findAll(Class<T> type) {

		CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
		Root<T> entityRoot = getEntityRoot(type, criteriaQuery);
		criteriaQuery.select(entityRoot);

		addIdFieldOrderAscending(type, entityRoot, criteriaQuery);

		return getResultListFromPreparedSelect(criteriaQuery);

	}

	@Override
	public <T extends BaseEntity> List<T> findAll(Class<T> type, Order order) {
		CriteriaQuery<T> select = getBasicSelectCriteriaQuery(type);
		select.orderBy(order);
		return getResultListFromPreparedSelect(select);

	}

	@Override
	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Order order,
			Map<String, Object> predicateMap) {

		@SuppressWarnings("unchecked")
		CriteriaQuery<T> criteriaQuery = (CriteriaQuery<T>) predicateMap.get(CRITERIA_QUERY_CONSTANT);
		@SuppressWarnings("unchecked")
		Root<T> entityRoot = (Root<T>) predicateMap.get(ENTITY_ROOT_CONSTANT);

		@SuppressWarnings("unchecked")
		List<Predicate> predicateList = (List<Predicate>) predicateMap.get(PREDICATE_LIST_CONSTANT);

		Predicate[] prediacteArray = predicateList.toArray(new Predicate[predicateList.size()]);

		criteriaQuery.select(entityRoot);

		criteriaQuery.where(prediacteArray);
		criteriaQuery.orderBy(order);

		return getResultListFromPreparedSelect(criteriaQuery);

	}

	@Override
	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Map<String, Object> predicateMap) {

		@SuppressWarnings("unchecked")
		CriteriaQuery<T> criteriaQuery = (CriteriaQuery<T>) predicateMap.get(CRITERIA_QUERY_CONSTANT);
		@SuppressWarnings("unchecked")
		Root<T> entityRoot = (Root<T>) predicateMap.get(ENTITY_ROOT_CONSTANT);

		@SuppressWarnings("unchecked")
		List<Predicate> predicateList = (List<Predicate>) predicateMap.get(PREDICATE_LIST_CONSTANT);

		Predicate[] prediacteArray = predicateList.toArray(new Predicate[predicateList.size()]);

		criteriaQuery.select(entityRoot);

		criteriaQuery.where(prediacteArray);
		addIdFieldOrderAscending(type, entityRoot, criteriaQuery);

		return getResultListFromPreparedSelect(criteriaQuery);

	}

	@Override
	public <T> void refresh(T entity) {
		// sometimes entity can be received before it was loaded to persistence
		// context.
		if (!getEntityManager().contains(entity)) {
			entity = getEntityManager().merge(entity);
		}
		getEntityManager().refresh(entity);

	}

	@Override
	public void flush() {
		getEntityManager().flush();
	}

	protected <T> String getEntityName(Class<T> type) {
		return type.getSimpleName();
	}

	protected <T extends BaseEntity> List<T> getResultListFromPreparedSelect(CriteriaQuery<T> select) {

		TypedQuery<T> q = entityManager.createQuery(select);
		return q.getResultList();

	}

	protected <T extends BaseEntity> CriteriaQuery<T> getCriteriaQuery(Class<T> type) {
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		return criteriaBuilder.createQuery(type);

	}

	protected <T extends BaseEntity> void addIdFieldOrderAscending(Class<T> type, Root<T> entityRoot,
			CriteriaQuery<T> select) {
		String idFieldName = "";

		try {
			idFieldName = (String) type.getMethod("getIdFieldName").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			logger.error("Error working with id field name {} in reflection", idFieldName, e);
		}

		select.orderBy(getCriteriaBuilder().asc(entityRoot.get(idFieldName)));
	}

	protected <T extends BaseEntity> Root<T> getEntityRoot(Class<T> type) {
		// https://docs.oracle.com/javaee/7/tutorial/persistence-criteria003.htm
		CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
		return criteriaQuery.from(type);

	}

	protected <T extends BaseEntity> Root<T> getEntityRoot(Class<T> type, CriteriaQuery<T> criteriaQuery) {
		return criteriaQuery.from(type);

	}

	protected <T extends BaseEntity> CriteriaQuery<T> getBasicSelectCriteriaQuery(Class<T> type) {
		CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
		Root<T> entityRoot = getEntityRoot(type, criteriaQuery);
		criteriaQuery.select(entityRoot);

		return criteriaQuery;
	}

	@SuppressWarnings("unchecked")
	protected <T extends BaseEntity> Class<?> getFieldClass(Class<T> type, String fieldName) {

		Field[] fields = type.getDeclaredFields();

		List<Field> fieldList = Arrays.asList(fields);
		Optional<Field> foundFieldOptional = fieldList.stream().filter(t -> t.getName().equals(fieldName)).findFirst();
		return foundFieldOptional.isPresent() ? (Class<T>) foundFieldOptional.get().getType() : null;

	}

}
