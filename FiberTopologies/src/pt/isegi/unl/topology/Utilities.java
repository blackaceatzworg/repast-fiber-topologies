/**
 * 
 */
package pt.isegi.unl.topology;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;

import javax.units.NonSI;
import javax.units.SI;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author pmendes
 *
 */
public class Utilities {

	/**
	 * Returns the angle of the vector from p0 to p1. The angle will be between
	 * -Pi and Pi.
	 * 
	 * @return the angle (in radians) that p0p1 makes with the positive x-axis.
	 */
	public static double angleInRadians(Coordinate p0, Coordinate p1) {
		return Math.atan2(p1.y - p0.y, p1.x - p0.x);
	}
	
	
	/**
	 * Converts a distance (passed as lat/log decimal degrees) to meters
	 * @param dist The distance in decimal degrees
	 * @return the equivalent distance in meters
	 */
	public static double convertToMeters(double distance) {
		return NonSI.NAUTICAL_MILE.getConverterTo(SI.METER).convert(distance*60);
	}
	
	/**
	 * Converts a distance (passed as meters) to lat/log decimal degrees
	 * 
	 * @param dist The distance in decimal degrees
	 * @return the equivalent distance in meters
	 */
	public static double convertFromMeters(double distance) {
		return SI.METER.getConverterTo(NonSI.NAUTICAL_MILE).convert(distance)/(60.0);
	}
	
	/**
	 * This method removes coordinate replication
	 */
	public static List<Coordinate> removeDuplicateCoordinates(List<Coordinate> coordinates) {
		LinkedHashSet<Coordinate> set = new LinkedHashSet<Coordinate>();
		for (Coordinate c : coordinates)
			set.add(c);

		coordinates = new Vector<Coordinate>();
		for (Coordinate c : set) {
			coordinates.add(c);
		}		
		return coordinates;
	}

}
