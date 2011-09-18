package com.hsun324.monitor.swing;

import java.util.List;

import javax.swing.DefaultListModel;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.hsun324.monitor.Config;
import com.hsun324.monitor.MonitorPlugin;
import com.hsun324.monitor.bukkit.PlayerTracker;
import com.hsun324.monitor.bukkit.WorldTracker;
import com.hsun324.monitor.events.Event;

public class WindowInterface
{
	public static void addEvent(Event event)
	{
		DefaultListModel model = MonitorPlugin.getInstance().getWindow().getEventListModel();
		Object eventObject = null;
		Event ev = null;
		if(!model.isEmpty() &&
		   (eventObject = model.get(model.size() - 1)) instanceof Event && eventObject != null &&
		     (ev = (Event) eventObject).getMessage().equals(event.getMessage()))
			ev.addAmount();
		else
			model.addElement(event);
		
		while(model.size() > Config.maxEvents)
			model.remove(0);
		
		MonitorPlugin.getInstance().getWindow().getEventList().ensureIndexIsVisible(model.size() - 1);
	}
	
	public static void addLog(Event event)
	{
		DefaultListModel model = MonitorPlugin.getInstance().getWindow().getConsoleListModel();
		model.addElement(event);
		
		while(model.size() > Config.maxConsole)
			model.remove(0);
		
		MonitorPlugin.getInstance().getWindow().getConsoleList().ensureIndexIsVisible(model.size() - 1);
	}

	public static void addLogs(List<Event> data)
	{
		DefaultListModel model = MonitorPlugin.getInstance().getWindow().getConsoleListModel();
		for(Event event : data)
			model.addElement(event);
		
		while(model.size() > Config.maxConsole)
			model.remove(0);
		
		MonitorPlugin.getInstance().getWindow().getConsoleList().ensureIndexIsVisible(model.size() - 1);
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
