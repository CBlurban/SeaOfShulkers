package com.seablu.seaofshulkers.mixin;

import com.seablu.seaofshulkers.SeaOfShulkers;
import com.seablu.seaofshulkers.ShulkerBlockEntityAddons;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.AbstractMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends LootableContainerBlockEntity implements ShulkerBlockEntityAddons {
    @Shadow @Final public static String ITEMS_KEY;
    String nameString = "";
    int shulkerType = -1;
    int decorItemType = -1;

    public ShulkerBoxBlockEntityMixin(){
        super(null, null, null);
    }

    public String getNameString(){
        return nameString;
    }
    public int getShulkerType(){
        return shulkerType;
    }

	@Override
	public void setCustomName(Text customName) {
        super.setCustomName(customName);
        updateShulkerType();
	}

    @Inject(method = "readNbt", at = @At("TAIL"))
    public void readNbt(NbtCompound nbt, CallbackInfo info) {
        updateShulkerType();
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    public void onClose(PlayerEntity player, CallbackInfo info){
        if (!player.isSpectator()){
            BlockEntityUpdateS2CPacket updatePacket = toUpdatePacket();

            if (updatePacket != null) {
                ((ServerPlayerEntity)player).networkHandler.sendPacket(updatePacket);
            }
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound newNBT = new NbtCompound();
        this.writeNbt(newNBT);
        return newNBT;
    }

    /*@Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this.pos, (BlockEntityType.SHULKER_BOX, this.toInitialChunkDataNbt()));
    }*/

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        this.writeNbt(new NbtCompound());
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void updateShulkerType(){
        nameString = this.getName().getString();
        //System.out.println("Shulker item name: "+nameString);
        if (this.hasCustomName()) {
            // Get shulker type based on the name
            int typeId = 0;
            for (AbstractMap.Entry<Identifier,String> entry: SeaOfShulkers.ALT_SHULKER_BOX_TEXTURES) {
                Pattern pattern = Pattern.compile(entry.getValue());
                Matcher matcher = pattern.matcher(nameString.toLowerCase(Locale.ROOT));
                if (matcher.find()) {
                    //System.out.println("Pattern matched: "+entry.getValue() + " Texture identifier: "+entry.getKey().toString());
                    shulkerType = typeId;
                    return;
                }
                typeId ++;
            }
        }
        shulkerType = -1;
    }

    public void fromClientTag(NbtCompound tag){
        readNbt(tag);
    }

    public NbtCompound toClientTag(NbtCompound tag){
        return toInitialChunkDataNbt();
    }
}
