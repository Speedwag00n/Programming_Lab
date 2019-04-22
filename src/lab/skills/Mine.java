package lab.skills;

import lab.objects.entities.Human.NegativePowerException;
import lab.objects.ContainsSkills;
import lab.objects.items.miningInstruments.MiningInstrument;
import lab.objects.items.rock.Minable;

public class Mine extends Skill {

    private Minable source;
    private float power = 10F;
    private MiningInstrument instrument;

    public Mine(String aName, Minable aSource, float aPower, MiningInstrument aInstrument){
        super(aName);
        setSource(aSource);
        try {
            setPower(aPower);
        }
        catch (NegativePowerException e){
            System.out.println("Возникло ислкючение " + e);
        }
            setInstrument(aInstrument);
    }

    public Minable getSource(){
        if(source == null)
            System.out.println("Источник добычи не известен.");
        return source;
    }

    public void setSource(Minable aSource){
        if(aSource != null)
            source = aSource;
        else
            System.out.println("Некорректный источник добычи.");
    }

    public float getPower(){
        return power;
    }

    public void setPower(float aPower) throws NegativePowerException {
        if(aPower < 0)
            throw new NegativePowerException("Введите неотрицательное значение силы!", aPower);
        else
            power = aPower;
    }

    public MiningInstrument getInstrument(){
        if(instrument == null)
            System.out.println("Не указан инструмент, которым нужно добывать.");
        return instrument;
    }

    public void setInstrument(MiningInstrument aInstrument){
        if(aInstrument != null)
            instrument = aInstrument;
        else
            System.out.println("Некорректный инструмент.");
    }

    public void use(ContainsSkills executor){
        if(getSource() != null)
            if (getInstrument() != null){
                System.out.println("Начинаем добычу.");
                float finalPower = getPower()*instrument.getPowerCoefficient();
                float oreWeight = (float)Math.random()*finalPower;
                float stoneWeight = (float)Math.random()*finalPower;
                try {
                    source.reduceWeightOfRock(oreWeight, stoneWeight);
                }
                catch (MiningSourceException e){
                    System.out.println("Возникло исключение " + e);
                }
            }
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj))
            return false;
        Mine object = (Mine) obj;
        if (!source.equals(object.source))
            return false;
        if (!(Math.abs(power - object.power) < 0.000000001))
            return false;
        if (!instrument.equals(object.instrument))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "Способность: " + getName() + ". Сила добычи: " + getPower() + ". Коэффициент инструмента: " + instrument.getPowerCoefficient() + ".";
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = super.hashCode();
        result = result*prime + source.hashCode();
        result = result*prime + (int)power*1000000;
        result = result*prime + instrument.hashCode();
        return result;
    }

}
