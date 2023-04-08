package com.seablu.seaofshulkers.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.ShulkerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


@Environment(EnvType.CLIENT)
public class CemDefaultShulkerModel extends ShulkerEntityModel<ShulkerEntity> implements CemModel{
    private static final Map<String, String> partNames = new HashMap<>();
    private final        CemModelRegistry    registry;

    static{
        partNames.put("headwear", "hat");
    }

    public CemDefaultShulkerModel(CemModelRegistry registry){
        this(registry, null);
    }

    public CemDefaultShulkerModel(CemModelRegistry registry, @Nullable Float inflate){
        super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
                .setInflate(inflate)
                .create()));
        this.registry = registry;
    }

    @Override
    public void setAngles(ShulkerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha){
        this.getParts().forEach((part) -> {
            part.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
    }
}