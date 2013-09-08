/**
 * 
 */
package com.chengzhang.picturematching.common;

import com.chengzhang.picturematching.bom.LinkInfo;
import com.chengzhang.picturematching.bom.Piece;

/**
 * @author Jackie
 * 
 */
public interface GameService {

	/**
	 * game start method
	 */
	void start();

	/**
	 * 
	 * @return game images
	 */
	Piece[][] getPieces();

	/**
	 * 
	 * @return true if game images is not null
	 */
	boolean hasPieces();

	/**
	 * 
	 * @param touchX
	 * @param touchY
	 * @return image based on position
	 */
	Piece findPiece(float touchX, float touchY);

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return link info between 2 images
	 */
	LinkInfo link(Piece p1, Piece p2);
}
