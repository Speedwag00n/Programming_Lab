package lab.locations.position;

import lab.locations.position.CoordsPair;

import java.io.Serializable;

/**
 * Objects of this class encapsulate two pairs of coordinates to describe rectangle where location exists on plane.
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */

public class RectanglePosition implements Serializable {

    private CoordsPair leftBottomPoint;
    private CoordsPair rightTopPoint;

    /**
     * Constructor for create object of position
     * @param aLeftBottomPoint Left-bottom point of rectangle that describes position of location on plane.
     * @param aRightTopPoint Right-top point of rectangle that describes position of location on plane.
     */

    public RectanglePosition(CoordsPair aLeftBottomPoint, CoordsPair aRightTopPoint){

        leftBottomPoint = aLeftBottomPoint;
        rightTopPoint = aRightTopPoint;

    }

    public CoordsPair getLeftBottomPoint(){
        return leftBottomPoint;
    }

    public CoordsPair getRightTopPoint(){
        return rightTopPoint;
    }

    @Override
    public String toString() {
        return "Левая нижняя точка: " + leftBottomPoint + ", правая верхняя точка: " + rightTopPoint;
    }
}
