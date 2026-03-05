package amorphia.alloygery.compat.recipeViewers.emi;

import amorphia.alloygery.compat.recipeViewers.RecipeViewerCommonBlockStateRenderers;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.widget.DrawableWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.state.BlockState;

public record BlockRenderWidget(BlockState blockState) implements EmiRenderable, DrawableWidget.DrawableWidgetConsumer
{
	@Override
	public void render(GuiGraphics draw, int x, int y, float delta)
	{
		RecipeViewerCommonBlockStateRenderers.drawBlock(draw, 0, 0, blockState);

//		Minecraft mc = Minecraft.getInstance();
//		BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
//		MultiBufferSource.BufferSource buffer = draw.bufferSource();
//		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
//		Lighting.setupFor3DItems();
//		final int light = 15 << 20 | 15 << 4;
//
//		PoseStack poseStack = draw.pose();
//		poseStack.pushPose();
//
//		poseStack.translate(0, 0, 200);
//		poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
//		poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));
//		poseStack.mulPoseMatrix(new Matrix4f().scaling(1, -1, 1));
//
//		poseStack.scale(20, 20, 20);
//		blockRenderer.renderSingleBlock(blockState, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
//
//		poseStack.popPose();
	}
}
