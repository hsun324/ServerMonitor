package com.hsun324.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hsun324.monitor.config.BooleanOption;
import com.hsun324.monitor.swing.CheckableOption;
import com.hsun324.monitor.swing.WindowInterface;
import com.hsun324.simplebukkit.config.yaml.YAMLConfigurationWrapper;

public class Config
{
	public static int maxEvents = 200;
	public static int maxConsole = 400;
	public static List<String> privateStrings = new ArrayList<String>();
	public static List<String> omitEvents = new ArrayList<String>();
	public static void run()
	{
		WindowInterface.clearOptions();
		File file = new File(MonitorPlugin.getInstance().getDataFolder(), "config.yml");
		YAMLConfigurationWrapper config = new YAMLConfigurationWrapper(file);
		config.load();
		WindowInterface.addOption(new CheckableOption(new BooleanOption("player.group", "Shows player Permissions group. (disable if permssions is not installed)", true).setSet(config)));
		WindowInterface.addOption(new CheckableOption(new BooleanOption("player.location", "Shows player location.", true).setSet(config)));
		WindowInterface.addOption(new CheckableOption(new BooleanOption("player.id", "Shows player entity id.", true).setSet(config)));
		WindowInterface.addOption(new CheckableOption(new BooleanOption("player.health", "Shows player health.", true).setSet(config)));
		WindowInterface.addOption(new CheckableOption(new BooleanOption("world.time", "Shows the world time.", true).setSet(config)));
		WindowInterface.addOption(new CheckableOption(new BooleanOption("world.player", "Shows the number of players in a world.", true).setSet(config)));
		WindowInterface.addOption(new CheckableOption(new BooleanOption("world.entity", "Shows the number of entites in a world.", true).setSet(config)));
		privateStrings = config.getStringList("private-commands", privateStrings);
		omitEvents = config.getStringList("omit-events", omitEvents);
		maxEvents = config.getInt("max-events", maxEvents);
		maxConsole = config.getInt("max-console-lines", maxConsole);
		config.save();
	}
}
