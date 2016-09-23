/**
 *
 */
package sample.service.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.dao.JpaDao;
import sample.domain.BaseEntity;
import sample.exceptions.BusinessLogicException;
import sample.service.GenericService;

/**
 * This class provides access to underlying DAO interface. One should use this
 * class to extend custom entities service layer implementation classes.
 *
 * @author duplyk.a
 *
 */
@Service
public class GenericServiceImpl implements GenericService {

	/**
	 * Injected JPA DAO interface
	 */
	@Autowired
	public JpaDao jpaDao;

	@Override
	public <T extends BaseEntity, V> Map<String, Object> getEqualPredicateMap(Class<T> type, String field, V value) {
		return jpaDao.getEqualPredicateMap(type, field, value);
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return jpaDao.getCriteriaBuilder();
	}

	@Override
	public <T extends BaseEntity, V> Map<String, Object> getNotEqualPredicateMap(Class<T> type, String field, V value) {
		return jpaDao.getNotEqualPredicateMap(type, field, value);
	}

	@Override
	public <T extends BaseEntity, V> void addEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap) {
		jpaDao.addEqualPredicateToMap(type, field, value, resultMap);

	}

	@Override
	public <T extends BaseEntity, V> void addNotEqualPredicateToMap(Class<T> type, String field, V value,
			Map<String, Object> resultMap) {
		jpaDao.addNotEqualPredicateToMap(type, field, value, resultMap);

	}

	@Override
	@Transactional(readOnly = true)
	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Order order,
			Map<String, Object> predicateMap) {

		return jpaDao.findAllWithRestrictions(type, order, predicateMap);
	}

	@Override
	@Transactional(readOnly = true)
	public <T extends BaseEntity> List<T> findAllWithRestrictions(Class<T> type, Map<String, Object> predicateMap) {
		return jpaDao.findAllWithRestrictions(type, predicateMap);
	}

	@Override
	@Transactional(readOnly = true)
	public <T extends BaseEntity> T findById(Class<T> type, Long id) {
		return jpaDao.findById(type, id);
	}

	@Override
	@Transactional(readOnly = true)
	public <T extends BaseEntity> List<T> findAll(Class<T> type) {
		return jpaDao.findAll(type);
	}

	@Override
	@Transactional(readOnly = true)
	public <T extends BaseEntity> List<T> findAll(Class<T> type, Order order) {
		return jpaDao.findAll(type, order);
	}

	@Override
	@Transactional
	public <T extends BaseEntity> T save(T entity) throws BusinessLogicException {
		return jpaDao.save(entity);
	}

	@Override
	@Transactional
	public <T extends BaseEntity> T update(T entity) throws BusinessLogicException {
		return jpaDao.update(entity);
	}

	@Override
	@Transactional
	public <T extends BaseEntity> void delete(T entity) throws BusinessLogicException {
		jpaDao.delete(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public <T extends BaseEntity> boolean exists(Class<T> type, Long id) {
		return jpaDao.exists(type, id);
	}

	@Override
	@Transactional(readOnly = true)
	public <T> void refresh(T entity) {
		jpaDao.refresh(entity);

	}

}
