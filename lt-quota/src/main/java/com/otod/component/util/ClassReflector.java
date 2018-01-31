/*
 * Source code of the book of Thinking in Java Component Design
 * ��������Java ������
 * ����: �׵���
 * Email: kshark2008@gmail.com
 * Date: 2008-12
 * Copyright 2008-2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.otod.component.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class ClassReflector {
	public ClassReflector(Object obj) {
		this.cls = obj.getClass();
		this.obj = obj;
		obtainMethods();
	}
	
	public ClassReflector(Class cls) {
		this.cls = cls;
		obtainMethods();
	}
	
	public Object getReflectObject() {
		return obj;
	}
	
	public Class getReflectClass() {
		return cls;
	}
	
	public String getClassName() {
		return cls.getName();
	}
	
	public String getSimpleClassName() {
		String clsName = cls.getSimpleName();
		return clsName;
	}
	
	public static Class newClass(String typeName) {
		try {
			int iType = DataType.getDataType(typeName);
			switch (iType) {
			case DataType.DT_byte:
				return byte.class;
			case DataType.DT_short:
				return short.class;
			case DataType.DT_int:
				return int.class;
			case DataType.DT_long:
				return long.class;
			case DataType.DT_float:
				return float.class;
			case DataType.DT_double:
				return double.class;
			case DataType.DT_char:
				return char.class;
			case DataType.DT_boolean:
				return boolean.class;
			}
			
			Class cls = Class.forName(typeName);
			//Class cls = Thread.currentThread().getContextClassLoader().loadClass(typeName);
			return cls;
		} catch (Exception e) {
			throw new GeneralException(e);
		}	
	}
	
	public static Object newInstance(String typeName) {
		Class cls = newClass(typeName);
		return newInstance(cls);
	}
	
	public static Object newInstance(Class cls) {
		try {
			return cls.newInstance();
		} catch (Exception e) {
			throw new GeneralException(e);
		}
	}
	
	public static Object newArray(Class cls, int len) {
		try {
			Object obj = Array.newInstance(cls, len);
			return obj;
		} catch (Exception e) {
			throw new GeneralException(e);
		}
	}
	
	public static void setArrayElement(Object array, int index, Object elementValue) {
		try {
			Array.set(array, index, elementValue);
		} catch (Exception e) {
			throw new GeneralException(e);
		}
	}
	
	public int getPropertyCount() {
		return getMethodList.size();
	}
	
	public String getPropertyName(int index) {
		if (index >=0 && index < getMethodList.size()) {
			String methodName = getMethodList.get(index).getName();
			if (methodName.startsWith("is")) {
				return lowercaseFirst(methodName.substring(2));
			} else {
				return lowercaseFirst(methodName.substring(3));
			}
		}
		
		return null;
	}
	
	public Object getPropertyValue(int index) {
		if (index >=0 && index < getMethodList.size()) {
			try {
				return getMethodList.get(index).invoke(obj, new Object[] {});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void setPropertyValue(String propName, Object propValue)  {
		setPropertyValue(propName, propValue, DataType.DT_Unknown);
	}
	
	public void setPropertyValue(String propName, Object propValue, int dataType) {
		String methodNameSuffix = uppercaseFirst(propName);
		String setMethodName = "set" + methodNameSuffix;
		Method setMethod = findMethod(setMethodName);
		if (setMethod == null) {
			throw new GeneralException("method " + setMethodName + " does not exist!");
		} 
		
		if (dataType != DataType.DT_Unknown) {
			try {
				Object convertValue = DataType.toType(propValue, dataType);
				setMethod.invoke(obj, new Object[] {convertValue});
				return;
			} catch (Exception e) {}
		}
		
		
		// ��������ʶ���ת��
		String getMethodName = "get" + methodNameSuffix;
		Method getMethod = findMethod(getMethodName);
		if (getMethod == null) {
			throw new GeneralException("method " + getMethodName + " does not exist!");
		}
		String argType = getMethod.getGenericReturnType().toString();
		Object convertValue = DataType.toType(propValue, argType);

		try {
			setMethod.invoke(obj, new Object[] {convertValue});
		} catch (Exception e) {
			throw new GeneralException(e);
		}
	}

	public Method findMethod(String methodName) {
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		
		return null;
	}
	
	public Method findGetMethod(String propName) {
		String methodNameSuffix = uppercaseFirst(propName);
		String getMethodName = "get" + methodNameSuffix;
		String getMethodName2 = "is" + methodNameSuffix;
		for (Method m : methods) {
			String methodName = m.getName();
			if (methodName.equals(getMethodName) || methodName.equals(getMethodName2)) {
				return m;
			}
		}
		return null;
	}
	
	public Method findSetMethod(String propName) {
		String methodNameSuffix = uppercaseFirst(propName);
		String setMethodName = "set" + methodNameSuffix;
		for (Method m : methods) {
			String methodName = m.getName();
			if (methodName.equals(setMethodName)) {
				return m;
			}
		}
		return null;
	}
	
	public String getPropertyType(String propName) {
		String methodNameSuffix = uppercaseFirst(propName);
		String getMethodName = "get" + methodNameSuffix;
		Method getMethod = findMethod(getMethodName);
		if (getMethod == null) {
			throw new GeneralException("method " + getMethodName + " does not exist!");
		}
		String typeName = getMethod.getGenericReturnType().toString();
		return DataType.toSimpleType(typeName);
	}
	
	public Object getPropertyValue(String propName) {
		String methodNameSuffix = uppercaseFirst(propName);
		String getMethodName = "get" + methodNameSuffix;
		Method getMethod = findMethod(getMethodName);
		if (getMethod == null) {
			getMethodName = "is" + methodNameSuffix;
			getMethod = findMethod(getMethodName);
		}
		
		if (getMethod == null) {
			return null;
		} else {
			try {
				return getMethod.invoke(obj, new Object[] {});
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public Annotation[] getAnnotations() {
		return cls.getAnnotations();
	}
	
	public Field[] getFields() {
		return cls.getDeclaredFields();
	}
	
	private void obtainMethods() {
		methods = cls.getMethods();
		for (Method m : methods) {
			String methodName = m.getName();
			if ((methodName.startsWith("get") && !methodName.equals("getClass"))
					|| methodName.startsWith("is")) {
				getMethodList.add(m);
			}
		}
	}
	
	private String uppercaseFirst(String propName) {
		return propName.substring(0, 1).toUpperCase() + propName.substring(1);
	}
	
	private String lowercaseFirst(String propName) {
		return propName.substring(0, 1).toLowerCase() + propName.substring(1);
	}
	
	private Object obj;
	private Class cls;
	private Method[] methods;
	private ArrayList<Method> getMethodList = new ArrayList<Method>();

	public static final int METHOD_GET = 1;
	public static final int METHOD_SET = 2;
}
	
