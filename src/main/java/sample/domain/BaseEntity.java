package sample.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Basic mapped superclass to hold Id field
 *
 *
 */

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -2233785857172942658L;

	@Id
	@GeneratedValue
	protected Long code;

	/**
	 * ID getter
	 *
	 * @return Long id value
	 */
	public Long getCode() {
		return code;
	}

	/**
	 * id code setter
	 *
	 * @param code
	 *            set id field value
	 */
	public void setCode(Long code) {
		this.code = code;
	}

	/**
	 * Jackson-required constructor
	 */
	public BaseEntity() {
		// For serialization purposes.
	}

	/**
	 * Resolve current id field for DAO layer
	 *
	 * @return string with id field name
	 */
	public static String getIdFieldName() {
		Field[] fields = BaseEntity.class.getDeclaredFields();

		// Only Long type of field with annotation javax.persistence.Id
		Optional<Field> idFieldOpt = Arrays.stream(fields)
				.filter(field -> (field.getAnnotation(Id.class) != null && field.getType() == Long.class)).findFirst();

		return idFieldOpt.isPresent() ? idFieldOpt.get().getName() : null;

	}

	/**
	 * This method is useful in JPA where an object, refreshed from database,
	 * gets into persistence context and destroys current values of entity's
	 * fields.
	 *
	 * @param sourceEntity
	 *            entity form which field values will be copied.
	 * @param destinationEntity
	 *            entity to which values will be assigned.
	 */
	public <T extends BaseEntity> void copyFields(T sourceEntity, T destinationEntity) {

		if (sourceEntity.getClass() != destinationEntity.getClass()) {
			throw new IllegalArgumentException(
					"Source object must be the same class or a subclass of destination one!");
		}

		Field fields[];
		Class curClass = sourceEntity.getClass();

		// Spin through all fields of the class & all its superclasses
		do {
			fields = curClass.getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				Field currentField = fields[i];

				if (java.lang.reflect.Modifier.isStatic(currentField.getModifiers())) {
					continue;
				}

				try {
					currentField.setAccessible(true);
					currentField.set(destinationEntity, currentField.get(sourceEntity));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// swallow
				}
			}
			curClass = curClass.getSuperclass();
		}

		while (curClass != null);

	}
}