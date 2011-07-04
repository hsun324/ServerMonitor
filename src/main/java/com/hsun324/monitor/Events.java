package com.hsun324.monitor;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.world.*;

import com.hsun324.monitor.events.Event;
import com.hsun324.monitor.events.EventType;
import com.hsun324.monitor.swing.WindowInterface;
import com.hsun324.monitor.util.ProgressiveNumbericTicker;
import com.hsun324.simplebukkit.api.SBWorld;
import com.hsun324.simplebukkit.util.Util;
import com.hsun324.simplebukkit.bindings.EventBindings.EventDeclaration;

public class Events
{
	@EventDeclaration(value = Type.BLOCK_IGNITE, priority = Priority.Monitor)
	public static void blockIgniteRegister(BlockIgniteEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		Location l = event.getBlock().getLocation();
		String ls = l.getWorld().getName() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
		WindowInterface.addEvent(new Event("The block in " + ls + " was ignited.", EventType.WARNING, event.getType()));
	}
	
	@EventDeclaration(value = Type.EXPLOSION_PRIME, priority = Priority.Monitor)
	public static void explosionPrimeRegister(ExplosionPrimeEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		Location l = event.getEntity().getLocation();
		String ls = l.getWorld().getName() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
		if(event.getEntity() instanceof TNTPrimed)
			WindowInterface.addEvent(new Event("TNT at " + ls + " was ignited.", EventType.WARNING, event.getType()));
	}
	
