package lab.locations.position;

import java.io.Serializable;

/**
 * Objects of this class encapsulate coordinates of point on plane.
 *
 * @author Nemankov Ilia
 * @version 1.6.0
 * @since 1.6.0
 */


public class CoordsPair implements Serializable {

    private int x;
    private int y;

    /**
     * Constructor for create new point on plane.
     *
     * @param aX X coordinate.
     * @param aY Y coordinate.
     */
    public CoordsPair(int aX, int aY) {

        x = aX;
        y = aY;

    }

    /**
     * Getter for receive X coordinate.
     *
     * @return X coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for receive Y coordinate.
     *
     * @return Y coordinat.
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "X: " + getX() + ", Y: " + getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CoordsPair object = (CoordsPair) obj;
        if (x != object.x)
            return false;
        if (y != object.y)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = result * prime + x;
        result = result * prime + y;
        return result;
    }

}
