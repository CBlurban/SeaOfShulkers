package com.seablu.seaofshulkers;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		ALT_SHULKER_BOX_TEXTURES = Stream.of(
				new AbstractMap.SimpleEntry<>("end_case","(?=.*case)(?=.*ender).*"),
				new AbstractMap.SimpleEntry<>("display_case","(?=.*case)(?=.*display).*"),
				new AbstractMap.SimpleEntry<>("diamond_case","(?=.*case)(?=.*diamond).*"),
				new AbstractMap.SimpleEntry<>("emerald_case","(?=.*case)(?=.*emerald).*"),
				new AbstractMap.SimpleEntry<>("end_trunk","(?=.*trunk)(?=.*ender).*"),
				new AbstractMap.SimpleEntry<>("obsidian_trunk","(?=.*trunk)(?=.*obsidian).*"),
				new AbstractMap.SimpleEntry<>("red_trunk","(?=.*trunk)(?=.*red).*"),
				new AbstractMap.SimpleEntry<>("snow_trunk","(?=.*trunk)(?=.*snow).*"),
				new AbstractMap.SimpleEntry<>("cyan_trunk","(?=.*trunk)(?=.*cyan).*"),
				new AbstractMap.SimpleEntry<>("black_trunk","(?=.*trunk)(?=.*black).*"),
				new AbstractMap.SimpleEntry<>("brown_trunk",".*(trunk).*"),
				new AbstractMap.SimpleEntry<>("jungle_log","(?=.*(log|stump))(?=.*jungle).*"),
				new AbstractMap.SimpleEntry<>("doak_log","(?=.*(log|stump))(?=.*dark oak).*"),
				new AbstractMap.SimpleEntry<>("birch_log","(?=.*(log|stump))(?=.*birch).*"),
				new AbstractMap.SimpleEntry<>("acacia_log","(?=.*(log|stump))(?=.*acacia).*"),
				new AbstractMap.SimpleEntry<>("spruce_log","(?=.*(log|stump))(?=.*spruce).*"),
				new AbstractMap.SimpleEntry<>("oak_log",".*(log|stump).*"),
				new AbstractMap.SimpleEntry<>("slab_case","(?=.*case)(?=.*slab).*"),
				new AbstractMap.SimpleEntry<>("wood_case",".*(case).*")
		).map(
				(entry) -> new AbstractMap.SimpleEntry<>(new Identifier("dorianpb:cem/shulker/"+"shulker_" + entry.getKey() +".png"), entry.getValue())
		).collect(ImmutableList.toImmutableList());
	}
}
