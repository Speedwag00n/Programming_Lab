package lab.objects.items.rock;

public interface Minable {

    float getWeightOfOre();
    float getWeightOfStone();
    void reduceWeightOfRock(float aWeightOfOre, float aWeightOfStone);

}
