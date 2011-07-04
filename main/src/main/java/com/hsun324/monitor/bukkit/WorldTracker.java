package com.hsun324.monitor.bukkit;

import org.bukkit.World;

import com.hsun324.monitor.config.BooleanOption;
import com.hsun324.simplebukkit.util.BukkitUtils;

public class WorldTracker
{
	private World world;
	public WorldTracker(World world)
	{
		this.world = world;
	}
	public World getWorld()
	{
		return world;
	}
	@Override
	public String toString()
	{
		String res = world.getName();
		if(BooleanOption.getOption("world.time").isSet())
			res += " (" + BukkitUtils.getWorldTimeString(world) + ")";
		if(BooleanOption.getOption("world.player").isSet() || BooleanOption.getOption("world.entity").isSet())
			res += " - ";
		if(BooleanOption.getOption("world.player").isSet())
			res += world.getPlayers().size();
		if(BooleanOption.getOption("world.player").isSet() && BooleanOption.getOption("world.entity").isSet())
			res += ", ";
		if(BooleanOption.getOption("world.entity").isSet())
			res += world.getEntities().size();
		return res;
	}
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof WorldTracker)
		{
			return ((WorldTracker)o).world.equals(world);
		}
		return o == this;
	}
	@Override
	public int hashCode()
	{
		return world.hashCode();
	}
}
