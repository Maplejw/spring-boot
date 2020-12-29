package com.igg.boot.framework.jdbc.persistence.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectUtil {
	public static boolean isExtends(Class<?> clazz, Class<?> superClass) {
		if (clazz == superClass) {
			return true;
		} else {
			for (Class<?> clz = clazz.getSuperclass(); clz != null; clz = clz.getSuperclass()) {
				if (clz == superClass) {
					return true;
				}
			}

			return false;
		}
	}

	public static boolean isImplement(Class<?> clazz, Class<?> interfaceClass) {
		if (!interfaceClass.isInterface()) {
			return false;
		} else if (clazz == interfaceClass) {
			return true;
		} else {
			List<Class<?>> interfaces = new ArrayList<>();
			if (clazz.isInterface()) {
				interfaces.addAll(getSuperInterfaces(clazz));
			} else {
				interfaces.addAll(getClassInterfaces(clazz));
			}

			return interfaces.contains(interfaceClass);
		}
	}

	private static List<Class<?>> getClassInterfaces(Class<?> clazz) {
		List<Class<?>> interfaces = new ArrayList<>();
		Class<?>[] class0 = clazz.getInterfaces();
		int arg2 = class0.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			Class<?> interface0 = class0[arg3];
			interfaces.addAll(getSuperInterfaces(interface0));
		}

		for (Class<?> arg5 = clazz.getSuperclass(); arg5 != null; arg5 = arg5.getSuperclass()) {
			interfaces.addAll(getClassInterfaces(arg5));
		}

		return interfaces;
	}

	private static List<Class<?>> getSuperInterfaces(Class<?> interface0) {
		List<Class<?>> interfaces = new ArrayList<>();
		interfaces.add(interface0);
		Class<?>[] arg1 = interface0.getInterfaces();
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			Class<?> interface1 = arg1[arg3];
			interfaces.addAll(getSuperInterfaces(interface1));
		}

		return interfaces;
	}

	public static List<Field> getAllField(Class<?> clazz) {
		ArrayList<Field> list = new ArrayList<>();
		Field[] clz = clazz.getDeclaredFields();
		int arg2 = clz.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			Field field = clz[arg3];
			list.add(field);
		}

		for (Class<?> arg5 = clazz.getSuperclass(); arg5 != null; arg5 = arg5.getSuperclass()) {
			list.addAll(getAllField(arg5));
		}

		return list;
	}

	public static List<String> getAllFieldNames(Class<?> clazz) {
		List<String> list = new ArrayList<>();
		List<Field> fields = getAllDeclareFields(clazz);
		Iterator<Field> arg2 = fields.iterator();

		while (arg2.hasNext()) {
			Field field = (Field) arg2.next();
			list.add(field.getName());
		}

		return list;
	}

	public static List<Field> getAllDeclareFields(Class<?> clazz) {
		List<Field> list = new ArrayList<>();
		Field[] arg1 = clazz.getDeclaredFields();
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			Field field = arg1[arg3];
			list.add(field);
		}

		return list;
	}

	public static List<Field> getFieldsByAnnotation(Class<?> clazz, Class<? extends Annotation> atClass) {
		List<Field> list = getAllField(clazz);
		List<Field> ret = new ArrayList<>();
		Iterator<Field> arg3 = list.iterator();

		while (arg3.hasNext()) {
			Field field = (Field) arg3.next();
			if (field.getAnnotation(atClass) != null) {
				ret.add(field);
			}
		}

		return ret;
	}
}