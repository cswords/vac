package edu.monash.it.student.util;

import java.util.EnumSet;

public final class EnumHelper {

	public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
		String text=name.trim().toLowerCase();
		for(final T e:EnumSet.allOf(enumType)){
			if(e.toString().toLowerCase().equals(text))
				return e;
		}
		return null;
	}
}
