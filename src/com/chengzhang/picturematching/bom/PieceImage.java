/**
 * 
 */
package com.chengzhang.picturematching.bom;

import android.graphics.Bitmap;

/**
 * @author Jackie
 * 
 */
public class PieceImage {
	/* Image for matching */
	private Bitmap _image;
	/* Image ID. */
	private int _imageID;

	/**
	 * constructor
	 * 
	 * @param image
	 * @param imageID
	 */
	public PieceImage(Bitmap image, int imageID) {
		_image = image;
		_imageID = imageID;
	}

	/**
	 * setter
	 * 
	 * @param image
	 */
	public void setImage(Bitmap image) {
		_image = image;
	}

	/**
	 * getter
	 * 
	 * @return Image
	 */
	public Bitmap getImage() {
		return _image;
	}

	/**
	 * getter
	 * 
	 * @return image Id
	 */
	public int getImageID() {
		return _imageID;
	}

	/**
	 * setter
	 * 
	 * @param imageID
	 */
	public void setImageID(int imageID) {
		_imageID = imageID;
	}

}
