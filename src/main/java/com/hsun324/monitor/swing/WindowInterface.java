package com.hsun324.monitor.swing;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.hsun324.monitor.MonitorPlugin;
import com.hsun324.monitor.bukkit.PlayerTracker;
import com.hsun324.monitor.bukkit.WorldTracker;
import com.hsun324.monitor.events.Event;

public class WindowInterface
{
	public static void addEvent(Event event)
	{
		MonitorPlugin.getInstance().getWindow().getEventListModel().addElement(event);
		
		MonitorPlugin.getInstance().getWindow().getEventList().ensureIndexIsVisible(MonitorPlugin.getInstance().getWindow().getEventListModel().size() - 1);
		MonitorPlugin.getInstance().getWindow().getEventList().repaint();
	}
	
	public static void addLog(Event event)
	{
		MonitorPlugin.getInstance().getWindow().getConsoleListModel().addElement(event);
		
		MonitorPlugin.getInstance().getWindow().getConsoleList().ensureIndexIsVisible(MonitorPlugin.getInstance().getWindow().getConsoleListModel().size() - 1);
		MonitorPlugin.getInstance().getWindow().getConsoleList().repaint();
	}
	
	public static void removeWorld(World world)
	{
		MonitorPlugin.getInstance().getWindow().getWorldListModel().removeElement(new WorldTracker(world));
	}
	
	public static void addWorld(World world)
	{
		MonitorPlugin.getInstance().getWindow().getWorldListModel().addElement(new WorldTracker(world));
	}
	
	public static void removePlayer(Player player)
	{
		MonitorPlugin.getInstance().getWindow().getPlayerListModel().removeElement(new PlayerTracker(player));
	}
	
	public static void addPlayer(Player player)
	{
		MonitorPlugin.getInstance().getWindow().getPlayerListModel().addElement(new PlayerTracker(player));
	}
	
	public static void updatePlayerLookup()
	{
		MonitorPlugin.getInstance().getWindow().getPlayerList().repaint();
	}
	
	public static void addOption(CheckableOption option)
	{
		MonitorPlugin.getInstance().getWindow().getOptionsListModel().addElement(option);
	}

	public static void clearOptions()
	{
		MonitorPlugin.getInstance().getWindow().getOptionsListModel().clear();
	}
}
