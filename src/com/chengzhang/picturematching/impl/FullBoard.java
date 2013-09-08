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
public class FullBoard extends AbstractBoard {

	@Override
	protected List<Piece> createPieces(GameConf gameConf, Piece[][] pieces) {
		List<Piece> nonNullPieces = new ArrayList<Piece>();
		for (int rowIndex = 1; rowIndex < pieces.length - 1; rowIndex++) {
			for (int colIndex = 1; colIndex < pieces[colIndex].length - 1; colIndex++) {
				Piece newPiece = new Piece(rowIndex, colIndex);
				nonNullPieces.add(newPiece);
			}
		}
		return nonNullPieces;
	}
}
