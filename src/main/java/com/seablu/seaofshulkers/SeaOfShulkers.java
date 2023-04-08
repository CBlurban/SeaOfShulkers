package com.seablu.seaofshulkers;

import com.google.common.collect.ImmutableList;
import net.dorianpb.cem.internal.util.CemFairy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Stream;

import static net.minecraft.block.Blocks.SHULKER_BOX;


public class SeaOfShulkers implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("modid");
	public static BlockEntityType<ShulkerBoxBlockEntity> DEFAULT_SHULKER_BOX;
	public static final List<AbstractMap.Entry<Identifier,String>> ALT_SHULKER_BOX_TEXTURES;

	@Override
	public void onInitialize() {
		DEFAULT_SHULKER_BOX = Registry.register(Registries.BLOCK_ENTITY_TYPE, "tutorial:demo_block_entity", FabricBlockEntityTypeBuilder.create(ShulkerBoxBlockEntity::new, SHULKER_BOX).build(null));
	}

	static {
		ALT_SHULKER_BOX_TEXTURES = (List) Stream.of(
				new AbstractMap.SimpleEntry<String,String>("end_case","(?=.*case)(?=.*ender).*"),
				new AbstractMap.SimpleEntry<String,String>("display_case","(?=.*case)(?=.*display).*"),
				new AbstractMap.SimpleEntry<String,String>("diamond_case","(?=.*case)(?=.*diamond).*"),
				new AbstractMap.SimpleEntry<String,String>("emerald_case","(?=.*case)(?=.*emerald).*"),
				new AbstractMap.SimpleEntry<String,String>("end_trunk","(?=.*trunk)(?=.*ender).*"),
				new AbstractMap.SimpleEntry<String,String>("obsidian_trunk","(?=.*trunk)(?=.*obsidian).*"),
				new AbstractMap.SimpleEntry<String,String>("red_trunk","(?=.*trunk)(?=.*red).*"),
				new AbstractMap.SimpleEntry<String,String>("snow_trunk","(?=.*trunk)(?=.*snow).*"),
				new AbstractMap.SimpleEntry<String,String>("cyan_trunk","(?=.*trunk)(?=.*cyan).*"),
				new AbstractMap.SimpleEntry<String,String>("black_trunk","(?=.*trunk)(?=.*black).*"),
				new AbstractMap.SimpleEntry<String,String>("brown_trunk",".*(trunk).*"),
				new AbstractMap.SimpleEntry<String,String>("jungle_log","(?=.*(log|stump))(?=.*jungle).*"),
				new AbstractMap.SimpleEntry<String,String>("doak_log","(?=.*(log|stump))(?=.*dark oak).*"),
				new AbstractMap.SimpleEntry<String,String>("birch_log","(?=.*(log|stump))(?=.*birch).*"),
				new AbstractMap.SimpleEntry<String,String>("acacia_log","(?=.*(log|stump))(?=.*acacia).*"),
				new AbstractMap.SimpleEntry<String,String>("spruce_log","(?=.*(log|stump))(?=.*spruce).*"),
				new AbstractMap.SimpleEntry<String,String>("oak_log",".*(log|stump).*"),
				new AbstractMap.SimpleEntry<String,String>("slab_case","(?=.*case)(?=.*slab).*"),
				new AbstractMap.SimpleEntry<String,String>("wood_case",".*(case).*")
		).map((entry) -> {
			return new AbstractMap.SimpleEntry<Identifier,String>(new Identifier("dorianpb:cem/shulker/"+"shulker_" + entry.getKey() +".png"), entry.getValue());
		}).collect(ImmutableList.toImmutableList());
	}
}
