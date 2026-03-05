package amorphia.alloygery.compat.recipeViewers;

import amorphia.alloygery.machines.MachinesModule;
import amorphia.alloygery.machines.heatExchanger.HeatExchangerBlock;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class RecipeViewerCommonBlockStateRenderers
{
	public static void drawBlock(GuiGraphics draw, int x, int y, BlockState blockState)
	{
		Minecraft mc = Minecraft.getInstance();
		BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
		MultiBufferSource.BufferSource buffer = draw.bufferSource();
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		Lighting.setupFor3DItems();
		final int light = 15 << 20 | 15 << 4;

		PoseStack poseStack = draw.pose();
		poseStack.pushPose();

		poseStack.translate(x, y, 200);
		poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
		poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));
		poseStack.mulPoseMatrix(new Matrix4f().scaling(1, -1, 1));

		poseStack.scale(20, 20, 20);
		blockRenderer.renderSingleBlock(blockState, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);

		draw.flush();

		poseStack.popPose();
	}

	public static void drawHeatExchangerWithBlock(GuiGraphics draw, int x, int y, BlockState blockState)
	{
		Minecraft mc = Minecraft.getInstance();
		BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
		MultiBufferSource buffer = draw.bufferSource();
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		Lighting.setupFor3DItems();

		final int light = 15 << 20 | 15 << 4;

		PoseStack poseStack = draw.pose();
		poseStack.pushPose();

		poseStack.translate(x, y, 200);
		poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
		poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));
		poseStack.mulPoseMatrix(new Matrix4f().scaling(1, -1, 1));

		poseStack.pushPose();
		poseStack.scale(20, 20, 20);
		blockRenderer.renderSingleBlock(MachinesModule.HEAT_EXCHANGER.defaultBlockState().setValue(HeatExchangerBlock.FACING, Direction.SOUTH).setValue(HeatExchangerBlock.LIT, true), poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();

		poseStack.pushPose();
		poseStack.scale(20, 20, 20);
		poseStack.translate(0, 1, 0);
		blockRenderer.renderSingleBlock(blockState, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();

		draw.flush();

		poseStack.popPose();
	}
}
