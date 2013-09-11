/**
 * 
 */
package com.chengzhang.picturematching.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.chengzhang.picturematching.bom.LinkInfo;
import com.chengzhang.picturematching.bom.Piece;
import com.chengzhang.picturematching.common.AbstractBoard;
import com.chengzhang.picturematching.common.GameConf;
import com.chengzhang.picturematching.common.GameService;
import com.chengzhang.picturematching.utils.ArrayUtils;

import android.graphics.Point;

/**
 * @author Jackie
 * 
 */
public class GameEngine implements GameService {

	private Piece[][] _pieces;

	private GameConf _config;

	public GameEngine(GameConf config) {
		_config = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.GameService#start()
	 */
	@Override
	public void start() {
		AbstractBoard board = null;
		Random random = new Random();
		int index = random.nextInt(4);

		switch (index) {
		case 0:
			board = new VerticalBoard();
			break;
		case 1:
			board = new HorizontalBoard();
			break;
		default:
			board = new FullBoard();
			break;
		}
		_pieces = board.create(_config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.GameService#getPieces()
	 */
	@Override
	public Piece[][] getPieces() {
		return _pieces;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.GameService#hasPieces()
	 */
	@Override
	public boolean hasPieces() {
		return !ArrayUtils.isNullOrEmpty(_pieces);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.GameService#findPiece(float, float)
	 */
	@Override
	public Piece findPiece(float touchX, float touchY) {
		int relativeX = (int) touchX - _config.getBeginImageX();
		int relativeY = (int) touchY - _config.getBeginImageY();

		if (relativeX < 0 || relativeY < 0) {
			return null;
		}

		int indexX = getIndex(relativeX, GameConf.PIECE_WIDTH);
		int indexY = getIndex(relativeY, GameConf.PIECE_HEIGHT);

		if (indexX < 0 || indexY < 0) {
			return null;
		}

		if (indexX >= _config.getXSize() || indexY >= _config.getYSize()) {
			return null;
		}

		return _pieces[indexX][indexY];
	}

	private static int getIndex(int relative, int size) {
		int index = -1;
		if (relative % size == 0) {
			index = relative / size - 1;
		} else {
			index = relative / size;
		}
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.GameService#link(bom.Piece, bom.Piece)
	 */
	@Override
	public LinkInfo link(Piece p1, Piece p2) {
		List<Point> list = new ArrayList<Point>();
		// Case 1 : same piece
		if (p1.equals(p2)) {
			return null;
		}
		
		if (!p1.isSameImage(p2))
			return null;
		
		if (p2.getIndexX() < p1.getIndexX()) {
			return link(p2, p1);
		}
		Point p1_Point = p1.getCenter();
		Point p2_Point = p2.getCenter();

		if (p1.getIndexY() == p2.getIndexY()) {
			if (!isXBlock(p1_Point, p2_Point, GameConf.PIECE_WIDTH)) {
				list.add(p1_Point);
				list.add(p2_Point);
				return new LinkInfo(list);
			}
		}

		if (p1.getIndexX() == p2.getIndexX()) {
			if (!isYBlock(p1_Point, p2_Point, GameConf.PIECE_HEIGHT)) {
				list.add(p1_Point);
				list.add(p2_Point);
				return new LinkInfo(list);
			}
		}

		Point cornerPoint = getCornerPoint(p1_Point, p2_Point,
				GameConf.PIECE_WIDTH, GameConf.PIECE_HEIGHT);
		if (cornerPoint != null) {
			list.add(p1_Point);
			list.add(cornerPoint);
			list.add(p2_Point);
			return new LinkInfo(list);
		}

		Map<Point, Point> turns = getLinkPoints(p1_Point, p2_Point,
				GameConf.PIECE_WIDTH, GameConf.PIECE_HEIGHT);
		if (!ArrayUtils.isNullOrEmpty(turns)) {
			return getShortcut(p1_Point, p2_Point, turns,
					getDistance(p1_Point, p2_Point));
		}
		return null;
	}

	/**
	 * 
	 * @param p
	 * @param min
	 * @param pieceWidth
	 * @return all images at the left of p
	 */
	private List<Point> getLeftChanel(Point p, int min, int pieceWidth) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.x - pieceWidth; i >= min; i = i - pieceWidth) {
			if (hasPiece(i, p.y)) {
				return result;
			}
			result.add(new Point(i, p.y));
		}
		return result;
	}

	/**
	 * 
	 * @param p
	 * @param max
	 * @param pieceWidth
	 * @return all images at the right of p
	 */
	private List<Point> getRightChanel(Point p, int max, int pieceWidth) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.x + pieceWidth; i <= max; i = i + pieceWidth) {
			if (hasPiece(i, p.y)) {
				return result;
			}
			result.add(new Point(i, p.y));
		}
		return result;
	}

	/**
	 * 
	 * @param p
	 * @param min
	 * @param pieceHeight
	 * @return all images above p
	 */
	private List<Point> getUpChanel(Point p, int min, int pieceHeight) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.y - pieceHeight; i >= min; i = i - pieceHeight) {
			if (hasPiece(p.x, i)) {
				return result;
			}
			result.add(new Point(p.x, i));
		}
		return result;
	}

