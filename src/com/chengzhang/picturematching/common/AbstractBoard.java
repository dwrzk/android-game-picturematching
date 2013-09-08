/**
 * 
 */
package com.chengzhang.picturematching.common;

import java.util.List;

import com.chengzhang.picturematching.bom.Piece;
import com.chengzhang.picturematching.bom.PieceImage;
import com.chengzhang.picturematching.utils.ImageUtils;

/**
 * @author Jackie
 * 
 */
public abstract class AbstractBoard {
	protected abstract List<Piece> createPieces(GameConf gameConf,
			Piece[][] pieces);

	public Piece[][] create(GameConf gameConf) {
		/* create piece array from game config. */
		Piece[][] pieces = new Piece[gameConf.getXSize()][gameConf.getYSize()];
		/* return non-null piece list */
		List<Piece> nonNullPieces = createPieces(gameConf, pieces);
		List<PieceImage> playImages = ImageUtils.getPlayImages(
				gameConf.getContext(), nonNullPieces.size());
		int imageWidth = playImages.get(0).getImage().getWidth();
		int imageHeight = playImages.get(0).getImage().getHeight();

		for (int i = 0; i < nonNullPieces.size(); i++) {
			Piece piece = nonNullPieces.get(i);
			piece.setPieceImage(playImages.get(i));
			piece.setBeginX(piece.getIndexX() * imageWidth
					+ gameConf.getBeginImageX());
			piece.setBeginY(piece.getIndexY() * imageHeight
					+ gameConf.getBeginImageY());
			pieces[piece.getIndexX()][piece.getIndexY()] = piece;
		}
		return pieces;
	}
}
