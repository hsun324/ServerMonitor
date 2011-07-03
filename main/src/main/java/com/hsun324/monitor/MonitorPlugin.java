package com.hsun324.monitor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.UIManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.hsun324.monitor.events.Event;
import com.hsun324.monitor.events.EventType;
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
	
	private Logger logger = Logger.getLogger("Minecraft.BukkitMonitor");
	private Logger baseLogger = Logger.getLogger("Minecraft");
	private String version = "";
	private MainWindow window = new MainWindow();
	private SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss");
	private Handler loggerHandler = new Handler()
	{
		public void publish(LogRecord record)
		{
			String message = "";
			if(record.getMessage() != null)
			{
				message = "[" + format.format(new Date(record.getMillis())) + "] " + record.getMessage().replace("\u00A7", "&");
			}
			if(record.getLevel() == Level.WARNING)
				WindowInterface.addLog(new Event(message, EventType.WARNING));
			else if(record.getLevel() == Level.SEVERE)
				WindowInterface.addLog(new Event(message, EventType.ERROR));
			else
				WindowInterface.addLog(new Event(message, EventType.INFO));
		}
		public void flush() {}
		public void close() throws SecurityException {}
	};
	
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
		logger.info("BukkitMonitor v" + getVersion() + ": Disabled");
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
		window.open();
		for(Player player : getServer().getOnlinePlayers())
			WindowInterface.addPlayer(player);
		for(World world : getServer().getWorlds())
			WindowInterface.addWorld(world);
		EventBindings.getInstance().addEventClass(Events.class);
		baseLogger.addHandler(loggerHandler);
		logger.info("BukkitMonitor v" + getVersion() + ": Enabled");
	}
}
