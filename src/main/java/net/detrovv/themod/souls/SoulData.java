package net.detrovv.themod.souls;

public class SoulData
{
    public SoulOrigins origin;
    public int minPower;
    public int maxPower;

    public SoulData(SoulOrigins origin, int minPower, int maxPower)
    {
        this.origin = origin;
        this.minPower = minPower;
        this.maxPower = maxPower;
    }
}
