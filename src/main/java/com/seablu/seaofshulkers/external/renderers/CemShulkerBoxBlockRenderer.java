package com.seablu.seaofshulkers.external.renderers;

import com.seablu.seaofshulkers.SeaOfShulkers;
import com.seablu.seaofshulkers.external.models.CemShulkerModel;
import com.seablu.seaofshulkers.ShulkerBlockEntityAddons;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ShulkerBoxBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.joml.Quaternionf;

import java.util.*;


@Environment(EnvType.CLIENT)
public class CemShulkerBoxBlockRenderer extends ShulkerBoxBlockEntityRenderer implements CemRenderer{
	private static final Map<String, String> partNames  = new HashMap<>();
	private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();
	protected ShulkerEntityModel<?> customModel;
	protected CemDefaultShulkerBoxBlockRenderer defaultModelRenderer;
	protected CemModelRegistry registry;

	static{
		/*partNames.put("slate", "flag");
		partNames.put("stand", "pole"); //jojo reference?
		partNames.put("top", "bar");*/
	}
	
	public CemShulkerBoxBlockRenderer(BlockEntityRendererFactory.Context context){
		super(context);
		registry = CemRegistryManager.getRegistry(this.getType());
		this.defaultModelRenderer = new CemDefaultShulkerBoxBlockRenderer(context);
		try{
			this.customModel = new CemShulkerModel(registry);
		} catch(Exception e){
			modelError(e);
		}
	}
	
	private BlockEntityType<? extends BlockEntity> getType(){
		return BlockEntityType.SHULKER_BOX;
	}
	
	@Override
	public String getId(){
		return getType().toString();
	}

	@Override
	public void render(ShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		((ShulkerBlockEntityAddons)shulkerBoxBlockEntity).updateShulkerType();
		int shulkerType = ((ShulkerBlockEntityAddons)shulkerBoxBlockEntity).getShulkerType();
		if (shulkerType != -1) {
			Direction direction = Direction.UP;
			if (shulkerBoxBlockEntity.hasWorld()) {
				BlockState blockState = shulkerBoxBlockEntity.getWorld().getBlockState(shulkerBoxBlockEntity.getPos());
				if (blockState.getBlock() instanceof ShulkerBoxBlock) {
					direction = (Direction) blockState.get(ShulkerBoxBlock.FACING);
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

			if (shulkerBoxBlockEntity.hasWorld()) {
				// Lid Item
				ItemStack stack = shulkerBoxBlockEntity.getStack(0);
				Direction itemDir = Direction.NORTH;
				Vec3d translate = new Vec3d(0,0.5,0);
				float scale = 0.4f;
				if (stack.getItem() instanceof BlockItem){
					itemDir = Direction.UP;
					scale = 0.5f;
					translate = new Vec3d(0,0.5 + 0.07,0);
				}
				Quaternionf itemFacing = itemDir.getRotationQuaternion();

				DisplayItem(matrixStack, vertexConsumerProvider, direction, shulkerBoxBlockEntity, f, stack,
					true, itemFacing, translate, scale);

				List<Integer> interiorItems = Arrays.asList(2, 5, 10, 15, 20, 25);

				Random random = new Random(shulkerBoxBlockEntity.getPos().getX());
				// interior Item
				for(Integer pos : interiorItems) {
					stack = shulkerBoxBlockEntity.getStack(pos);
					itemFacing = new Quaternionf();
					itemFacing = itemFacing.rotateXYZ(random.nextFloat() * 360f, random.nextFloat() * 360f, random.nextFloat() * 360f);
					translate = new Vec3d(-0.2f + random.nextFloat() * 0.4f, -0.3f + random.nextFloat() * 0.3f, -0.2f + random.nextFloat() * 0.4f);
					scale = 0.4f;
					if (stack.getItem() instanceof BlockItem) {
						itemFacing = itemFacing.rotateXYZ(random.nextFloat() * 360f, random.nextFloat() * 360f, random.nextFloat() * 360f);
						scale = 0.5f;
						//translate = new Vec3d(0, 0.07, 0);
					}

					DisplayItem(matrixStack, vertexConsumerProvider, direction, shulkerBoxBlockEntity, f, stack,
						false, itemFacing, translate, scale);
				}
			}
		}
		else{
			defaultModelRenderer.render(shulkerBoxBlockEntity, f, matrixStack,vertexConsumerProvider, i, j);
		}
	}

	public VertexConsumer getTexture(ShulkerBoxBlockEntity shulkerBoxBlockEntity, VertexConsumerProvider vertexConsumerProvider){
		int shulkerType = ((ShulkerBlockEntityAddons)shulkerBoxBlockEntity).getShulkerType();
		VertexConsumer vertexConsumer;
		Identifier identifier = SeaOfShulkers.ALT_SHULKER_BOX_TEXTURES.get(shulkerType).getKey();
		vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(identifier));
		return vertexConsumer;
	}

	public void DisplayItem(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Direction direction, BlockEntity shulkerBoxBlockEntity, float f, ItemStack stack, boolean moveWithLid, Quaternionf itemFacing, Vec3d translate, float scale){
		matrixStack.push();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.multiply(direction.getRotationQuaternion());
		matrixStack.translate(0D, 0D, 0D);
		matrixStack.translate(translate.x,translate.y,translate.z);
		if (moveWithLid) {
			matrixStack.translate(0D, ((ShulkerBoxBlockEntity) shulkerBoxBlockEntity).getAnimationProgress(f) * 0.5F, 0D);
			Quaternionf itemQuat = new Quaternionf();
			matrixStack.multiply(itemQuat.rotateXYZ(0, -270 * ((ShulkerBoxBlockEntity) shulkerBoxBlockEntity).getAnimationProgress(f) * 0.017453292F, 0));
		}
		matrixStack.multiply(itemFacing);
		matrixStack.scale(scale,scale,scale);

		Vec3i shift = direction.getVector();
		int lightAbove = WorldRenderer.getLightmapCoordinates(shulkerBoxBlockEntity.getWorld(), shulkerBoxBlockEntity.getPos().add(shift));
		//System.out.println("Shulker stack: "+stack);
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, lightAbove, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, shulkerBoxBlockEntity.getWorld(), 0);
		matrixStack.pop();
	}
}