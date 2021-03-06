package pcl.lc.module.stargate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import pcl.lc.base.multiblock.GenericMultiblock;
import pcl.lc.base.multiblock.MultiblockPart;
import pcl.lc.base.network.packet.ModPacket;
import pcl.lc.base.network.packet.StandardModPacket;
import pcl.lc.module.stargate.tile.TileTransporterRing;
import pcl.lc.util.ScanningHelper;
import pcl.lc.util.Vector3;
import pcl.lc.util.WorldLocation;

public class TransporterRingPart extends MultiblockPart {

	private String typeof;
	private boolean modified = false;

	private WeakReference<GenericMultiblock> currentHost;

	public TransporterRingPart(TileEntity host) {
		super(host);
	}

	@Override
	public void tick() {
		if (!host.getWorldObj().isRemote && modified) {
			modified = !modified;
			host.getDescriptionPacket();
		}
	}

	public void setType(String typeof) {
		this.typeof = typeof;
		modified = true;
	}

	@Override
	public GenericMultiblock findHostMultiblock(boolean allowScanning) {
		if (currentHost != null && currentHost.get() != null)
			return currentHost.get();

		if (!allowScanning)
			return null;
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(-5, 0, -5, 5, 1, 5);
		ArrayList<Vector3> entities = ScanningHelper.findAllTileEntitesOf(host.getWorldObj(),
				TileTransporterRing.class, host.xCoord, host.yCoord, host.zCoord, bounds);
		if (entities == null || entities.size() == 0)
			return null;
		Vector3 origin = new Vector3(host);
		for (int i = 0; i < entities.size(); i++) {
			Vector3 apath = entities.get(i).add(origin);
			TileEntity tile = host.getWorldObj().getTileEntity(apath.floorX(), apath.floorY(), apath.floorZ());
			if (tile instanceof TileTransporterRing) {
				TileTransporterRing ring = (TileTransporterRing) tile;
				if (ring.isHost())
					return ring.getAsStructure();
			}
		}
		return null;
	}

	@Override
	public boolean canMergeWith(GenericMultiblock structure) {
		if (isClient)
			return true;
		if (currentHost == null)
			return true;
		if (currentHost.get() == null)
			return true;
		if (currentHost.get().equals(structure))
			return true;
		return false;
	}

	@Override
	public boolean mergeWith(GenericMultiblock structure) {
		currentHost = new WeakReference<GenericMultiblock>(structure);
		modified = true;
		return true;
	}

	@Override
	public boolean isMerged() {
		return (currentHost != null && currentHost.get() != null);
	}

	@Override
	public void release() {
		currentHost = null;
		modified = true;
	}

	@Override
	public String getType() {
		return typeof;
	}

	@Override
	public Vector3 getVectorLoc() {
		return new Vector3(host);
	}

	public ModPacket pack() {
		StandardModPacket packet = new StandardModPacket(new WorldLocation(host));
		packet.setIsForServer(false);
		packet.setType("LanteaPacket.MultiblockUpdate");
		if (currentHost != null && currentHost.get() != null)
			packet.setValue("currentHost", currentHost.get().getLocation());
		return packet;
	}

	public void unpack(ModPacket packetOf) {
		StandardModPacket packet = (StandardModPacket) packetOf;
		if (packet.hasFieldWithValue("currentHost")) {
			Vector3 location = (Vector3) packet.getValue("currentHost");
			TileEntity target = host.getWorldObj().getTileEntity(location.floorX(), location.floorY(),
					location.floorZ());
			if (target != null && (target instanceof TileTransporterRing)) {
				TileTransporterRing stargateBase = (TileTransporterRing) target;
				currentHost = new WeakReference<GenericMultiblock>(stargateBase.getAsStructure());
			}
		} else
			currentHost = null;
		host.getWorldObj().markBlockForUpdate(host.xCoord, host.yCoord, host.zCoord);
	}

}