	/**
	 * 
	 * @param p
	 * @param max
	 * @param pieceHeight
	 * @return all images below p
	 */
	private List<Point> getDownChanel(Point p, int max, int pieceHeight) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.y + pieceHeight; i <= max; i = i + pieceHeight) {
			if (hasPiece(p.x, i)) {
				return result;
			}
			result.add(new Point(p.x, i));
		}
		return result;
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param pieceWidth
	 * @return true if any image is in the x path between p1 and p2
	 */
	private boolean isXBlock(Point p1, Point p2, int pieceWidth) {
		if (p2.x < p1.x) {
			return isXBlock(p2, p1, pieceWidth);
		}
		for (int i = p1.x + pieceWidth; i < p2.x; i = i + pieceWidth) {
			if (hasPiece(i, p1.y)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param pieceHeight
	 * @return true if any image is in the y path between p1 and p2
	 */
	private boolean isYBlock(Point p1, Point p2, int pieceHeight) {
		if (p2.y < p1.y) {
			return isYBlock(p2, p1, pieceHeight);
		}
		for (int i = p1.y + pieceHeight; i < p2.y; i = i + pieceHeight) {
			if (hasPiece(p1.x, i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param p1Chanel
	 * @param p2Chanel
	 * @return the wrap point between 2 chanels
	 */
	private static Point getWrapPoint(List<Point> p1Chanel, List<Point> p2Chanel) {
		for (int i = 0; i < p1Chanel.size(); i++) {
			Point temp1 = p1Chanel.get(i);
			for (int j = 0; j < p2Chanel.size(); j++) {
				Point temp2 = p2Chanel.get(j);
				if (temp1.equals(temp2)) {
					return temp1;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param pieceWidth
	 * @param pieceHeight
	 * @return corner point between p1 and p2
	 */
	private Point getCornerPoint(Point p1, Point p2, int pieceWidth,
			int pieceHeight) {
		if (isLeftUp(p1, p2) || isLeftDown(p1, p2)) {
			return getCornerPoint(p2, p1, pieceWidth, pieceHeight);
		}

		List<Point> point1RightChanel = getRightChanel(p1, p2.x, pieceWidth);
		List<Point> point1UpChanel = getUpChanel(p1, p2.y, pieceHeight);
		List<Point> point1DownChanel = getDownChanel(p1, p2.y, pieceHeight);

		List<Point> point2LeftChanel = getLeftChanel(p2, p1.x, pieceWidth);
		List<Point> point2UpChanel = getUpChanel(p2, p1.y, pieceHeight);
		List<Point> point2DownChanel = getDownChanel(p2, p1.y, pieceHeight);

		if (isRightUp(p1, p2)) {
			Point linkPoint1 = getWrapPoint(point1RightChanel, point2DownChanel);
			Point linkPoint2 = getWrapPoint(point1UpChanel, point2LeftChanel);
			return (linkPoint1 == null) ? linkPoint2 : linkPoint1;
		}

		if (isRightDown(p1, p2)) {
			Point linkPoint1 = getWrapPoint(point1RightChanel, point2UpChanel);
			Point linkPoint2 = getWrapPoint(point1DownChanel, point2LeftChanel);
			return (linkPoint1 == null) ? linkPoint2 : linkPoint1;
		}

		return null;
	}

	private static boolean isLeftUp(Point p1, Point p2) {
		return (p2.x < p1.x && p2.y < p1.y);
	}

	private static boolean isLeftDown(Point p1, Point p2) {
		return (p2.x < p1.x && p2.y > p1.y);
	}

	private static boolean isRightUp(Point p1, Point p2) {
		return (p2.x > p1.x && p2.y < p1.y);
	}

	private static boolean isRightDown(Point p1, Point p2) {
		return (p2.x > p1.x && p2.y > p1.y);
	}

	private boolean hasPiece(int x, int y) {
		if (findPiece(x, y) == null)
			return false;
		return true;
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param pieceWidth
	 * @param pieceHeight
	 * @return all link points between p1 and p2
	 */
	private Map<Point, Point> getLinkPoints(Point p1, Point p2, int pieceWidth,
			int pieceHeight) {
		Map<Point, Point> result = new HashMap<Point, Point>();

		List<Point> p1RightChanel = getRightChanel(p1, p2.x, pieceWidth);
		List<Point> p1UpChanel = getUpChanel(p1, p2.y, pieceHeight);
		List<Point> p1DownChanel = getDownChanel(p1, p2.y, pieceHeight);

		List<Point> p2LeftChanel = getLeftChanel(p2, p1.x, pieceWidth);
		List<Point> p2UpChanel = getUpChanel(p2, p1.y, pieceHeight);
		List<Point> p2DownChanel = getDownChanel(p2, p1.y, pieceHeight);

		int heightMax = (_config.getYSize() + 1) * pieceHeight
				+ _config.getBeginImageY();
		int widthMax = (_config.getXSize() + 1) * pieceWidth
				+ _config.getBeginImageX();

		if (isLeftUp(p1, p2) || isLeftDown(p1, p2)) {
			return getLinkPoints(p2, p1, pieceWidth, pieceHeight);
		}

		if (p1.y == p2.y) {
			p1UpChanel = getUpChanel(p1, 0, pieceHeight);
			p2UpChanel = getUpChanel(p2, 0, pieceHeight);

			p1DownChanel = getDownChanel(p1, heightMax, pieceHeight);
			p2DownChanel = getDownChanel(p2, heightMax, pieceHeight);

			Map<Point, Point> upLinkPoints = getXLinkPoints(p1UpChanel,
					p2UpChanel, pieceHeight);
			Map<Point, Point> downLinkPoints = getXLinkPoints(p1DownChanel,
					p2DownChanel, pieceHeight);

			result.putAll(upLinkPoints);
			result.putAll(downLinkPoints);
			return result;
		}

		if (p1.x == p2.x) {
			p1RightChanel = getRightChanel(p1, widthMax, pieceWidth);
			List<Point> p2RightChanel = getRightChanel(p2, widthMax, pieceWidth);

			List<Point> p1LeftChanel = getLeftChanel(p1, 0, pieceWidth);
			p2LeftChanel = getLeftChanel(p2, 0, pieceWidth);

			Map<Point, Point> rightLinkPoints = getYLinkPoints(p1RightChanel,
					p2RightChanel, pieceWidth);
			Map<Point, Point> leftLinkPoints = getYLinkPoints(p1LeftChanel,
					p2LeftChanel, pieceWidth);

			result.putAll(leftLinkPoints);
			result.putAll(rightLinkPoints);

			return result;
		}

		Map<Point, Point> upDownLinkPoints = new HashMap<Point, Point>();
		if (isRightUp(p1, p2)) {
			upDownLinkPoints = getXLinkPoints(p1UpChanel, p2DownChanel,
					pieceWidth);
		}

		if (isRightDown(p1, p2)) {
			upDownLinkPoints = getXLinkPoints(p1DownChanel, p2UpChanel,
					pieceWidth);
		}
		Map<Point, Point> rightLeftLinkPoints = getYLinkPoints(p1RightChanel,
				p2LeftChanel, pieceHeight);

		p1UpChanel = getUpChanel(p1, 0, pieceHeight);
		p2UpChanel = getUpChanel(p2, 0, pieceHeight);

		Map<Point, Point> upUpLinkPoints = getXLinkPoints(p1UpChanel,
				p2UpChanel, pieceWidth);
		p1DownChanel = getDownChanel(p1, heightMax, pieceHeight);
		p2DownChanel = getDownChanel(p2, heightMax, pieceHeight);
		Map<Point, Point> downDownLinkPoints = getXLinkPoints(p1DownChanel,
				p2DownChanel, pieceWidth);

		p1RightChanel = getRightChanel(p1, widthMax, pieceWidth);
		List<Point> p2RightChanel = getLeftChanel(p2, widthMax, pieceWidth);
		Map<Point, Point> rightRightLinkPoints = getYLinkPoints(p1RightChanel,
				p2RightChanel, pieceHeight);

		List<Point> p1LeftChanel = getLeftChanel(p1, 0, pieceWidth);
		p2LeftChanel = getLeftChanel(p2, 0, pieceWidth);

		Map<Point, Point> leftLeftLinkPoints = getYLinkPoints(p1LeftChanel,
				p2LeftChanel, pieceHeight);

		result.putAll(upUpLinkPoints);
		result.putAll(downDownLinkPoints);
		result.putAll(rightRightLinkPoints);
		result.putAll(leftLeftLinkPoints);
		result.putAll(rightLeftLinkPoints);
		result.putAll(upDownLinkPoints);
		return result;
	}

	private Map<Point, Point> getYLinkPoints(List<Point> p1Chanel,
			List<Point> p2Chanel, int pieceHeight) {
		Map<Point, Point> result = new HashMap<Point, Point>();

		for (int i = 0; i < p1Chanel.size(); i++) {
			Point temp1 = p1Chanel.get(i);
			for (int j = 0; j < p2Chanel.size(); j++) {
				Point temp2 = p2Chanel.get(j);
				if (temp1.x == temp2.x) {
					if (!isYBlock(temp1, temp2, pieceHeight)) {
						result.put(temp1, temp2);
					}
				}
			}
		}
		return result;
	}

	private Map<Point, Point> getXLinkPoints(List<Point> p1Chanel,
			List<Point> p2Chanel, int pieceWidth) {
		Map<Point, Point> result = new HashMap<Point, Point>();

		for (int i = 0; i < p1Chanel.size(); i++) {
			Point temp1 = p1Chanel.get(i);
			for (int j = 0; j < p2Chanel.size(); j++) {
				Point temp2 = p2Chanel.get(j);
				if (temp1.y == temp2.y) {
					if (!isXBlock(temp1, temp2, pieceWidth)) {
						result.put(temp1, temp2);
					}
				}
			}
		}
		return result;
	}

	private static LinkInfo getShortcut(Point p1, Point p2,
			Map<Point, Point> turns, int shortDistance) {
		List<LinkInfo> infos = new ArrayList<LinkInfo>();
		List<Point> points = new ArrayList<Point>();

		for (Point point1 : turns.keySet()) {
			Point point2 = turns.get(point1);
			points.clear();
			points.add(p1);
			points.add(point1);
			points.add(point2);
			points.add(p2);
			infos.add(new LinkInfo(points));
		}

		return getShortcut(infos, shortDistance);
	}

	private static LinkInfo getShortcut(List<LinkInfo> infos, int shortDistance) {
		int temp1 = 0;
		LinkInfo result = null;
		for (int i = 0; i < infos.size(); i++) {
			LinkInfo info = infos.get(i);
			int distance = countAll(info.getLinkPoints());
			if (i == 0) {
				temp1 = distance - shortDistance;

			}
			if (distance - shortDistance < temp1) {
				temp1 = distance - shortDistance;

			}
			result = info;
		}
		return result;
	}

	private static int countAll(List<Point> points) {
		int result = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			Point point1 = points.get(i);

			Point point2 = points.get(i + 1);
			result += getDistance(point1, point2);
		}
		return result;
	}

	private static int getDistance(Point p1, Point p2) {
		int xDistance = Math.abs(p1.x - p2.x);
		int yDistance = Math.abs(p1.y - p2.y);

		return xDistance + yDistance;
	}
}
