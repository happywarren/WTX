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

import java.util.*;
import java.text.*;
import java.math.*;

public class DataType {
	private static String deletePrefix(String str, String prefix) {
		if (str.startsWith(prefix)) {
			str = str.substring(prefix.length());
		}
		return str;
	}
	
	public static String toSimpleType(String typeName) {
		typeName = deletePrefix(typeName, "class ");
		typeName = deletePrefix(typeName, "java.lang.");
		typeName = deletePrefix(typeName, "java.util.");
		typeName = deletePrefix(typeName, "java.sql.");
		typeName = deletePrefix(typeName, "java.math.");
		return typeName;
	}
	
	public static int getDataType(Object obj) {
		if (obj == null) {
			return DT_Unknown;
		}
		
		return getDataType(obj.getClass().getName());
	}
	
	public static int getDataType(Class cls) {
		if (cls == null) {
			return DT_Unknown;
		}
		
		return getDataType(cls.getName());
	}
	
	public static int getDataType(String typeName) {
		typeName = toSimpleType(typeName);
		
		if (typeName.charAt(0) == '[') {
			return DT_Array;
		}
		
		Integer iType = dataTypeMap.get(typeName);
		return iType == null ? DT_UserDefine : iType;
	}	
	
	public static String toUnifyTypeName(String sName) {
		return matchBracket(sName, "<", ">", false);
	}
	
	public static String getElementTypeName(String collectionTypeName) {
		return getElementTypeName(collectionTypeName, 0);
	}
	
	public static String getElementTypeName(String collectionTypeName, int itemIndex) {
		String typeName = toSimpleType(collectionTypeName);
		int iType = getDataType(typeName);
		if (iType == DT_Array) {
			switch (typeName.charAt(1)) {
			case 'B':	// byte[]
				return "byte";
			case 'S':	// short[]
				return "short";
			case 'I':	// int[]
				return "int";
			case 'J':	// long[]
				return "long";
			case 'Z':	// boolean[]
				return "boolean";
			case 'C':	// char[]
				return "char";
			case 'F':	// float[]
				return "float";
			case 'D':	// double[]
				return "double";
			case 'L':	// [Ljava.lang.Integer;
				if (typeName.charAt(typeName.length()-1) == ';')
					return typeName.substring(2, typeName.length()-1);
				else
					return typeName.substring(2);
			case '[':	// [[I
				return typeName.substring(1);
			}
		}
		
		String str = matchBracket(typeName, "<", ">", true);
		int iLen = str.length();
		for (int i = 0; i <= itemIndex; i++) {
			str = matchBracket(str, "<", ">", false);
			int iLen1 = str.length();
			if (iLen1 == iLen) {	// û�п����г�Ĳ�����
				break;
			} else {
				iLen = iLen1;
			}
		}
		
		// ���Ҷ��ŷָ���
		int iBegin = 0, iEnd = str.length();
		int iPos = 0;
		for (int i = 0; i < itemIndex+1; i++) {
			iPos = str.indexOf(',', iPos);
			if (iPos == -1) {
				break;
			}
			
			if (i == itemIndex - 1) {
				iBegin = iPos;
			} else if (i == itemIndex) {
				iEnd = iPos;
			}
		}
		
		return str.substring(iBegin+1, iEnd);
	}
	
	public static int getElementDataType(String collectionTypeName) {
		return getElementDataType(collectionTypeName, 0);
	}

	public static int getElementDataType(String collectionTypeName, int itemIndex) {
		return getDataType(getElementTypeName(collectionTypeName, itemIndex));
	}
	
	public static Object toType(Object value, String targetType) {
		int destType = getDataType(targetType);
		return toType(value, destType);
	}
	
	public static Object toType(Object value, int targetType) {
		String fromType = value.getClass().getName();
		int srcType = getDataType(fromType);
		return toType(value, srcType, targetType);
	}
	
	public static Object toType(Object value, int srcType, String targetTypeName) {
		//int targetType =
		return null;
	}
	
