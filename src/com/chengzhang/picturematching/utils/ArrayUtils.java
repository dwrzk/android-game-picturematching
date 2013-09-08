/**
 * 
 */
package com.chengzhang.picturematching.utils;

import java.util.Map;

/**
 * @author Jackie Utils class for array object
 */
public class ArrayUtils {

	/**
	 * 
	 * @param map
	 * @return true if map is null or empty
	 */
	public static boolean isNullOrEmpty(Map<?, ?> map) {
		return map == null || map.size() == 0;
	}

	/**
	 * 
	 * @param array
	 * @return true if array is null or empty
	 */
	public static boolean isNullOrEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 
	 * @param array
	 * @return true if array is null or empty
	 */
	public static boolean isNullOrEmpty(Object[][] array) {
		if (array == null) {
			return true;
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (array[i][j] != null) {
					return false;
				}
			}
		}
		return true;
	}
}
