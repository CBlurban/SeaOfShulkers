package com.seablu.seaofshulkers.external;

import com.google.common.collect.ImmutableList;
import com.seablu.seaofshulkers.SeaOfShulkers;
import com.seablu.seaofshulkers.external.renderers.CemShulkerBoxBlockRenderer;
import net.dorianpb.cem.external.CemEntitiesInit;
import net.dorianpb.cem.internal.api.CemEntityInitializer;
import net.dorianpb.cem.internal.util.CemFairy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.ShulkerBoxBlockEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class ShulkerEntitiesInit extends CemEntityInitializer {

	@Override
	public void onInit(){
		register(BlockEntityType.SHULKER_BOX, CemShulkerBoxBlockRenderer::new);
		register("default_shulker_box");
	}
}