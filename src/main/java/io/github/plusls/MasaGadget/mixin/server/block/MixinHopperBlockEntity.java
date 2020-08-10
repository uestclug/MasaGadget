package io.github.plusls.MasaGadget.mixin.server.block;

import io.github.plusls.MasaGadget.MasaGadgetMod;
import io.github.plusls.MasaGadget.network.ServerNetworkHandler;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HopperBlockEntity.class)
public abstract class MixinHopperBlockEntity extends LootableContainerBlockEntity implements Hopper, Tickable {

    @Shadow
    public abstract CompoundTag toTag(CompoundTag tag);

    public MixinHopperBlockEntity() {
        super(null);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (ServerNetworkHandler.lastBlockPosMap.containsValue(this.pos)) {
            ((ServerWorld) this.getWorld()).getChunkManager().markForUpdate(this.getPos());
            MasaGadgetMod.LOGGER.debug("update HopperBlockEntity: {}", this.pos);
        }
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 0, this.toTag(new CompoundTag()));
    }
}