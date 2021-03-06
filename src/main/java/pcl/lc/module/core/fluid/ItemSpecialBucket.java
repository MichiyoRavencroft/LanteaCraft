package pcl.lc.module.core.fluid;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import pcl.lc.LanteaCraft;
import pcl.lc.core.ResourceAccess;
import pcl.lc.util.CreativeTabHelper;
import pcl.lc.util.SpecialBucketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * ItemSpecialBucket is a special bucket implementation which allows Fluids to
 * create custom bucket types generically.
 * 
 * @author AfterLifeLochie
 */
public class ItemSpecialBucket extends ItemBucket {

	/**
	 * Icon name associated with the unlocalized name of this bucket instance.
	 */
	private String iconName;

	/**
	 * Creates an instance of an ItemSpecialBucket and initializes it with
	 * default properties and with the {@link SpecialBucketHandler} registry.
	 * 
	 * @param i
	 *            The item ID to use for this bucket.
	 * @param hostBlock
	 *            The fluid block this Bucket is hosting.
	 */
	public ItemSpecialBucket(Block hostBlock) {
		super(hostBlock);
		LanteaCraft.getSpecialBucketHandler().registerBucketMapping(hostBlock, this);
		setCreativeTab(CreativeTabHelper.getTab("LanteaCraft"));
		setContainerItem(Items.bucket);
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		iconName = par1Str;
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(ResourceAccess.formatResourceName(
				"${ASSET_KEY}:bucket_%s_${TEX_QUALITY}", iconName));
	}

	public void setTargetTexture(String bucketTextureName) {
		iconName = bucketTextureName;
	}
}
