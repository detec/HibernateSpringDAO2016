package sample.domain;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -2233785857172942658L;

	@Id
	@GeneratedValue
	private Long code;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public BaseEntity() {
	}

	// This method is needed to return quickly the name of id field.
	public static String getIdFieldName() {
		return "code";
	}

	public <T extends BaseEntity> T cloneEntity(T origObj) {

		Field fields[];
		Class curClass = origObj.getClass();
		T newEntity = null;
		try {
			newEntity = (T) curClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		// Spin through all fields of the class & all its superclasses
		do {
			fields = curClass.getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getName().equals("serialVersionUID")) {
					continue;
				}
				try {
					fields[i].setAccessible(true);
					fields[i].set(newEntity, fields[i].get(origObj));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			curClass = curClass.getSuperclass();
		}

		while (curClass != null);
		return newEntity;
	}

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
				if (fields[i].getName().equals("serialVersionUID")) {
					continue;
				}
				try {
					fields[i].setAccessible(true);
					fields[i].set(destinationEntity, fields[i].get(sourceEntity));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			curClass = curClass.getSuperclass();
		}

		while (curClass != null);

	}
}