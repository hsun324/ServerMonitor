package com.hsun324.monitor;

import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.swing.UIManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.hsun324.monitor.swing.MainWindow;
import com.hsun324.monitor.swing.WindowInterface;
import com.hsun324.simplebukkit.bindings.EventBindings;

public class MonitorPlugin extends JavaPlugin
{
	private static MonitorPlugin instance;
	public static MonitorPlugin getInstance()
	{
		return instance;
	}
	
	private Logger logger = Logger.getLogger("Minecraft.ServerMonitor");
	private Logger baseLogger = Logger.getLogger("Minecraft");
	private String version = "";
	private Handler loggerHandler = null;
	private MainWindow window = new MainWindow();
	
	public Logger getLogger()
	{
		return logger;
	}
	public String getVersion()
	{
		return version;
	}
	public PluginManager getPM()
	{
		return getServer().getPluginManager();
	}
	public MainWindow getWindow()
	{
		return window;
	}
	public void onDisable()
	{
		EventBindings.getInstance().removeEventClass(Events.class);
		window.close();
		baseLogger.removeHandler(loggerHandler);
		logger.info("ServerMonitor v" + getVersion() + ": Disabled");
	}
	public void onEnable()
	{
		version = getDescription().getVersion();
		MonitorPlugin.instance = this;
		Config.run();
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) { }
		Thread windowThread = new Thread("thread-window")
		{
			@Override
			public void run()
			{
				window.open();
			}
		};
		windowThread.setDaemon(false);
		windowThread.run();
		
		for(Player player : getServer().getOnlinePlayers())
			WindowInterface.addPlayer(player);
		for(World world : getServer().getWorlds())
			WindowInterface.addWorld(world);
		EventBindings.getInstance().addEventClass(Events.class);
		loggerHandler = new LoggerHandler();
		baseLogger.addHandler(loggerHandler);
		logger.info("ServerMonitor v" + getVersion() + ": Enabled");
	}
}