	@EventDeclaration(value = Type.ENTITY_DAMAGE, priority = Priority.Monitor)
	public static void entityDamageRegister(EntityDamageEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.updatePlayerLookup();
		if(event.getEntity() instanceof Player && event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)event).getDamager() instanceof Player)
		{
			WindowInterface.addEvent(new Event(((Player)event.getEntity()).getName() + " was hit by " + ((Player)((EntityDamageByEntityEvent)event).getDamager()).getName() + ".", EventType.WARNING, event.getType()));
		}
	}
	
	@EventDeclaration(value = Type.ENTITY_DEATH, priority = Priority.Monitor)
	public static void entityDeathRegister(EntityDeathEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		if(event.getEntity() instanceof Player)
		{
			WindowInterface.addEvent(new Event(((Player)event.getEntity()).getName() + " has died.", EventType.INFO, event.getType()));
			WindowInterface.removePlayer((Player)event.getEntity());
		}
		WindowInterface.updatePlayerLookup();
	}
	
	@EventDeclaration(value = Type.LIGHTNING_STRIKE, priority = Priority.Monitor)
	public static void lightningStickRegister(LightningStrikeEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		String ls = event.getLightning().getLocation().getBlockX() + ", " + event.getLightning().getLocation().getBlockY() + ", " + event.getLightning().getLocation().getBlockZ();
		WindowInterface.addEvent(new Event("Lightning has struck " + event.getWorld().getName() + " at " + ls + ".", EventType.INFO, event.getType()));
	}

	@EventDeclaration(value = Type.PIG_ZAP, priority = Priority.Monitor)
	public static void pigZapRegister(PigZapEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		Location l = event.getEntity().getLocation();
		String ls = l.getWorld().getName() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
		WindowInterface.addEvent(new Event("Pig at " + ls + " was zapped.", EventType.WARNING, event.getType()));
	}

	@EventDeclaration(value = Type.CREEPER_POWER, priority = Priority.Monitor)
	public static void creeperPowerRegister(CreeperPowerEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		Location l = event.getEntity().getLocation();
		String ls = l.getWorld().getName() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
		WindowInterface.addEvent(new Event("Creeper at " + ls + " was powered.", EventType.WARNING, event.getType()));
	}
	
	@EventDeclaration(value = Type.PLAYER_COMMAND_PREPROCESS, priority = Priority.Monitor)
	public static void serverCommandRegister(PlayerCommandPreprocessEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		String message = event.getMessage().toLowerCase();
		if(message.startsWith("//"))
			WindowInterface.addEvent(new Event(event.getPlayer().getName() + " used the double-slash command: " + event.getMessage(), EventType.WARNING, event.getType()));
		else
		{
			String localMessage = message;
			if(localMessage.startsWith("/"))
				localMessage = localMessage.substring(1);
			if(localMessage.indexOf(' ') > -1)
				localMessage = localMessage.substring(0, localMessage.indexOf(' '));
			for(String privateString : Config.privateStrings)
				if(privateString.equals(localMessage))
				{
					WindowInterface.addEvent(new Event(event.getPlayer().getName() + " used the private command: " + localMessage, EventType.USER, event.getType()));
					return;
				}
			WindowInterface.addEvent(new Event(event.getPlayer().getName() + " used the command: " + event.getMessage(), EventType.USER, event.getType()));
		}
		
	}

	@EventDeclaration(value = Type.PLAYER_CHAT, priority = Priority.Monitor)
	public static void playerChatRegister(PlayerChatEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getFormat(), EventType.TEXT, event.getType()));
	}
	
	@EventDeclaration(value = Type.PLAYER_MOVE, priority = Priority.Monitor)
	public static void playerMoveWatch(PlayerMoveEvent event)
	{
		WindowInterface.updatePlayerLookup();
	}

	@EventDeclaration(value = Type.PLAYER_LOGIN, priority = Priority.Monitor)
	public static void playerLoginRegister(PlayerLoginEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getPlayer().getName() + " has attempted to login.", EventType.USER, event.getType()));
	}
	
	@EventDeclaration(value = Type.PLAYER_JOIN, priority = Priority.Monitor)
	public static void playerJoinRegister(PlayerJoinEvent event)
	{
		WindowInterface.addPlayer(event.getPlayer());
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getPlayer().getName() + " has joined.", EventType.USER, event.getType()));
	}
	
	@EventDeclaration(value = Type.PLAYER_QUIT, priority = Priority.Monitor)
	public static void playerQuitRegister(PlayerQuitEvent event)
	{
		WindowInterface.removePlayer(event.getPlayer());
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getPlayer().getName() + " has quitted.", EventType.USER, event.getType()));
	}

	@EventDeclaration(value = Type.PLAYER_RESPAWN, priority = Priority.Monitor)
	public static void playerRespawnRegister(PlayerRespawnEvent event)
	{
		WindowInterface.addPlayer(event.getPlayer());
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getPlayer().getName() + " has respawned.", EventType.USER, event.getType()));
	}

	@EventDeclaration(value = Type.PLAYER_TELEPORT, priority = Priority.Monitor)
	public static void playerTeleportRegister(PlayerTeleportEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		if(!event.getFrom().getWorld().equals(event.getTo().getWorld()))
			WindowInterface.addEvent(new Event(event.getPlayer().getName() + " has teleported from " + event.getFrom().getWorld().getName() + " to " + event.getTo().getWorld().getName() + ".", EventType.USER, event.getType()));
		
	}

	@EventDeclaration(value = Type.VEHICLE_CREATE, priority = Priority.Monitor)
	public static void vehicleCreateRegister(VehicleCreateEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		Location l = event.getVehicle().getLocation();
		String ls = l.getWorld().getName() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();

		Vehicle vehicle = event.getVehicle();
		String vehicleName = vehicle.getClass().getSimpleName();
		if(vehicle instanceof StorageMinecart)
			vehicleName = "Storage Minecart";
		else if(vehicle instanceof PoweredMinecart)
			vehicleName = "Powered Minecart";
		else if(vehicle instanceof Minecart)
			vehicleName = "Minecart";
		else if(vehicle instanceof Boat)
			vehicleName = "Boat";
		WindowInterface.addEvent(new Event("A " + vehicleName + " at " + ls + " was created.", EventType.INFO, event.getType()));
	}

	@EventDeclaration(value = Type.VEHICLE_DESTROY, priority = Priority.Monitor)
	public static void vehicleDestroyRegister(VehicleDestroyEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		Location l = event.getVehicle().getLocation();
		String ls = l.getWorld().getName() + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();

		Vehicle vehicle = event.getVehicle();
		String vehicleName = vehicle.getClass().getSimpleName();
		if(vehicle instanceof StorageMinecart)
			vehicleName = "Storage Minecart";
		else if(vehicle instanceof PoweredMinecart)
			vehicleName = "Powered Minecart";
		else if(vehicle instanceof Minecart)
			vehicleName = "Minecart";
		else if(vehicle instanceof Boat)
			vehicleName = "Boat";
		WindowInterface.addEvent(new Event("A " + vehicleName + " at " + ls + " was destroyed.", EventType.INFO, event.getType()));
	}

	@EventDeclaration(value = Type.WEATHER_CHANGE, priority = Priority.Monitor)
	public static void weatherChangeRegister(WeatherChangeEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		SBWorld sbworld = new SBWorld(event.getWorld());
		WindowInterface.addEvent(new Event(event.getWorld().getName() + "'s weather has changed to " + Util.formatEnumName(sbworld.getWeather()), EventType.INFO, event.getType()));
	}

	@EventDeclaration(value = Type.WORLD_LOAD, priority = Priority.Monitor)
	public static void worldLoadRegister(WorldLoadEvent event)
	{
		WindowInterface.addWorld(event.getWorld());
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getWorld().getName() + " was loaded.", EventType.INFO, event.getType()));
	}

	@EventDeclaration(value = Type.WORLD_UNLOAD, priority = Priority.Monitor)
	public static void worldUnloadRegister(WorldUnloadEvent event)
	{
		WindowInterface.addWorld(event.getWorld());
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getWorld().getName() + " was unloaded.", EventType.INFO, event.getType()));
	}

	@EventDeclaration(value = Type.WORLD_SAVE, priority = Priority.Monitor)
	public static void worldSaveRegister(WorldSaveEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getWorld().getName() + " was saved.", EventType.INFO, event.getType()));
	}

	@EventDeclaration(value = Type.PLAYER_INTERACT, priority = Priority.Monitor)
	public static void interactTick(PlayerInteractEvent event)
	{
		ProgressiveNumbericTicker.playerActivityTicker.tick();
		ProgressiveNumbericTicker.playerInteractTicker.tick();
	}

	@EventDeclaration(value = Type.BLOCK_BREAK, priority = Priority.Monitor)
	public static void blockBreakTick(BlockBreakEvent event)
	{
		ProgressiveNumbericTicker.playerActivityTicker.tick();
		ProgressiveNumbericTicker.blockBreakTicker.tick();
	}

	@EventDeclaration(value = Type.PLAYER_CHAT, priority = Priority.Monitor)
	public static void chatTick(PlayerChatEvent event)
	{
		ProgressiveNumbericTicker.playerActivityTicker.tick();
		ProgressiveNumbericTicker.talkTicker.tick();
	}

	@EventDeclaration(value = Type.PLAYER_COMMAND_PREPROCESS, priority = Priority.Monitor)
	public static void commandTick(PlayerCommandPreprocessEvent event)
	{
		ProgressiveNumbericTicker.playerActivityTicker.tick();
		ProgressiveNumbericTicker.talkTicker.tick();
	}

	@EventDeclaration(value = Type.BLOCK_PLACE, priority = Priority.Monitor)
	public static void blockPlaceTick(BlockPlaceEvent event)
	{
		ProgressiveNumbericTicker.playerActivityTicker.tick();
		ProgressiveNumbericTicker.blockPlaceTicker.tick();
	}

	@EventDeclaration(value = Type.PLUGIN_ENABLE, priority = Priority.Monitor)
	public static void registerPluginEnable(PluginEnableEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getPlugin().getDescription().getName()+ " was enabled.", EventType.INFO, event.getType()));
	}

	@EventDeclaration(value = Type.PLUGIN_DISABLE, priority = Priority.Monitor)
	public static void registerPluginDisable(PluginDisableEvent event)
	{
		if(Config.omitEvents.contains(event.getType().name()))
			return;
		WindowInterface.addEvent(new Event(event.getPlugin().getDescription().getName()+ " was disable.", EventType.INFO, event.getType()));
	}
}
