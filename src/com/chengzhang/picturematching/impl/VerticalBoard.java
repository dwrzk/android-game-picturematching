/**
 * 
 */
package com.chengzhang.picturematching.impl;

import java.util.ArrayList;
import java.util.List;

import com.chengzhang.picturematching.bom.Piece;
import com.chengzhang.picturematching.common.AbstractBoard;
import com.chengzhang.picturematching.common.GameConf;

/**
 * @author Jackie
 * 
 */
public class VerticalBoard extends AbstractBoard {

	@Override
	protected List<Piece> createPieces(GameConf gameConf, Piece[][] pieces) {
		List<Piece> nonNullPieces = new ArrayList<Piece>();
		for (int rowIndex = 0; rowIndex < pieces.length; rowIndex++) {
			for (int colIndex = 0; colIndex < pieces[rowIndex].length; colIndex++) {
				if (rowIndex % 2 == 0) {
					Piece newPiece = new Piece(rowIndex, colIndex);
					nonNullPieces.add(newPiece);
				}
			}
		}
		return nonNullPieces;
	}

}
