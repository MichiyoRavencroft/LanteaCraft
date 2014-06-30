package pcl.lc.render.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import pcl.common.render.GenericBlockRenderer;
import pcl.lc.LanteaCraft;
import pcl.lc.module.ModulePower;
import pcl.lc.render.models.NaquadahGeneratorModel;
import cpw.mods.fml.client.FMLClientHandler;

public class BlockNaquadahGeneratorRenderer extends GenericBlockRenderer {

	private ResourceLocation texture;

	public BlockNaquadahGeneratorRenderer() {
		texture = LanteaCraft.getResource("textures/models/naquada_generator_off_"
				+ LanteaCraft.getProxy().getRenderMode() + ".png");
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int renderID, RenderBlocks rb) {
		if (!LanteaCraft.getProxy().isUsingModels())
			super.renderWorldBlock(world, x, y, z, block, renderID, rb);
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks rb) {
		if (!LanteaCraft.getProxy().isUsingModels())
			super.renderInventoryBlock(block, metadata, modelID, rb);
		else {
			NaquadahGeneratorModel model = ModulePower.Render.modelNaquadahGenerator;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glTranslatef(0.5f, 0.5f, 0.0f);
			GL11.glRotatef(45, 0, 1, 0);
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
			GL11.glTranslatef(-0.6f, 0.0f, 0.0f);
			GL11.glScalef(1.5f, 1.5f, 1.5f);
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
			model.renderAll();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}

	}
}
