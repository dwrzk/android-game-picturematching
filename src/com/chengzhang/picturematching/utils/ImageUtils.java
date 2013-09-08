/**
 * 
 */
package com.chengzhang.picturematching.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.chengzhang.picturematching.R;
import com.chengzhang.picturematching.bom.PieceImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Jackie Utils class object for image
 */
public class ImageUtils {
	private static List<Integer> _ImageValues = getImageValues();

	/**
	 * 
	 * @return all picture IDs
	 */
	public static List<Integer> getImageValues() {
		try {
			Field[] drawabaleFields = R.drawable.class.getFields();
			List<Integer> resaonceValues = new ArrayList<Integer>();
			for (Field field : drawabaleFields) {
				if (field.getName().indexOf("p_") != -1) { //$NON-NLS-1$
					resaonceValues.add(field.getInt(R.drawable.class));
				}
			}
			return resaonceValues;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	/**
	 * 
	 * @param sourceValues
	 * @param size
	 * @return random images in list
	 */
	public static List<Integer> getRandomValues(List<Integer> sourceValues,
			int size) {
		Random random = new Random();

		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			try {
				int index = random.nextInt(sourceValues.size());
				Integer image = sourceValues.get(index);
				result.add(image);
			} catch (IndexOutOfBoundsException e) {
				return result;
			}
		}
		return result;
	}

	/**
	 * 
	 * @param size
	 * @return return playable values
	 */
	private static List<Integer> getPlayValues(int size) {
		if (size % 2 != 0) {
			size += 1;
		}

		List<Integer> playImageValues = getRandomValues(_ImageValues, size / 2);
		playImageValues.addAll(playImageValues);
		Collections.shuffle(playImageValues);
		return playImageValues;
	}

	/**
	 * 
	 * @param context
	 * @param size
	 * @return play image in list
	 */
	public static List<PieceImage> getPlayImages(Context context, int size) {
		List<Integer> resourceValues = getPlayValues(size);
		List<PieceImage> result = new ArrayList<PieceImage>();

		for (Integer value : resourceValues) {
			Bitmap image = BitmapFactory.decodeResource(context.getResources(),
					value);
			PieceImage pieceImage = new PieceImage(image, value);
			result.add(pieceImage);
		}
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return selected image
	 */
	public static Bitmap getSelectedImage(Context context) {
		Bitmap selectedImage = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.selected);
		return selectedImage;
	}

}
