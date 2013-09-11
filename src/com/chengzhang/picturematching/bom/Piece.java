/**
 * 
 */
package com.chengzhang.picturematching.bom;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * @author Jackie
 * 
 */
public class Piece {
	/* Image class for matching */
	private PieceImage _image;
	/* X position */
	private int _beginX;
	/* Y position */
	private int _beginY;
	/* index in row. */
	private int _indexX;
	/* index in column */
	private int _indexY;

	/**
	 * Constructor
	 * 
	 * @param indexX
	 * @param indexY
	 */
	public Piece(int indexX, int indexY) {
		_indexX = indexX;
		_indexY = indexY;
	}

	/**
	 * setter
	 * 
	 * @param image
	 */
	public void setPieceImage(PieceImage image) {
		_image = image;
	}

	/**
	 * getter
	 * 
	 * @return piece image
	 */
	public PieceImage getPieceImage() {
		return _image;
	}

	public Bitmap getImage() {
		return _image.getImage();
	}

	/**
	 * 
	 * @return the x position
	 */
	public int getBeginX() {
		return _beginX;
	}

	/**
	 * setter
	 * 
	 * @param beginX
	 */
	public void setBeginX(int beginX) {
		_beginX = beginX;
	}

	/**
	 * 
	 * @return the y position
	 */
	public int getBeginY() {
		return _beginY;
	}

	/**
	 * setter
	 * 
	 * @param beginY
	 */
	public void setBeginY(int beginY) {
		_beginY = beginY;
	}

	public int getIndexX() {
		return _indexX;
	}

	public void setIndexX(int indexX) {
		_indexX = indexX;
	}

	public int getIndexY() {
		return _indexY;
	}

	public void setIndexY(int indexY) {
		_indexY = indexY;
	}
	
	public boolean isSameImage(Piece other)
	{
		if (_image == null)
		{
			if (other._image != null)
				return false;
		}
		return _image.getImageID() == other._image.getImageID();
	}

	public Point getCenter() {
		return new Point(getImage().getWidth() / 2 + getBeginX(), getBeginY()
				+ getImage().getHeight() / 2);
	}

}
