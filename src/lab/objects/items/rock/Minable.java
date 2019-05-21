package lab.objects.items.rock;

/**
 * The Minable interface should be implemented by any class which can be mined
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.3.0
 */
public interface Minable {

    /**
     * Getter to receive weight of ore in object.
     *
     * @return weight of ore in object.
     */
    float getWeightOfOre();

    /**
     * Getter to receive weight of stone in object.
     *
     * @return weight of stone in object.
     */
    float getWeightOfStone();

    /**
     * Method that allows to reduce weight of ore and weight of stone.
     *
     * @param aWeightOfOre   number on which reduce weight of ore.
     * @param aWeightOfStone number on which reduce weight of stone.
     */
    void reduceWeightOfRock(float aWeightOfOre, float aWeightOfStone);

}
