package com.hsun324.monitor.bukkit;

import org.bukkit.entity.Player;

import com.hsun324.monitor.config.BooleanOption;
import com.hsun324.simplebukkit.permissions.PermissionsHandler;

public class PlayerTracker
{
	private Player player;
	public PlayerTracker(Player player)
	{
		this.player = player;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	@Override
	public String toString()
	{
		String lo = player.getLocation().getWorld().getName() + ":" + player.getLocation().getBlockX() + "," +
		            player.getLocation().getBlockY() + "," + player.getLocation().getBlockZ();
		String res = "";
		if(BooleanOption.getOption("player.group").isSet())
			res += "[" + PermissionsHandler.getInstance().getGroup(player) + "] ";
		res += player.getName();
		if(BooleanOption.getOption("player.location").isSet())
			res += " (" + lo + ")";
		if(BooleanOption.getOption("player.id").isSet())
			res += " " + player.getEntityId();
		if(BooleanOption.getOption("player.health").isSet())
			res += " - " + player.getHealth()/(float)2 + "/10h";
		return res;
		
	}
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof PlayerTracker)
		{
			return ((PlayerTracker)o).player.equals(player);
		}
		return o == this;
	}
	@Override
	public int hashCode()
	{
		return player.hashCode();
	}
}
