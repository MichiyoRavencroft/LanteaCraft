package pcl.lc.base.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import pcl.lc.base.network.IStreamPackable;
import pcl.lc.util.WorldLocation;

public abstract class ModPacket {

	// Registers
	protected static final Class<?>[] classReferences = { int.class, Integer.class, // integers
			boolean.class, Boolean.class, // booleans
			double.class, Double.class, // doubles
			float.class, Float.class, // floats
			char.class, Character.class, // chars (int)
			String.class, // string
			ArrayList.class, HashMap.class // generic containers
	};

	protected static ArrayList<IStreamPackable<?>> packableHelpers = new ArrayList<IStreamPackable<?>>();

	/**
	 * Registers a network packing agent with the registry
	 * 
	 * @param agent
	 *            The agent to register
	 * @return The ID of the agent
	 */
	public static int registerPackable(IStreamPackable<?> agent) {
		synchronized (packableHelpers) {
			packableHelpers.add(agent);
			int id = packableHelpers.indexOf(agent);
			return id;
		}
	}

	/**
	 * Finds a packer by a given ID
	 * 
	 * @param idx
	 *            The ID of the packer
	 * @return The packer object
	 */
	protected static IStreamPackable<?> findPacker(int idx) {
		synchronized (packableHelpers) {
			for (IStreamPackable<?> packer : packableHelpers)
				if (packer.getTypeOf() == idx)
					return packer;
		}
		return null;
	}

	/**
	 * Finds a packer by a given class
	 * 
	 * @param clazz
	 *            The class the packer supports
	 * @return The packer object
	 */
	protected static IStreamPackable<?> findPacker(Class<?> clazz) {
		synchronized (packableHelpers) {
			for (IStreamPackable<?> packer : packableHelpers)
				if (packer.getClassOf().equals(clazz))
					return packer;
		}
		return null;
	}

	/**
	 * Gets the ID of a generic Java type
	 * 
	 * @param clazz
	 *            The class of the generic
	 * @return The ID of the generic type
	 */
	protected static int getGenericID(Class<?> clazz) {
		for (int i = 0; i < classReferences.length; i++)
			if (classReferences[i].equals(clazz))
				return i;
		return -1;
	}

	/**
	 * Gets the class of a generic Java type
	 * 
	 * @param id
	 *            The ID of the generic type
	 * @return The class of the generic
	 */
	protected static Class<?> getGeneric(int id) {
		if (id >= 0 && id < classReferences.length)
			return classReferences[id];
		return null;
	}

	public abstract boolean getPacketIsForServer();

	public abstract String getType();

	public abstract WorldLocation getOriginLocation();

	public abstract void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException;

	public abstract void decodeFrom(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException;

}
