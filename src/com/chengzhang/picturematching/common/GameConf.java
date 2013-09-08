/**
 * 
 */
package com.chengzhang.picturematching.common;

import android.content.Context;

/**
 * @author Jackie
 * 
 */
public class GameConf {
	public static final int PIECE_WIDTH = 40;
	public static final int PIECE_HEIGHT = 40;

	public static int DEFAULT_TIME = 100;

	private int _xSize, _ySize;

	private int _beginImageX, _beginImageY;

	private long _gameTime;

	private Context _context;

	public GameConf(int xSize, int ySize, int beginImageX, int beginImageY,
			long gameTime, Context context) {
		_xSize = xSize;
		_ySize = ySize;
		_beginImageX = beginImageX;
		_beginImageY = beginImageY;
		_gameTime = gameTime;
		_context = context;
	}

	public int getXSize() {
		return _xSize;
	}

	public int getYSize() {
		return _ySize;
	}

	public int getBeginImageX() {
		return _beginImageX;
	}

	public int getBeginImageY() {
		return _beginImageY;
	}

	public long getGameTime() {
		return _gameTime;
	}

	public Context getContext() {
		return _context;
	}
}
