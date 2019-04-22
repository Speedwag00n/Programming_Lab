package lab.locations.position;

import java.io.Serializable;

/**
 * Objects of this class encapsulate coordinates of point on plane.
 * @author Nemankov Ilia
 * @version 1.6.0
 * @since 1.6.0
 */


public class CoordsPair implements Serializable {

    private int x;
    private int y;

    /**
     * Constructor for create new point on plane.
     * @param aX X coordinate.
     * @param aY Y coordinate.
     */

    public CoordsPair(int aX, int aY){

        x = aX;
        y = aY;

    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    @Override
    public String toString() {
        return "X: " + getX() + ", Y: " + getY();
    }
}
