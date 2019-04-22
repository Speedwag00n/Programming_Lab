package lab.objects.items.miningInstruments;

import lab.objects.items.Item;

import java.io.Serializable;

public class MiningInstrument extends Item implements Serializable {

    public enum Material {
        STEEL("Steel"),
        IRON("Iron"),
        STONE("Stone"),
        WOOD("Wood"),
        PLASTIC("Plastic");

        private String name;

        private Material(String aName){
            name = aName;
        }

        public static Material fromStringToMaterial(String name){
            name = name.trim().toLowerCase();
            switch (name){
                case "steel":
                    return Material.STEEL;
                case "iron":
                    return Material.IRON;
                case "stone":
                    return Material.STONE;
                case "wood":
                    return Material.WOOD;
                case "plastic":
                    return Material.PLASTIC;
            }
            return null;
        }

        public String fromMaterialToString(){
            return name;
        }
    }

    private float powerCoefficient = 0.1F;

    public MiningInstrument(){

    }

    public MiningInstrument(float aPowerCoefficient){
        try {
            setPowerCoefficient(aPowerCoefficient);
        }
        catch (IncorrectPowerCoefficientException e){
            System.out.println("Возникло исключение " + e);
        }
    }

    public MiningInstrument(float aPowerCoefficient, String aName){
        super(aName);
        try {
            setPowerCoefficient(aPowerCoefficient);
        }
        catch (IncorrectPowerCoefficientException e){
            System.out.println("Возникло исключение " + e);
        }
    }

    public float getPowerCoefficient(){
        return powerCoefficient;
    }

    public void setPowerCoefficient(float aPowerCoefficient) throws IncorrectPowerCoefficientException {
        if ((aPowerCoefficient < 0)||(aPowerCoefficient > 1))
            throw new IncorrectPowerCoefficientException("Введите корректный коэффициент для инструмента!", aPowerCoefficient);
        else
            powerCoefficient = aPowerCoefficient;
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj))
            return false;
        MiningInstrument object = (MiningInstrument) obj;
        if (!(Math.abs(powerCoefficient - object.powerCoefficient) < 0.000000001))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "Горнодобывающий инструмент " + getName() + ". Коэффициент инструмента: " + getPowerCoefficient() + ".";
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = super.hashCode();
        result = result*prime + (int)powerCoefficient*1000000;
        return result;
    }

}
