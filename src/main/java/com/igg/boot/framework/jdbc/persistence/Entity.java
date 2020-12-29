package com.igg.boot.framework.jdbc.persistence;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Iterator;

import com.igg.boot.framework.jdbc.persistence.annotation.Id;
import com.igg.boot.framework.jdbc.persistence.exception.DBAnnotationException;
import com.igg.boot.framework.jdbc.persistence.utils.ObjectUtil;

public abstract class Entity implements Cloneable, Serializable, Comparable<Entity> {
	private static final long serialVersionUID = 4224390531787078169L;
	public static final String COLUMN_ACCESS_OPERATOR = ".";
	public static final String TABLE_SUFFIX_CONNECTOR = "_";
	public static final String ORDER_DESCENDING = "DESC";
	public static final Class<?>[] ASSOCIATION_LINK_ALL = null;
	public static final String TABLE_NAME_DEFAULT = "";
	public static final String COLUMN_NAME_DEFAULT = "";
	public static final int COLUMN_TYPE_DEFAULT = 0;
	public static final int COLUMN_SIZE_DEFAULT = 0;
	public static final Class<? extends Entity> REFERENCE_CLASS_DEFAULT = Entity.class;

	public int hashCode() {
		int result = super.hashCode();
		String idstr = this.identityString();
		long id = idstr == null ? 0L : Long.parseLong(idstr);
		result = 31 * result + (int) (id ^ id >>> 32);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj != null && this.getClass() == obj.getClass()) {
			Entity other = (Entity) obj;
			String idstr = this.identityString();
			String oidstr = other.identityString();
			return idstr == oidstr ? true : (idstr == null ? false : idstr.equals(oidstr));
		} else {
			return false;
		}
	}

	public int compareTo(Entity other) {
		String idstr = this.identityString();
		String oidstr = other.identityString();
		return idstr == null && oidstr == null
				? 0
				: (idstr == null
						? 1
						: (oidstr == null ? -1 : (new BigDecimal(idstr)).compareTo(new BigDecimal(oidstr))));
	}

	public String identityString() {
		Class<?> clazz = this.getClass();
		Iterator<Field> arg1 = ObjectUtil.getAllField(clazz).iterator();

		Field field;
		Id id;
		do {
			if (!arg1.hasNext()) {
				throw new DBAnnotationException(
						"Annotation " + Id.class.getName() + " not found for " + clazz.getName());
			}

			field = (Field) arg1.next();
			id = (Id) field.getAnnotation(Id.class);
		} while (id == null);

		try {
			Object e = (new PropertyDescriptor(field.getName(), clazz)).getReadMethod().invoke(this, new Object[0]);
			if (e == null) {
				return null;
			} else {
				if (Integer.class.equals(e.getClass())) {
					int val = ((Integer) e).intValue();
					if (val == 0) {
						return null;
					}
				} else if (Long.class.equals(e.getClass())) {
					long val1 = ((Long) e).longValue();
					if (val1 == 0L) {
						return null;
					}
				}

				return e.toString();
			}
		} catch (InvocationTargetException | IntrospectionException | IllegalAccessException arg7) {
			throw new RuntimeException(arg7);
		}
	}

	public boolean isTransient() {
		return this.identityString() == null;
	}
}