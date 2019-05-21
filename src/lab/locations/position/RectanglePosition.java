package lab.locations.position;

import java.io.Serializable;

/**
 * Objects of this class encapsulate two pairs of coordinates to describe rectangle where location exists on plane.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */

public class RectanglePosition implements Serializable {

    private CoordsPair leftBottomPoint;
    private CoordsPair rightTopPoint;

    /**
     * Constructor for create object of position.
     *
     * @param aLeftBottomPoint Left-bottom point of rectangle that describes position of location on plane.
     * @param aRightTopPoint   Right-top point of rectangle that describes position of location on plane.
     */

    public RectanglePosition(CoordsPair aLeftBottomPoint, CoordsPair aRightTopPoint) {

        leftBottomPoint = aLeftBottomPoint;
        rightTopPoint = aRightTopPoint;

    }

    /**
     * Getter for receive coordinates pair of left-bottom point.
     *
     * @return coordinates pair of left-bottom point.
     */
    public CoordsPair getLeftBottomPoint() {
        return leftBottomPoint;
    }

    /**
     * Getter for receive coordinates pair of right-top point.
     *
     * @return coordinates pair of right-top point.
     */
    public CoordsPair getRightTopPoint() {
        return rightTopPoint;
    }

    /**
     * This method allows to receive rectangle position as a array of Integer's.
     * 0th and 1th elements is coordinates of left-bottom point. 2th and 3th elements is coordinates of right-top point.
     *
     * @return rectangle position as a array of Integer's.
     */
    public Integer[] asArray() {
        Integer[] result = new Integer[4];
        result[0] = leftBottomPoint.getX();
        result[1] = leftBottomPoint.getY();
        result[2] = rightTopPoint.getX();
        result[3] = rightTopPoint.getY();
        return result;
    }

    @Override
    public String toString() {
        return "Левая нижняя точка: " + leftBottomPoint + ", правая верхняя точка: " + rightTopPoint;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RectanglePosition object = (RectanglePosition) obj;
        if (!leftBottomPoint.equals(object.leftBottomPoint))
            return false;
        if (!rightTopPoint.equals(object.rightTopPoint))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = result * prime + ((leftBottomPoint == null) ? 0 : leftBottomPoint.hashCode());
        result = result * prime + ((rightTopPoint == null) ? 0 : rightTopPoint.hashCode());
        return result;
    }

}
