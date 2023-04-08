package com.seablu.seaofshulkers.external.renderers;

import com.seablu.seaofshulkers.ShulkerBlockEntityAddons;
import com.seablu.seaofshulkers.external.ShulkerEntitiesInit;
import com.seablu.seaofshulkers.external.models.CemDefaultShulkerModel;
import com.seablu.seaofshulkers.external.models.CemShulkerModel;
import net.dorianpb.cem.external.renderers.CemSheepRenderer;
import net.dorianpb.cem.internal.api.CemRenderer;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.util.CemRegistryManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ShulkerBoxBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Environment(EnvType.CLIENT)
public class CemDefaultShulkerBoxBlockRenderer extends ShulkerBoxBlockEntityRenderer implements CemRenderer{
    private static final Map<String, String> partNames  = new HashMap<>();
    private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();
    protected ShulkerEntityModel<?> customModel;
    protected CemModelRegistry registry;

    public CemDefaultShulkerBoxBlockRenderer(BlockEntityRendererFactory.Context context){
        super(context);
        registry = CemRegistryManager.getRegistry(this.getType());
        try{
            this.customModel = new CemDefaultShulkerModel(registry);
        } catch(Exception e){
            modelError(e);
        }
    }

    private String getType(){
        return "default_shulker_box";
    }

    @Override
    public String getId(){
        return getType().toString();
    }


    @Override
    public void render(ShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Direction direction = Direction.UP;
        if (shulkerBoxBlockEntity.hasWorld()) {
            BlockState blockState = shulkerBoxBlockEntity.getWorld().getBlockState(shulkerBoxBlockEntity.getPos());
            if (blockState.getBlock() instanceof ShulkerBoxBlock) {
                direction = (Direction)blockState.get(ShulkerBoxBlock.FACING);
            }
        }

        matrixStack.push();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        float g = 0.9995F;
        matrixStack.scale(0.9995F, 0.9995F, 0.9995F);
        matrixStack.multiply(direction.getRotationQuaternion());
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0.0D, -1.0D, 0.0D);
        ModelPart modelPart = this.customModel.getLid();
        modelPart.setPivot(0.0F, 24.0F - shulkerBoxBlockEntity.getAnimationProgress(f) * 0.5F * 16.0F, 0.0F);
        modelPart.yaw = 270.0F * shulkerBoxBlockEntity.getAnimationProgress(f) * 0.017453292F;

        VertexConsumer vertexConsumer = getTexture(shulkerBoxBlockEntity, vertexConsumerProvider);
        this.customModel.render(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

    public VertexConsumer getTexture(ShulkerBoxBlockEntity shulkerBoxBlockEntity, VertexConsumerProvider vertexConsumerProvider){
        ((ShulkerBlockEntityAddons)shulkerBoxBlockEntity).updateShulkerType();

        DyeColor dyeColor = shulkerBoxBlockEntity.getColor();
        VertexConsumer vertexConsumer;
        if (dyeColor == null) {
            SpriteIdentifier spriteIdentifier2 = TexturedRenderLayers.SHULKER_TEXTURE_ID;
            vertexConsumer = spriteIdentifier2.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutoutNoCull);
        } else {
            SpriteIdentifier spriteIdentifier2 = (SpriteIdentifier)TexturedRenderLayers.COLORED_SHULKER_BOXES_TEXTURES.get(dyeColor.getId());
            vertexConsumer = spriteIdentifier2.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutoutNoCull);
        }
        return vertexConsumer;
    }
}
