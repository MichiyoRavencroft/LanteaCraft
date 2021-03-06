package pcl.lc.module.stargate.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import pcl.lc.base.GenericBlockRenderer;
import pcl.lc.module.stargate.StargateMultiblock;
import pcl.lc.module.stargate.StargatePart;
import pcl.lc.module.stargate.block.BlockStargateRing;
import pcl.lc.module.stargate.tile.TileStargateRing;

public class BlockStargateRingRenderer extends GenericBlockRenderer {

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rb) {
		BlockStargateRing ringBlock = (BlockStargateRing) block;
		TileStargateRing ringTE = (TileStargateRing) world.getTileEntity(x, y, z);

		StargatePart partOf = ringTE.getAsPart();
		if (partOf == null)
			return super.renderWorldBlock(world, x, y, z, block, modelId, rb);

		StargateMultiblock structureOf = (StargateMultiblock) partOf.findHostMultiblock(false);
		if (structureOf == null)
			return super.renderWorldBlock(world, x, y, z, block, modelId, rb);

		if (!structureOf.isValid())
			return super.renderWorldBlock(world, x, y, z, block, modelId, rb);

		return false;

	}

}
