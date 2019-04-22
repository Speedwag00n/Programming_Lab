package lab.objects.items.rock;

import lab.objects.items.Item;
import lab.objects.items.NegativeWeightException;
import lab.skills.MiningSourceException;

import java.io.Serializable;
import java.util.ArrayList;

public class Rock extends Item implements Minable, Serializable {

    private float weightOfOre = 0;
    private float weightOfStone = 0;
    private int countOfPieces = 0;

    public Rock(float aWeightOfOre, float aWeightOfStone, String aName){
        super(aName);
        try {
            changeWeightOfRock(aWeightOfOre, aWeightOfStone);
        }
        catch (NegativeWeightException e) {
            System.out.println("Возникло исключение " + e);
        }
    }

    public float getWeightOfOre(){
        return weightOfOre;
    }

    public float getWeightOfStone(){
        return weightOfStone;
    }

    private void changeWeightOfRock(float aWeightOfOre, float aWeightOfStone) throws NegativeWeightException{
        if (aWeightOfOre < 0)
            throw new NegativeWeightException("Введите неотрицательный вес руды!", aWeightOfOre);
        else
            weightOfOre = aWeightOfOre;
        if (aWeightOfStone < 0)
            throw new NegativeWeightException("Введите неотрицательный вес камня!", aWeightOfStone);
        else
            weightOfStone = aWeightOfStone;
    }

    public void reduceWeightOfRock(float aWeightOfOre, float aWeightOfStone){
        if ((weightOfOre == 0)&&(weightOfStone == 0))
            throw new MiningSourceException("Скала окончательно разрушилась, отламывать нечего.");
        if(aWeightOfOre > weightOfOre)
            aWeightOfOre = weightOfOre;
        if(aWeightOfStone > weightOfStone)
            aWeightOfStone = weightOfStone;
        createPieces(aWeightOfOre, aWeightOfStone);
        if ((Math.random() + countOfPieces*0.1 > 1)||(weightOfStone == 0)||(weightOfOre == 0)){
            System.out.println("Скала разрушилась!");
            createPieces(weightOfOre, weightOfStone);
        }
    }

    private void createPieces(float aWeightOfOre, float aWeightOfStone){
        ArrayList<Item> pieces = new ArrayList<Item>();
        float weightOfPiece;
        float currentWeightOfOre = aWeightOfOre;
        float currentWeightOfStone = aWeightOfStone;
        while (currentWeightOfOre > 0){
            weightOfPiece = (float)Math.random()*aWeightOfOre;
            if (weightOfPiece > currentWeightOfOre)
                weightOfPiece = currentWeightOfOre;
            pieces.add(new Piece(weightOfPiece, "Кусок лунита"));
            currentWeightOfOre -= weightOfPiece;
            incCountOfPieces();
        }
        while (currentWeightOfStone > 0){
            weightOfPiece = (float)Math.random()*aWeightOfStone;
            if (weightOfPiece > currentWeightOfStone)
                weightOfPiece = currentWeightOfStone;
            pieces.add(new Piece(weightOfPiece, "Кусок камня"));
            currentWeightOfStone -= weightOfPiece;
            incCountOfPieces();
        }
        try {
            changeWeightOfRock(weightOfOre - aWeightOfOre, weightOfStone - aWeightOfStone);
        }
        catch (NegativeWeightException e) {
            System.out.println("Возникло исключение " + e);
        }
        getLocation().addItems(pieces);
        System.out.println("В скале осталось " + weightOfOre + " руды. В скале осталось " + weightOfStone + " камня.");
    }

    private void incCountOfPieces(){
        countOfPieces++;
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj))
            return false;
        Rock object = (Rock) obj;
        if (!(Math.abs(weightOfOre - object.weightOfOre) < 0.000000001))
            return false;
        if (!(Math.abs(weightOfStone - object.weightOfStone) < 0.000000001))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "Скала: " + getName() + ". Вес руды в скале: " + getWeightOfOre() + ". Вес камня в скале: " + getWeightOfStone() + ".";
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = super.hashCode();
        result = result*prime + (int)weightOfOre*1000000;
        result = result*prime + (int)weightOfStone*1000000;
        return result;
    }

    public static class Piece extends Item implements Serializable{

        private float weight = 0;

        public Piece(float aWeight){
            try {
                setWeight(aWeight);
            }
            catch (NegativeWeightException e){
                System.out.println("Возникло исключение " + e);
            }
        }

        public Piece(float aWeight, String aName){
            super(aName);
            try {
                setWeight(aWeight);
            }
            catch (NegativeWeightException e){
                System.out.println("Возникло исключение " + e);
            }
        }

        public float getWeight(){
            return weight;
        }

        public void setWeight(float aWeight) throws NegativeWeightException{
            if(aWeight < 0)
                throw new NegativeWeightException("Введите не отрицательный вес куска!", aWeight);
            else
                weight = aWeight;
        }

        @Override
        public boolean equals(Object obj){
            if(!super.equals(obj))
                return false;
            Piece object = (Piece) obj;
            if (!(Math.abs(weight - object.weight) < 0.000000001))
                return false;
            return true;
        }

        @Override
        public String toString(){
            return getName() + ". Вес: " + getWeight() + ".";
        }

        @Override
        public int hashCode(){
            final int prime = 17;
            int result = super.hashCode();
            result = result*prime + (int)weight*1000000;
            return result;
        }

    }

}
