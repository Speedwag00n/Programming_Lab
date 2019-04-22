package lab.objects.items.miningInstruments;

import lab.objects.items.Item;

import java.io.Serializable;

public class Hammer extends MiningInstrument implements Serializable {

    private Head head;
    private Handle handle;

    public Hammer(String name, Material headMaterial, Material handleMaterial){
        setName(name);
        head = new Head(headMaterial);
        handle = new Handle(handleMaterial);
        try {
            setPowerCoefficient(head.coefficient*0.8F + handle.coefficient*0.2F);
        }
        catch (IncorrectPowerCoefficientException e){
            System.out.println("Возникло исключение " + e);
        }
    }

    private class Head extends Item implements Serializable {
        private float coefficient;
        private Material material;
        public Head(Material aMaterial){
            super("Боёк");
            material = aMaterial;
            switch (aMaterial){
                case STEEL:
                    coefficient = 0.95F;
                    break;
                case IRON:
                    coefficient = 0.75F;
                    break;
                case STONE:
                    coefficient = 0.35F;
                    break;
                case WOOD:
                    coefficient = 0.1F;
                    break;
                case PLASTIC:
                    coefficient = 0.05F;
                    break;
                    default:
                        coefficient = 0F;
                        break;
            }
        }
        public float getCoefficient(){
            return coefficient;
        }

        public Material getMaterial(){
            return material;
        }

        @Override
        public boolean equals(Object obj){
            if(!super.equals(obj))
                return false;
            Head object = (Head) obj;
            if (!(Math.abs(coefficient - object.coefficient) < 0.000000001))
                return false;
            return true;
        }

        @Override
        public String toString(){
            return getName() + ". Коэффициент бойка молотка: " + getCoefficient() + ".";
        }

        @Override
        public int hashCode(){
            final int prime = 17;
            int result = super.hashCode();
            result = result*prime + (int)coefficient*1000000;
            return result;
        }
    }

    private class Handle extends Item implements Serializable {
        private float coefficient;
        private Material material;
        public Handle(Material aMaterial){
            material = aMaterial;
            switch (aMaterial){
                case PLASTIC:
                    coefficient = 0.95F;
                    break;
                case WOOD:
                    coefficient = 0.65F;
                    break;
                case IRON:
                    coefficient = 0.15F;
                    break;
                case STEEL:
                    coefficient = 0.1F;
                    break;
                case STONE:
                    coefficient = 0.05F;
                    break;
                    default:
                        coefficient = 0F;
                        break;
            }
        }
        public float getCoefficient(){
            return coefficient;
        }

        public Material getMaterial(){
            return material;
        }

        @Override
        public boolean equals(Object obj){
            if(!super.equals(obj))
                return false;
            Handle object = (Handle) obj;
            if (!(Math.abs(coefficient - object.coefficient) < 0.000000001))
                return false;
            return true;
        }

        @Override
        public String toString(){
            return getName() + ". Коэффициент рукоятки молотка: " + getCoefficient() + ".";
        }

        @Override
        public int hashCode(){
            final int prime = 17;
            int result = super.hashCode();
            result = result*prime + (int)coefficient*1000000;
            return result;
        }
    }

    public void changeHead(Material headMaterial){
        head = new Head(headMaterial);
        try {
            setPowerCoefficient(head.coefficient*0.8F + handle.coefficient*0.2F);
        }
        catch (IncorrectPowerCoefficientException e){
            System.out.println("Возникло исключение " + e);
        }
        System.out.println("Боёк молотка заменён. Новый коэффициент молотка: " + getPowerCoefficient() + ".");
    }

    public void changeHandle(Material handleMaterial){
        handle = new Handle(handleMaterial);
        try {
            setPowerCoefficient(head.coefficient*0.8F + handle.coefficient*0.2F);
        }
        catch (IncorrectPowerCoefficientException e){
            System.out.println("Возникло исключение " + e);
        }
        System.out.println("Рукоять молотка заменена. Новый коэффициент молотка: " + getPowerCoefficient() + ".");
    }

    public String getHeadMaterialName(){
        return head.getMaterial().fromMaterialToString();
    }

    public String getHandleMaterialName(){
        return handle.getMaterial().fromMaterialToString();
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj))
            return false;
        Hammer object = (Hammer) obj;
        if (!head.equals(object.head))
            return false;
        if (!handle.equals(object.handle))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return getName() + ". Коэффициент молотка: " + getPowerCoefficient() + ".";
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = super.hashCode();
        result = result*prime + ((head == null) ? 0 : head.hashCode());
        result = result*prime + ((handle == null) ? 0 : handle.hashCode());
        return result;
    }

}
