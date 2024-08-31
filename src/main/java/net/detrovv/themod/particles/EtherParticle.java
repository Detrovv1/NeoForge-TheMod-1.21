package net.detrovv.themod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import java.util.Random;

public class EtherParticle extends TextureSheetParticle
{
    private final SpriteSet spriteSet;
    private Direction direction;
    private TextureAtlasSprite sprite;
    private double startX;
    private double startY;
    private double startZ;
    private double xMargin;
    private double yMargin;
    private double zMargin;
    private final double speed = 0.05f;
    private final float marginReducingValue = 0.00175f;
    private final Double dispersion = 0.7d;

    public EtherParticle(ClientLevel level, double x, double y, double z, int directionCode, SpriteSet spriteSet)
    {
        super(level, x, y, z);

        startX = x;
        startY = y;
        startZ = z;
        this.spriteSet = spriteSet;
        this.lifetime = 105;
        this.alpha = 0.0f;
        this.gravity = 0;
        Random random = new Random();
        xMargin = random.nextDouble(dispersion) - dispersion / 2;
        yMargin = random.nextDouble(dispersion) - dispersion / 2;
        zMargin = random.nextDouble(dispersion) - dispersion / 2;

        updateDirectionData(directionCode);
        updateStartPosition();

        this.x = startX + xMargin;
        this.y = startY + yMargin;
        this.z = startZ + zMargin;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.setSpriteFromAge(spriteSet);
    }

    private void updateDirectionData(int directionCode)
    {
        switch (directionCode)
        {
            case 2 -> {this.direction = Direction.SOUTH;}
            case 3 -> {this.direction = Direction.NORTH;}
            case 4 -> {this.direction = Direction.WEST;}
            case 5 -> {this.direction = Direction.EAST;}
            default -> {this.direction = Direction.UP;}
        }
    }

    private void updateStartPosition()
    {
        switch (direction)
        {
            case SOUTH ->
            {
                startX += 0.5f;
            }
            case NORTH ->
            {
                startX += 0.5f;
                startZ += 1f;
            }
            case EAST ->
            {
                startZ += 0.5f;
            }
            case WEST ->
            {
                startX += 1f;
                startZ += 0.5f;
            }
        }
    }

    @Override
    public void tick()
    {
        move();

        if (this.age == 5)
        {
            this.alpha = 0.75f;
        }

        if (this.age++ >= this.lifetime)
        {
            this.remove();
        }

        this.setSpriteFromAge(spriteSet);
    }

    private void move()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        moveInDirection(this.direction);
    }

    private void moveInDirection(Direction direction)
    {
        switch (direction)
        {
            case SOUTH ->
            {
                this.z += speed;
                this.y = startY + movingFunction(this.z, startZ) + yMargin;
                this.x = startX + xMargin;
            }
            case NORTH ->
            {
                this.z -= speed;
                this.y = startY + movingFunction(this.z, startZ) + yMargin;
                this.x = startX + xMargin;
            }
            case EAST ->
            {
                this.x += speed;
                this.y = startY + movingFunction(this.x, startX) + yMargin;
                this.z = startZ + zMargin;
            }
            case WEST ->
            {
                this.x -= speed;
                this.y = startY + movingFunction(this.x, startX) + yMargin;
                this.z = startZ + zMargin;
            }
        }
        reduceMargine();
    }

    private double movingFunction(double currentGlobalPos, double startGlobalPos)
    {
        double localPos = Math.abs(currentGlobalPos - startGlobalPos);
        return 1 - 0.7442235*localPos + 0.3292045*localPos*localPos - 0.08674242*localPos*localPos*localPos + 0.01045455*localPos*localPos*localPos*localPos;
    }

    private void reduceMargine()
    {
        if (xMargin > 0) {xMargin -= marginReducingValue;}
        else {xMargin += marginReducingValue;}
        if (yMargin > 0) {yMargin -= marginReducingValue;}
        else {yMargin += marginReducingValue;}
        if (zMargin > 0) {zMargin -= marginReducingValue;}
        else {zMargin += marginReducingValue;}
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
