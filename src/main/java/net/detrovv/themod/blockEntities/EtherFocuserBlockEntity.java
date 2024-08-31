package net.detrovv.themod.blockEntities;

import net.detrovv.themod.blocks.ModBlocks;
import net.detrovv.themod.blocks.custom.EtherReciever;
import net.detrovv.themod.blocks.custom.SoulTube;
import net.detrovv.themod.particles.ModParticles;
import net.detrovv.themod.souls.Soul;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class EtherFocuserBlockEntity extends BlockEntity
{
    public float power = 0;
    public boolean isWorking = false;
    private int counterForRestoringPower = 0;
    private final float maxPower = 1000;
    private final float powerReducePerTick = 0.83f;
    private Direction workingDirection = null;

    public EtherFocuserBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.ETHER_FOCUSER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick()
    {
        Level level = getLevel();
        if (level.isClientSide())
        {
            clientTick();
        }
        if (!level.isClientSide())
        {
            serverTick();
        }
    }

    private void clientTick()
    {
        Random random = new Random();
        if (random.nextBoolean())
        {
            spawnParticles();
        }
    }

    private void spawnParticles()
    {
        Level level = getLevel();
        BlockPos position = getBlockPos();
        if (isWorking)
        {
            int directionCode = SoulTube.getDirectionCode(workingDirection);
            level.addParticle(ModParticles.ETHER.get(), position.getX(), position.getY(), position.getZ(), directionCode,0,0);
        }
    }

    private void serverTick()
    {
        counterForRestoringPower++;
        if (counterForRestoringPower == 20)
        {
            restorePower();
            counterForRestoringPower = 0;
        }
        if (!isWorking)
        {
            tryToStartWork();
        }
        if (isWorking)
        {
            if (isThereReciever())
            {
                reducePower();
            }
            else
            {
                isWorking = false;
                workingDirection = null;
            }
        }
        setChangedAndSendUpdate();
    }

    private void restorePower()
    {
        List<AbstractSoulStorageBlockEntity> storages = getSoulStorages();
        float freeSpace = maxPower - power;

        if (freeSpace == 0)
        {
            return;
        }

        for (AbstractSoulStorageBlockEntity storage : storages)
        {
            List<Soul> souls = storage.getSouls();
            for (Soul soul : souls)
            {
                int soulPower = soul.getPower();
                if (!isWorking || soulPower < freeSpace)
                {
                    power += soulPower;
                    if (power > maxPower)
                    {
                        power = maxPower;
                    }
                    storage.removeSoul(soul);
                    return;
                }
            }
        }
    }

    private List<AbstractSoulStorageBlockEntity> getSoulStorages()
    {
        BlockPos thisPosition = getBlockPos();
        Level level = getLevel();
        List<BlockPos> neighborPositions = SoulTube.getNeighborPositions(thisPosition);
        List<AbstractSoulStorageBlockEntity> soulStorages = new ArrayList<>();

        for (BlockPos neighborPosition : neighborPositions)
        {
            BlockEntity blockEntity = level.getBlockEntity(neighborPosition);
            if (blockEntity instanceof AbstractSoulStorageBlockEntity soulStorage)
            {
                soulStorages.add(soulStorage);
            }
            if (blockEntity instanceof SoulTubeBlockEntity soulTube)
            {
                if (SoulTube.isTubeFacingBlock(level.getBlockState(neighborPosition), neighborPosition, thisPosition))
                {
                    Set<BlockPos> checkedPositions = new HashSet<>();
                    soulStorages.addAll(soulTube.getSoulSources(checkedPositions));
                }
            }
        }

        return soulStorages;
    }

    private void tryToStartWork()
    {
        if (power == maxPower && isThereReciever())
        {
            isWorking = true;
        }
    }

    private boolean isThereReciever()
    {
        Level level = getLevel();
        BlockPos thisPosition = getBlockPos();
        List<Direction> directions = SoulTube.DIRECTIONS;
        List<Direction> suitableDirections = new ArrayList<>();
        for (int i = 2; i < 6; i++)
        {
            BlockPos checkingPosition = SoulTube.getNeighborPositionInDirection(thisPosition, directions.get(i), 4);
            BlockState checkingState = level.getBlockState(checkingPosition);
            if (checkingState.getBlock() == ModBlocks.ETHER_RECIEVER.get())
            {
                Direction currentDirection = directions.get(i);
                Direction checkingRecieverDirection = checkingState.getValue(EtherReciever.FACING);

                if (checkingRecieverDirection == currentDirection ||
                    checkingRecieverDirection == currentDirection.getOpposite())
                {
                    suitableDirections.add(currentDirection);
                }
            }
        }

        if (suitableDirections.size() > 0)
        {
            if (suitableDirections.contains(workingDirection))
            {
                return true;
            }
            else
            {
                this.workingDirection = suitableDirections.get(0);
                return true;
            }
        }
        this.workingDirection = null;
        return false;
    }

    private void reducePower()
    {
        power -= powerReducePerTick;
        if (power <= 0)
        {
            power = 0;
        }
    }

    private void setChangedAndSendUpdate()
    {
        setChanged();
        getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider registries)
    {
        super.loadAdditional(compoundTag, registries);

        power = compoundTag.getFloat("power");
        isWorking = compoundTag.getBoolean("is_working");
        if (compoundTag.contains("directionCode"))
        {
            int directionCode = compoundTag.getInt("directionCode");
            workingDirection = SoulTube.DIRECTIONS.get(directionCode);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider registries)
    {
        super.saveAdditional(compoundTag, registries);

        if (workingDirection != null)
        {
            int directionCode = SoulTube.getDirectionCode(workingDirection);
            compoundTag.putInt("directionCode", directionCode);
        }
        compoundTag.putFloat("power", power);
        compoundTag.putBoolean("is_working", isWorking);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        CompoundTag compoundTag = new CompoundTag();

        if (workingDirection != null)
        {
            int directionCode = SoulTube.getDirectionCode(workingDirection);
            compoundTag.putInt("directionCode", directionCode);
        }
        compoundTag.putFloat("power", power);
        compoundTag.putBoolean("is_working", isWorking);

        return compoundTag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider)
    {
        super.onDataPacket(net, pkt, lookupProvider);
        this.loadAdditional(pkt.getTag(), lookupProvider);
    }
}