	public static Object toType(Object value, int srcType, int targetType) {
		srcType = toObjectType(srcType);
		targetType = toObjectType(targetType);
		if (srcType == targetType) {
			return value;
		}
		
		if (value == null) {
			return null;
		}
		
		Object retObj = null;
		if (srcType == DT_String) {
			String str = ((String)value).trim();
			if (str.length() < 1 || str.equalsIgnoreCase("null")) {
				return null;
			}
		}
		
		if (srcType >= DT_byte && srcType <= DT_boolean) {
			srcType += DT_Byte - DT_byte;
		}
		if (targetType >= DT_byte && targetType <= DT_boolean) {
			targetType += DT_Byte - DT_byte;
		}
		
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		switch (targetType) {
		case DT_Byte:
			switch (srcType) {
			case DT_Short:
				retObj = ((Short)value).byteValue();
				break;
			case DT_Integer:
				retObj = ((Integer)value).byteValue();
				break;
			case DT_Long:
				retObj = ((Long)value).byteValue();
				break;
			case DT_BigInteger:
				retObj = ((BigInteger)value).byteValue();
				break;
			case DT_Float:
				retObj = ((Float)value).byteValue();
				break;
			case DT_Double:
				retObj = ((Double)value).byteValue();
				break;
			case DT_BigDecimal:
				retObj = ((BigDecimal)value).byteValue();
				break;
			case DT_Character:
				retObj = Byte.parseByte(((Character)value).toString());
				break;
			case DT_String:
				retObj = Byte.parseByte((String)value);
				break;
			case DT_Boolean:
				retObj = (byte)(((Boolean)value).booleanValue() ? 1 : 0);
				break;
			}
			break;
		case DT_Short:
			switch (srcType) {
			case DT_Byte:
				retObj = ((Byte)value).shortValue();
				break;
			case DT_Integer:
				retObj = ((Integer)value).shortValue();
				break;
			case DT_Long:
				retObj = ((Long)value).shortValue();
				break;
			case DT_BigInteger:
				retObj = ((BigInteger)value).shortValue();
				break;
			case DT_Float:
				retObj = ((Float)value).shortValue();
				break;
			case DT_Double:
				retObj = ((Double)value).shortValue();
				break;
			case DT_BigDecimal:
				retObj = ((BigDecimal)value).shortValue();
				break;
			case DT_Character:
				retObj = Short.parseShort(((Character)value).toString());
				break;
			case DT_String:
				retObj = Short.parseShort((String)value);
				break;
			case DT_Boolean:
				retObj = (short)(((Boolean)value).booleanValue() ? 1 : 0);
				break;
			}
			break;
		case DT_Integer:
			switch (srcType) {
			case DT_Byte:
				retObj = ((Byte)value).intValue();
				break;
			case DT_Short:
				retObj = ((Short)value).intValue();
				break;
			case DT_Long:
				retObj = ((Long)value).intValue();
				break;
			case DT_BigInteger:
				retObj = ((BigInteger)value).intValue();
				break;
			case DT_Float:
				retObj = ((Float)value).intValue();
				break;
			case DT_Double:
				retObj = ((Double)value).intValue();
				break;
			case DT_BigDecimal:
				retObj = ((BigDecimal)value).intValue();
				break;
			case DT_Character:
				retObj = Integer.parseInt(((Character)value).toString());
				break;
			case DT_String:
				retObj = Integer.parseInt((String)value);
				break;
			case DT_Boolean:
				retObj = (int)(((Boolean)value).booleanValue() ? 1 : 0);
				break;
			case DT_Date:
				retObj = (int)((Date)value).getTime();
				break;
			case DT_Time:
				retObj = (int)((java.sql.Time)value).getTime();
				break;
			case DT_DateTime:
				retObj = (int)((java.sql.Timestamp)value).getTime();
				break;
			}
			break;
		case DT_Long:
			switch (srcType) {
			case DT_Byte:
				retObj = ((Byte)value).longValue();
				break;
			case DT_Short:
				retObj = ((Short)value).longValue();
				break;
			case DT_Integer:
				retObj = ((Integer)value).longValue();
				break;
			case DT_BigInteger:
				retObj = ((BigInteger)value).longValue();
				break;
			case DT_Float:
				retObj = ((Float)value).longValue();
				break;
			case DT_Double:
				retObj = ((Double)value).longValue();
				break;
			case DT_BigDecimal:
				retObj = ((BigDecimal)value).longValue();
				break;
			case DT_Character:
				retObj = Long.parseLong(((Character)value).toString());
				break;
			case DT_String:
				retObj = Long.parseLong((String)value);
				break;
			case DT_Boolean:
				retObj = (long)(((Boolean)value).booleanValue() ? 1 : 0);
				break;
			case DT_Date:
				retObj = ((Date)value).getTime();
				break;
			case DT_Time:
				retObj = ((java.sql.Time)value).getTime();
				break;
			case DT_DateTime:
				retObj = ((java.sql.Timestamp)value).getTime();
				break;
			}
			break;
		case DT_Float:
			switch (srcType) {
			case DT_Byte:
				retObj = ((Byte)value).floatValue();
				break;
			case DT_Short:
				retObj = ((Short)value).floatValue();
				break;
			case DT_Integer:
				retObj = ((Integer)value).floatValue();
				break;
			case DT_Long:
				retObj = ((Long)value).floatValue();
				break;
			case DT_BigInteger:
				retObj = ((BigInteger)value).floatValue();
				break;
			case DT_Double:
				retObj = ((Double)value).floatValue();
				break;
			case DT_BigDecimal:
				retObj = ((BigDecimal)value).floatValue();
				break;
			case DT_Character:
				retObj = Float.parseFloat(((Character)value).toString());
				break;
			case DT_String:
				retObj = Float.parseFloat((String)value);
				break;
			case DT_Boolean:
				retObj = (float)(((Boolean)value).booleanValue() ? 1 : 0);
				break;
			}
			break;
		case DT_Double:
			switch (srcType) {
			case DT_Byte:
				retObj = ((Byte)value).doubleValue();
				break;
			case DT_Short:
				retObj = ((Short)value).doubleValue();
				break;
			case DT_Integer:
				retObj = ((Integer)value).doubleValue();
				break;
			case DT_Long:
				retObj = ((Long)value).doubleValue();
				break;
			case DT_BigInteger:
				retObj = ((BigInteger)value).doubleValue();
				break;
			case DT_Float:
				retObj = ((Float)value).doubleValue();
				break;
			case DT_BigDecimal:
				retObj = ((BigDecimal)value).doubleValue();
				break;
			case DT_Character:
				retObj = Double.parseDouble(((Character)value).toString());
				break;
			case DT_String:
				retObj = Double.parseDouble((String)value);
				break;
			case DT_Boolean:
				retObj = (double)(((Boolean)value).booleanValue() ? 1 : 0);
				break;
			}
			break;
		case DT_Character:
			switch (srcType) {
			case DT_Byte:
				retObj = Character.toChars((Byte)value)[0];
				break;
			case DT_Short:
				retObj = Character.toChars((Short)value)[0];
				break;
			case DT_Integer:
				retObj = Character.toChars((Integer)value)[0];
				break;
			case DT_Long:
				retObj = Character.toChars(((Long)value).intValue())[0];
				break;
			case DT_BigInteger:
				retObj = Character.toChars(((BigInteger)value).intValue())[0];
				break;
			case DT_Float:
				retObj = Character.toChars(((Float)value).intValue())[0];
				break;
			case DT_Double:
				retObj = Character.toChars(((Double)value).intValue())[0];
				break;
			case DT_BigDecimal:
				retObj = Character.toChars(((BigDecimal)value).intValue())[0];
				break;
			case DT_String:
				retObj = ((String)value).charAt(0);
				break;
			case DT_Boolean:
				retObj = (double)(((Boolean)value).booleanValue() ? 'T' : 'F');
				break;
			}
			break;
		case DT_Boolean:
			switch (srcType) {
			case DT_Byte:
				retObj = ((Byte)value).byteValue() == 0 ? false : true;
				break;
			case DT_Short:
				retObj = ((Short)value).shortValue() == 0 ? false : true;
				break;
			case DT_Integer:
				retObj = ((Integer)value).intValue() == 0 ? false : true;
				break;
			case DT_Long:
				retObj = ((Long)value).longValue() == 0 ? false : true;
				break;
			case DT_BigInteger:
				retObj = ((BigInteger)value).longValue() == 0 ? false : true;
				break;
			case DT_Float:
				retObj = ((Float)value).intValue() == 0 ? false : true;
				break;
			case DT_Double:
				retObj = ((Double)value).intValue() == 0 ? false : true;
				break;
			case DT_BigDecimal:
				retObj = ((BigDecimal)value).longValue() == 0 ? false : true;
				break;
			case DT_Character:
				retObj = ((Character)value).charValue() == 'T' ? true : false;
				break;
			case DT_String:
				retObj = ((String)value).equalsIgnoreCase("True") ? true : false;
				break;
			}
			break;
		case DT_String:
			switch (srcType) {
			case DT_Byte:
				retObj = ((Byte)value).toString();
				break;
			case DT_Short:
				retObj = ((Short)value).toString();
				break;
			case DT_Integer:
				retObj = ((Integer)value).toString();
				break;
			case DT_Long:
				retObj = ((Long)value).toString();
				break;
			case DT_BigInteger:
				retObj = ((BigInteger)value).toString();
				break;
			case DT_Float:
				retObj = ((Float)value).toString();
				break;
			case DT_Double:
				retObj = ((Double)value).toString();
				break;
			case DT_BigDecimal:
				retObj = ((BigDecimal)value).toString();
				break;
			case DT_Character:
				retObj = ((Character)value).toString();
				break;
			case DT_Boolean:
				retObj = ((Boolean)value).toString();
				break;
			case DT_Date:
				if (value instanceof java.sql.Date) {
					sdf.applyPattern("yyyy-MM-dd");
				}
				retObj = sdf.format((Date)value);
				break;
			case DT_Time:
				sdf.applyPattern("HH:mm:ss");
				retObj = sdf.format((Date)value);
				break;
			case DT_DateTime:
				retObj = sdf.format((Date)value);
				break;
			}
			break;
		case DT_Date:
			switch (srcType) {
			case DT_String:
//				sdf.applyPattern("yyyy-MM-dd");
//				date = sdf.parse((String)value);
//				retObj = new java.sql.Date(date.getTime());
				retObj = java.sql.Date.valueOf((String)value);
				break;
			case DT_DateTime:
			case DT_Long:
			case DT_Integer:
				Calendar cal = Calendar.getInstance();
				if (srcType == DT_DateTime) {
					cal.setTime((Date)value);
				} else if (srcType == DT_Long) {
					cal.setTimeInMillis((Long)value);
				} else {
					cal.setTimeInMillis((Integer)value);
				}
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				retObj = new java.sql.Date(cal.getTimeInMillis());
				break;
			}
			break;
		case DT_Time:
			switch (srcType) {
			case DT_String:
//				sdf.applyPattern("HH:mm:ss");
//				date = sdf.parse((String)value);
//				retObj = new java.sql.Time(date.getTime());
				retObj = java.sql.Time.valueOf((String)value);
				break;
			case DT_DateTime:
			case DT_Long:
			case DT_Integer:
				Calendar cal = Calendar.getInstance();
				if (srcType == DT_DateTime) {
					cal.setTime((Date)value);
				} else if (srcType == DT_Long) {
					cal.setTimeInMillis((Long)value);
				} else {
					cal.setTimeInMillis((Integer)value);
				}
				cal.set(Calendar.YEAR, 0);
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 0);
				cal.set(Calendar.MILLISECOND, 0);
				retObj = new java.sql.Time(cal.getTimeInMillis());
				break;
			}
			break;
		case DT_DateTime:
			switch (srcType) {
			case DT_String:
//				date = sdf.parse((String)value);
//				retObj = new java.sql.Timestamp(date.getTime());
				retObj = java.sql.Timestamp.valueOf((String)value);
				break;
			case DT_Date:
			case DT_Time:
			case DT_Long:
			case DT_Integer:
				Calendar cal = Calendar.getInstance();
				if (srcType == DT_Date || srcType == DT_Time) {
					cal.setTime((Date)value);
				} else if (srcType == DT_Long) {
					cal.setTimeInMillis((Long)value);
				} else {
					cal.setTimeInMillis((Integer)value);
				}
				cal.set(Calendar.MILLISECOND, 0);
				retObj = new java.sql.Timestamp(cal.getTimeInMillis());
				break;
			}
			break;
			
		}
		} catch (Exception e) {
		}
		
		return retObj;
	}
	
	
	
	private static String matchBracket(String str, String matchBegin, String matchEnd, boolean bGet) {
		int iLen = str.length();
		int iMatchLen = matchBegin.length();
		char cBegin = '[', cEnd = ']';
		int iBeginPos = -1, iEndPos = -1;
		boolean bFindMatch = false;
		int iCount = -1;
		for (int i = 0; i < iLen; i++) {
			char ch = str.charAt(i);
			if (bFindMatch) {
				if (ch == cBegin) {
					iCount++;
				} else if (ch == cEnd) {
					iCount--;
					if (iCount == 0) {
						iEndPos = i;
						break;
					}
				}
			} else {
				for (int k = 0; k < iMatchLen; k++) {
					cBegin = matchBegin.charAt(k);
					if (ch == cBegin) {
						bFindMatch = true;
						cEnd = matchEnd.charAt(k);
						iBeginPos = i;
						iCount = 1;
						break;
					}
				}
			}
		}
		
		if (bFindMatch) {	// �ҵ�ƥ��
			if (bGet) {	// ��ȡƥ�������е��Ӵ�
				return str.substring(iBeginPos+1, iEndPos);
			} else {	// �г�ƥ�������е��ִ�
				return str.substring(0, iBeginPos) + str.substring(iEndPos + 1);
			}
		} else {			// δ�ҵ�ƥ��
			if (bGet) {	// ��ȡƥ�������е��Ӵ�
				return "";
			} else {	// �г�ƥ�������е��ִ�
				return str;
			}
		}
	}
	
	public static boolean isSimpleType(int iType) {
		switch (iType) {
			case DT_byte:
			case DT_short:
			case DT_int:
			case DT_long:
			case DT_float:
			case DT_double:
			case DT_char:
			case DT_boolean:
			
			case DT_Byte:
			case DT_Short:
			case DT_Integer:
			case DT_Long:
			case DT_BigInteger:
			case DT_Float:
			case DT_Double:
			case DT_BigDecimal:
			case DT_Character:
			case DT_String:
			case DT_Boolean:
			case DT_Date:
			case DT_Time:
			case DT_DateTime:
				return true;
		}
		
		return false;
	}
	
	public static int toObjectType(int iType) {
		if (iType >= DT_byte && iType <= DT_boolean) {
			iType += DT_Byte - DT_byte;
		}
		return iType;
	}
	
	public static final int DT_Unknown = 0;
	
	public static final int DT_byte = 1;
	public static final int DT_short = 2;
	public static final int DT_int = 3;
	public static final int DT_long = 4;
	public static final int DT_float = 5;
	public static final int DT_double = 6;
	public static final int DT_char = 7;
	public static final int DT_boolean = 8;
	//public static final int DT_array = 9;
	
	public static final int DT_Byte = 10;
	public static final int DT_Short = 11;
	public static final int DT_Integer = 12;
	public static final int DT_Long = 13;
	public static final int DT_Float = 14;
	public static final int DT_Double = 15;
	public static final int DT_Character = 16;
	public static final int DT_Boolean = 17;
	
	public static final int DT_String = 20;
	public static final int DT_BigInteger = 21;
	public static final int DT_BigDecimal = 22;
	public static final int DT_Date = 23;
	public static final int DT_Time = 24;
	public static final int DT_DateTime = 25;
	
	public static final int DT_Array = 30;
	public static final int DT_List = 31;
	//public static final int DT_ArrayList = 32;
	//public static final int DT_LinkedList = 33;
	public static final int DT_Map = 34;
	//public static final int DT_HashMap = 35;
	//public static final int DT_Hashtable = 36;
	public static final int DT_Set = 37;
	//public static final int DT_HashSet = 38;
	
	public static final int DT_Object = 40;
	public static final int DT_Class = 41;
	
	public static final int DT_UserDefine = 50;
	
	private static Map<String, Integer> dataTypeMap = new Hashtable<String, Integer>();
	static {
		dataTypeMap.put("byte", DT_byte);
		dataTypeMap.put("Byte", DT_Byte);
		dataTypeMap.put("short", DT_short);
		dataTypeMap.put("Short", DT_Short);
		dataTypeMap.put("int", DT_int);
		dataTypeMap.put("Integer", DT_Integer);
		dataTypeMap.put("long", DT_long);
		dataTypeMap.put("Long", DT_Long);
		dataTypeMap.put("boolean", DT_boolean);
		dataTypeMap.put("Boolean", DT_Boolean);
		dataTypeMap.put("char", DT_char);
		dataTypeMap.put("Character", DT_Character);
		dataTypeMap.put("float", DT_float);
		dataTypeMap.put("Float", DT_Float);
		dataTypeMap.put("double", DT_double);
		dataTypeMap.put("Double", DT_Double);
		dataTypeMap.put("BigInteger", DT_BigInteger);
		dataTypeMap.put("BigDecimal", DT_BigDecimal);
		dataTypeMap.put("String", DT_String);
		dataTypeMap.put("Date", DT_Date);
		dataTypeMap.put("Time", DT_Time);
		dataTypeMap.put("Timestamp", DT_DateTime);
		dataTypeMap.put("List", DT_List);
		dataTypeMap.put("ArrayList", DT_List);
		dataTypeMap.put("LinkedList", DT_List);
		dataTypeMap.put("Map", DT_Map);
		dataTypeMap.put("HashMap", DT_Map);
		dataTypeMap.put("Hashtable", DT_Map);
		dataTypeMap.put("Set", DT_Set);
		dataTypeMap.put("HashSet", DT_Set);
		dataTypeMap.put("Object", DT_Object);
		dataTypeMap.put("Class", DT_Class);
	}

}
