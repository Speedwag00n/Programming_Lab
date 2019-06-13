package lab.objects.items.miningInstruments;

import lab.client.settings.Settings;
import lab.objects.items.Item;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Mining instrument item class.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.3.0
 */
public class MiningInstrument extends Item implements Serializable {

    /**
     * Enum of materials.
     */
    public enum Material {
        STEEL("Steel"),
        IRON("Iron"),
        STONE("Stone"),
        WOOD("Wood"),
        PLASTIC("Plastic");

        private String name;

        private Material(String aName) {
            name = aName;
        }

        /**
         * Method that allows to receive material by its name.
         *
         * @param name name of material (steel, iron, stone, wood, plastic).
         * @return appropriate material.
         */
        public static Material fromStringToMaterial(String name) {
            name = name.trim().toLowerCase();
            switch (name) {
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

        /**
         * Method that gets material name.
         *
         * @return material name.
         */
        public String fromMaterialToString() {
            return name;
        }
    }

    private float powerCoefficient = 0.1F;

    /**
     * Empty constructor of MiningInstrument.
     */
    public MiningInstrument() {

    }

    /**
     * Constructor of MiningInstrument that allows to set power coefficient.
     *
     * @param aPowerCoefficient power coefficient of mining instrument.
     */
    public MiningInstrument(float aPowerCoefficient) {
        try {
            setPowerCoefficient(aPowerCoefficient);
        } catch (IncorrectPowerCoefficientException e) {
            System.out.println("Возникло исключение " + e);
        }
    }

    /**
     * Constructor of MiningInstrument that allows to set power coefficient and name.
     *
     * @param aPowerCoefficient power coefficient of mining instrument.
     * @param aName             name of mining instrument.
     */
    public MiningInstrument(float aPowerCoefficient, String aName) {
        super(aName);
        try {
            setPowerCoefficient(aPowerCoefficient);
        } catch (IncorrectPowerCoefficientException e) {
            System.out.println("Возникло исключение " + e);
        }
    }

    /**
     * Getter to receive power coefficient of mining instrument.
     *
     * @return power coefficient of mining instrument
     */
    public float getPowerCoefficient() {
        return powerCoefficient;
    }

    /**
     * Setter to set power coefficient of mining instrument.
     *
     * @param aPowerCoefficient power coefficient of mining instrument
     * @throws IncorrectPowerCoefficientException if a power coefficient is incorrect.
     */
    public void setPowerCoefficient(float aPowerCoefficient) throws IncorrectPowerCoefficientException {
        if ((aPowerCoefficient < 0) || (aPowerCoefficient > 1))
            throw new IncorrectPowerCoefficientException("Введите корректный коэффициент для инструмента!", aPowerCoefficient);
        else
            powerCoefficient = aPowerCoefficient;
    }

    @Override
    public String getAttributesDescription() {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        StringBuilder description = new StringBuilder();
        description.append(resources.getString("object_item_mininginstrument_coefficient") + ": " + powerCoefficient + " ");

        return description.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        MiningInstrument object = (MiningInstrument) obj;
        if (!(Math.abs(powerCoefficient - object.powerCoefficient) < 0.000000001))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Горнодобывающий инструмент " + getName() + ". Коэффициент инструмента: " + getPowerCoefficient() + ".";
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = super.hashCode();
        result = result * prime + (int) powerCoefficient * 1000000;
        return result;
    }

}
