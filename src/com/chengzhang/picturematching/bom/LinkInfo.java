/**
 * 
 */
package com.chengzhang.picturematching.bom;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

/**
 * @author Jackie
 * 
 */
public class LinkInfo {
	/* List to save all link points. */
	private List<Point> _points = new ArrayList<Point>();

	/**
	 * constructor
	 * 
	 * @param startPoint
	 * @param endPoint
	 */
	public LinkInfo(List<Point> points) {
		_points.addAll(points);
	}

	/**
	 * 
	 * @return all link points
	 */
	public List<Point> getLinkPoints() {
		return _points;
	}
}
