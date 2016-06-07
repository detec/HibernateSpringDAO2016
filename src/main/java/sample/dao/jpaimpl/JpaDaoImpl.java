
package sample.dao.jpaimpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.springframework.stereotype.Repository;

import sample.dao.JpaDao;
import sample.domain.BaseEntity;

@Repository
public class JpaDaoImpl implements JpaDao {

	private EntityManager entityManager;

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// http://www.tutorialspoint.com/jpa/jpa_criteria_api.htm
	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		return criteriaBuilder;

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

		resultMap.put("criteriaQuery", criteriaQuery);
		resultMap.put("entityRoot", entityRoot);

		List<Predicate> predicateList = new ArrayList<Predicate>();
		predicateList.add(predicate);
		resultMap.put("predicateList", predicateList);

		return resultMap;

	}

	@Override
	public <T extends BaseEntity, V> void addEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap) {

		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

		if (resultMap == null || resultMap.keySet().isEmpty()) {
			// initializing Map if it doesn't meet requirements
			resultMap = new HashMap<String, Object>();

			CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
			Metamodel m = getEntityManager().getMetamodel();
			EntityType<T> metaT = m.entity(type);
			Root<T> entityRoot = criteriaQuery.from(metaT);

			Predicate predicate = criteriaBuilder.equal(entityRoot.get(field.toLowerCase()), value);
			resultMap.put("predicateList", Arrays.asList(new Predicate[] { predicate }));

		} else {
			// construct and add a new predicate.
			@SuppressWarnings("unchecked")
			Root<T> entityRoot = (Root<T>) resultMap.get("entityRoot");
			Predicate predicate = criteriaBuilder.equal(entityRoot.get(field.toLowerCase()), value);

			@SuppressWarnings("unchecked")
			List<Predicate> predicateList = (List<Predicate>) resultMap.get("predicateList");
			predicateList.add(predicate);
		}

	}

	@Override
	public <T extends BaseEntity, V> Map<String, Object> getNotEqualPredicateMap(Class<T> type, String field, V value) {

		Map<String, Object> resultMap = getEqualPredicateMap(type, field, value);
		@SuppressWarnings("unchecked")
		List<Predicate> predicateList = (List<Predicate>) resultMap.get("predicateList");
		predicateList.get(0).not(); // negate predicate

		return resultMap;
	}

	@Override
	public <T extends BaseEntity, V> void addNotEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap) {

		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

		if (resultMap == null || resultMap.keySet().isEmpty()) {
			// initializing Map if it doesn't meet requirements
			resultMap = new HashMap<String, Object>();

			CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
			Metamodel m = getEntityManager().getMetamodel();
			EntityType<T> metaT = m.entity(type);
			Root<T> entityRoot = criteriaQuery.from(metaT);
			Predicate predicate = criteriaBuilder.notEqual(entityRoot.get(field.toLowerCase()), value);

			List<Predicate> predicateList = new ArrayList<Predicate>();
			predicateList.add(predicate);
			resultMap.put("predicateList", predicate);

		} else {
			// construct and add a new predicate.
			@SuppressWarnings("unchecked")
			Root<T> entityRoot = (Root<T>) resultMap.get("entityRoot");
			Predicate predicate = criteriaBuilder.notEqual(entityRoot.get(field.toLowerCase()), value);

			@SuppressWarnings("unchecked")
			List<Predicate> predicateList = (List<Predicate>) resultMap.get("predicateList");
			predicateList.add(predicate);
		}

	}

	@Override
	public <T extends BaseEntity> T findById(Class<T> type, Long id) {

		T entity = getEntityManager().find(type, id);
		return entity;
	}

	@Override
	public <T extends BaseEntity> T save(T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	@Override
	public <T extends BaseEntity> T update(T entity) {
		T mergedEntity = getEntityManager().merge(entity);
		return mergedEntity;
	}

	@Override
	public <T extends BaseEntity> void delete(T entity) {
		entity = getEntityManager().merge(entity);
		getEntityManager().remove(entity);

	}

	@Override
	public <T extends BaseEntity> boolean exists(Class<T> type, Long id) {
		return (findById(type, id) != null);
	}

	/*
	 * Returns all entities ordered by id in ascending order.
	 */
	@Override
	public <T extends BaseEntity> List<T> findAll(Class<T> type) {
		// Simplification doesn't work.
		CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
		Root<T> entityRoot = getEntityRoot(type, criteriaQuery);
		criteriaQuery.select(entityRoot);

		addIdFieldOrderAscending(type, entityRoot, criteriaQuery);

		List<T> results = getResultListFromPreparedSelect(criteriaQuery);

		return results;
	}

	/*
	 * Returns all entities ordered by order sent
	 */
	@Override
	public <T extends BaseEntity> List<T> findAll(Class<T> type, Order order) {
		CriteriaQuery<T> select = getBasicSelectCriteriaQuery(type);

		select.orderBy(order);

		List<T> results = getResultListFromPreparedSelect(select);
		return results;

	}

	@Override
	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Order order,
			Map<String, Object> predicateMap) {

		@SuppressWarnings("unchecked")
		CriteriaQuery<T> criteriaQuery = (CriteriaQuery<T>) predicateMap.get("criteriaQuery");
		@SuppressWarnings("unchecked")
		Root<T> entityRoot = (Root<T>) predicateMap.get("entityRoot");

		@SuppressWarnings("unchecked")
		List<Predicate> predicateList = (List<Predicate>) predicateMap.get("predicateList");

		Predicate[] prediacteArray = predicateList.toArray(new Predicate[predicateList.size()]);

		criteriaQuery.select(entityRoot);

		criteriaQuery.where(prediacteArray);
		criteriaQuery.orderBy(order);

		List<T> results = getResultListFromPreparedSelect(criteriaQuery);

		return results;
	}

	@Override
	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Map<String, Object> predicateMap) {

		@SuppressWarnings("unchecked")
		CriteriaQuery<T> criteriaQuery = (CriteriaQuery<T>) predicateMap.get("criteriaQuery");
		@SuppressWarnings("unchecked")
		Root<T> entityRoot = (Root<T>) predicateMap.get("entityRoot");

		@SuppressWarnings("unchecked")
		List<Predicate> predicateList = (List<Predicate>) predicateMap.get("predicateList");

		// http://stackoverflow.com/questions/22731706/java-lang-classcastexception-ljava-lang-object-cannot-be-cast-to-ljava-lang
		Predicate[] prediacteArray = predicateList.toArray(new Predicate[predicateList.size()]);

		criteriaQuery.select(entityRoot);

		criteriaQuery.where(prediacteArray);
		addIdFieldOrderAscending(type, entityRoot, criteriaQuery);

		List<T> results = getResultListFromPreparedSelect(criteriaQuery);

		return results;

	}

	@Override
	public <T> void refresh(T entity) {
		getEntityManager().refresh(entity);

	}

	@Override
	public void flush() {
		getEntityManager().flush();
	}

	protected <T> String getEntityName(Class<T> type) {
		return type.getSimpleName();
	}

	// https://docs.oracle.com/javaee/7/tutorial/persistence-criteria003.htm

	protected <T extends BaseEntity> List<T> getResultListFromPreparedSelect(CriteriaQuery<T> select) {

		TypedQuery<T> q = entityManager.createQuery(select);
		List<T> results = q.getResultList();
		return results;
	}

	protected <T extends BaseEntity> CriteriaQuery<T> getCriteriaQuery(Class<T> type) {
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
		return criteriaQuery;
	}

	protected <T extends BaseEntity> void addIdFieldOrderAscending(Class<T> type, Root<T> entityRoot,
			CriteriaQuery<T> select) {
		String idFieldName = "";

		try {
			idFieldName = (String) type.getMethod("getIdFieldName").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}

		select.orderBy(getCriteriaBuilder().asc(entityRoot.get(idFieldName)));
	}

	protected <T extends BaseEntity> Root<T> getEntityRoot(Class<T> type) {
		// https://docs.oracle.com/javaee/7/tutorial/persistence-criteria003.htm
		CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
		Root<T> entityRoot = criteriaQuery.from(type);
		return entityRoot;
	}

	protected <T extends BaseEntity> Root<T> getEntityRoot(Class<T> type, CriteriaQuery<T> criteriaQuery) {
		Root<T> entityRoot = criteriaQuery.from(type);
		return entityRoot;
	}

	protected <T extends BaseEntity> CriteriaQuery<T> getBasicSelectCriteriaQuery(Class<T> type) {
		CriteriaQuery<T> criteriaQuery = getCriteriaQuery(type);
		Root<T> entityRoot = getEntityRoot(type, criteriaQuery);
		criteriaQuery.select(entityRoot);

		return criteriaQuery;
	}

	@SuppressWarnings("unchecked")
	protected <T extends BaseEntity> Class<?> getFieldClass(Class<T> type, String fieldName) {
		Field fields[];
		fields = type.getDeclaredFields();

		List<Field> fieldList = Arrays.asList(fields);
		Class<T> clazz = null;
		List<Class<T>> classList = new ArrayList<Class<T>>();

		// find field with the given name and return its class
		fieldList.stream().filter(t -> t.getName().equals(fieldName)).forEach(t -> {
			classList.add((Class<T>) t.getType());
		});

		if (classList.size() == 1) {
			clazz = classList.get(0);
		}

		// o.getClass().getField("fieldName").getType().isPrimitive(); for
		// primitives
		return clazz;
	}

}
