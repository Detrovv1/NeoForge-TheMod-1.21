package net.detrovv.themod.souls;

import java.util.Random;

public class Soul
{
    private SoulOrigins origin;
    private int soulPower;

    public Soul(SoulData data)
    {
        Random rand = new Random();
        soulPower = rand.nextInt(data.minPower, data.maxPower);
        origin = data.origin;
    }

    public Soul(SoulOrigins pOrigin, int pSoulPower)
    {
        origin = pOrigin;
        soulPower = pSoulPower;
    }

    public int GetPower()
    {
        return soulPower;
    }

    public SoulOrigins GetOrigin()
    {
        return origin;
    }
}
