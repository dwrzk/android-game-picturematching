/**
 * 
 */
package com.chengzhang.picturematching.view;

import java.util.List;

import com.chengzhang.picturematching.bom.LinkInfo;
import com.chengzhang.picturematching.bom.Piece;
import com.chengzhang.picturematching.common.GameService;
import com.chengzhang.picturematching.utils.ArrayUtils;
import com.chengzhang.picturematching.utils.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Jackie
 * 
 */
public class GameView extends View {
	/* logic class for game. */
	private GameService _gameService;
	/* selected piece. */
	private Piece _selectedPiece;
	/* link info. */
	private LinkInfo _linkInfo;
	/* Paint> */
	private Paint _paint;
	private Bitmap _selectedImage;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setViewProperties();
		_selectedImage = ImageUtils.getSelectedImage(context);
	}

	/**
	 * set view paint properties
	 */
	private void setViewProperties() {
		_paint = new Paint();
		/* color for link line. */
		_paint.setColor(Color.BLUE);
		/* set strokeWidth. */
		_paint.setStrokeWidth(3);
	}

	/**
	 * setter
	 * 
	 * @param linkInfo
	 */
	public void setLinkInfo(LinkInfo linkInfo) {
		_linkInfo = linkInfo;
	}

	/**
	 * setter
	 * 
	 * @param gameService
	 */
	public void setGameService(GameService gameService) {
		_gameService = gameService;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (_gameService == null) {
			return;
		}
		Piece[][] pieces = _gameService.getPieces();
		if (!ArrayUtils.isNullOrEmpty(pieces)) {
			for (int rowIndex = 0; rowIndex < pieces.length; rowIndex++) {
				for (int colIndex = 0; colIndex < pieces[rowIndex].length; colIndex++) {
					if (pieces[rowIndex][colIndex] != null) {
						Piece piece = pieces[rowIndex][colIndex];
						canvas.drawBitmap(piece.getImage(), piece.getBeginX(),
								piece.getBeginY(), null);
					}
				}
			}
		}

		/* draw link info. */
		if (_linkInfo != null) {
			drawLine(_linkInfo, canvas);
			_linkInfo = null;
		}

		/* draw selected piece. */
		if (_selectedPiece != null) {
			canvas.drawBitmap(_selectedImage, _selectedPiece.getBeginX(),
					_selectedPiece.getBeginY(), null);
		}
	}

	private void drawLine(LinkInfo linkInfo, Canvas canvas) {
		/* get all linkPointpositions */
		List<Point> points = linkInfo.getLinkPoints();

		for (int i = 0; i < points.size() - 1; i++) {
			Point currentPoint = points.get(i);
			Point nextPoint = points.get(i + 1);

			canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x,
					nextPoint.y, _paint);
		}
	}

	/**
	 * setter
	 * 
	 * @param selectedPiece
	 */
	public void setSelectedPiece(Piece selectedPiece) {
		_selectedPiece = selectedPiece;
	}

	/**
	 * start the game
	 */
	public void startGame() {
		_gameService.start();
		postInvalidate();
	}

}
