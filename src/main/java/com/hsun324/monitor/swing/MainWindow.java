package com.hsun324.monitor.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import net.minecraft.server.MinecraftServer;

import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.*;

import com.hsun324.monitor.MonitorPlugin;
import com.hsun324.monitor.bukkit.PlayerTracker;
import com.hsun324.monitor.bukkit.WorldTracker;
import com.hsun324.monitor.events.Event;
import com.hsun324.monitor.events.EventType;
import com.hsun324.monitor.icons.IconsList;
import com.hsun324.monitor.util.ProgressiveNumbericTicker;
import com.hsun324.simplebukkit.player.ColoredMessageSender;
import com.hsun324.simplebukkit.util.BukkitUtils;

import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextField;
import javax.swing.JDialog;

public class MainWindow
{

	private JFrame window = null;
	private JTabbedPane mainPane = null;
	private JPanel playerPanel = null;
	private JScrollPane playerListScroll = null;
	private JPanel playerActionsPanel = null;
	private JList playerList = null;
	private DefaultListModel playerListModel = null;
	private JButton playerWarnAction = null;
	private JButton playerWarnReasonAction = null;
	private JButton playerKickAction = null;
	private JButton playerKickReasonAction = null;
	private JButton playerMessageAction = null;
	private JPanel worldPanel = null;
	private JLabel playerListLabel1 = null;
	private JLabel worldListLabel = null;
	private JScrollPane worldListScroll = null;
	private JPanel worldActionPanel = null;
	private JButton worldAnnounceAction = null;
	private JList worldList = null;
	private DefaultListModel worldListModel = null;
	private JScrollPane eventScrollPane = null;
	private JList eventList = null;
	private DefaultListModel eventListModel = null;
	private JButton playersRefreshAction = null;
	private JScrollPane consoleScrollPanel = null;
	private JList consoleList = null;
	private DefaultListModel consoleListModel = null;
	private JButton worldRefreshAction = null;
	private JPanel serverPanel = null;
	private JLabel serverLabel = null;
	private JLabel serverAllocatedLabel = null;
	private JLabel serverAllocated = null;
	private JLabel worldsLabel = null;
	private JLabel serverPeopleLabel = null;
	private JLabel serverPlayer = null;
	private JLabel serverAvailableLabel = null;
	private JLabel serverAvailable = null;
	private JLabel worldEntitiesLabel = null;
	private JLabel worldEntites = null;
	private JLabel serverMaximumLabel = null;
	private JLabel serverMaximum = null;
	private JButton serverReloadAction = null;
	private JButton serverStopAction = null;
	
	private Timer timer = new Timer();
	private JLabel worldDetailsLabel = null;
	private JLabel worldListActionsLabel = null;
	private JLabel worldDateTimeLabel = null;
	private JLabel worldDateTime = null;
	private JLabel worldPlayersLabel = null;
	private JLabel worldPlayers = null;
	private JLabel worldEntitesLabel = null;
	private JLabel worldEntities = null;
	private JButton serverAnnounceAction = null;
	private JPanel consolePanel = null;
	private JTextField consoleInputField = null;
	public void open()
	{
		getJFrame().setVisible(true);
		timer.schedule(new TimerTask(){
			@Override
			public void run()
			{
				try
				{
					Runtime runtime = Runtime.getRuntime();
					long max = runtime.maxMemory();
					long available = runtime.totalMemory();
					long allocated = available - runtime.freeMemory();
					double allmb = Math.round((double)allocated / 10485.76) / (double)100;
					double avamb = Math.round((double)available / 10485.76) / (double)100;
					double maxmb = Math.round((double)max / 10485.76) / (double)100;
					double alloava = Math.round((double)allocated / (double)available * 10000) / (double)100;
					double avaomax = Math.round((double)available / (double)max * 10000) / (double)100;
					serverAllocated.setText(allmb + " MB (" + alloava + "%)");
					serverAvailable.setText(avamb + " MB (" + avaomax + "%)");
					serverMaximum.setText(maxmb + " MB");
					
					serverPlayer.setText(Integer.toString(MonitorPlugin.getInstance().getServer().getOnlinePlayers().length));
					int enti = 0;
					for(World world : MonitorPlugin.getInstance().getServer().getWorlds())
						enti += world.getEntities().size();
					worldEntites.setText(Integer.toString(enti));
					getWorldList().repaint();
					if(!getWorldList().isSelectionEmpty())
					{
						World world = ((WorldTracker) getWorldList().getSelectedValue()).getWorld();
						worldDateTime.setText(BukkitUtils.getWorldTimeString(world));
						worldPlayers.setText(Integer.toString(world.getPlayers().size()));
						worldEntities.setText(Integer.toString(world.getEntities().size()));
					}
				}
				catch(Exception e) { }
			}
		}, 200, 200);
		timer.schedule(new TimerTask(){
			private int p = 0;
			@Override
			public void run()
			{
				try
				{
					Runtime runtime = Runtime.getRuntime();
					long max = runtime.maxMemory();
					long available = runtime.totalMemory();
					long allocated = available - runtime.freeMemory();
					int players = MonitorPlugin.getInstance().getServer().getOnlinePlayers().length;
					int worlds = MonitorPlugin.getInstance().getServer().getWorlds().size();
					
					getMemoryMapViewer().addValue(Color.YELLOW, (double)available / max * 100d);
					getMemoryMapViewer().addValue(Color.GREEN, (double)allocated / max * 100d);

					getServerStatsViewer().addValue(Color.YELLOW, worlds);
					getServerStatsViewer().addValue(Color.GREEN, players);

					getPlayerStatsViewer().addValue(Color.ORANGE, ProgressiveNumbericTicker.talkTicker.total());
					getPlayerStatsViewer().addValue(Color.RED, ProgressiveNumbericTicker.blockBreakTicker.total());
					getPlayerStatsViewer().addValue(Color.BLUE, ProgressiveNumbericTicker.playerInteractTicker.total());
					getPlayerStatsViewer().addValue(Color.GREEN, ProgressiveNumbericTicker.blockPlaceTicker.total());
					getPlayerStatsViewer().addValue(Color.WHITE, ProgressiveNumbericTicker.playerActivityTicker.total());

					int entities = 0, mobs = 0, animals = 0, vehicles = 0, pigs = 0, sheep = 0, cows = 0, chickens = 0;
					int squids = 0, carts = 0, poweredcarts = 0, storagecarts = 0, boats = 0, zombies = 0;
					int creepers = 0, skeletons = 0, spiders = 0, ghasts = 0, pigmans = 0, slimes = 0, humans = 0;
					int giants = 0, wolves = 0, others = 0;
					
					for(World world : MonitorPlugin.getInstance().getServer().getWorlds())
					{
						entities += world.getEntities().size();
						for(Entity e : world.getEntities())
						{
							if(e instanceof Pig)
							{
								animals++;
								pigs++;
							}
							else if(e instanceof Sheep)
							{
								animals++;
								sheep++;
							}
							else if(e instanceof Cow)
							{
								animals++;
								cows++;
							}
							else if(e instanceof Chicken)
							{
								animals++;
								chickens++;
							}
							else if(e instanceof Squid)
							{
								animals++;
								squids++;
							}
							else if(e instanceof Minecart)
							{
								vehicles++;
								carts++;
								if(e instanceof PoweredMinecart)
									poweredcarts++;
								else if(e instanceof StorageMinecart)
									storagecarts++;
							}
							else if(e instanceof Boat)
							{
								vehicles++;
								boats++;
							}
							else if(e instanceof Zombie)
							{
								mobs++;
								zombies++;
							}
							else if(e instanceof Spider)
							{
								mobs++;
								spiders++;
							}
							else if(e instanceof Skeleton)
							{
								mobs++;
								skeletons++;
							}
							else if(e instanceof Creeper)
							{
								mobs++;
								creepers++;
							}
							else if(e instanceof Ghast)
							{
								mobs++;
								ghasts++;
							}
							else if(e instanceof PigZombie)
							{
								mobs++;
								pigmans++;
							}
							else if(e instanceof Giant)
							{
								mobs++;
								giants++;
							}
							else if(e instanceof Monster)
							{
								mobs++;
								humans++;
							}
							else if(e instanceof Wolf)
							{
								mobs++;
								wolves++;
							}
							else if(e instanceof Slime)
							{
								mobs++;
								slimes++;
							}
							else
								others++;
						}
					}

					getEntityStatsViewer().addValue(Color.ORANGE, others);
					getEntityStatsViewer().addValue(Color.YELLOW, vehicles);
					getEntityStatsViewer().addValue(Color.BLUE, animals);
					getEntityStatsViewer().addValue(Color.RED, mobs);
					getEntityStatsViewer().addValue(Color.GREEN, entities);
					
					getHostleEntityStatsViewer().addValue(Color.GRAY, zombies);
					getHostleEntityStatsViewer().addValue(Color.WHITE, skeletons);
					getHostleEntityStatsViewer().addValue(Color.MAGENTA, creepers);
					getHostleEntityStatsViewer().addValue(Color.DARK_GRAY, spiders);
					getHostleEntityStatsViewer().addValue(Color.CYAN, ghasts);
					getHostleEntityStatsViewer().addValue(Color.RED, pigmans);
					getHostleEntityStatsViewer().addValue(Color.ORANGE, giants);
					getHostleEntityStatsViewer().addValue(Color.PINK, humans);
					getHostleEntityStatsViewer().addValue(Color.YELLOW, slimes);
					getHostleEntityStatsViewer().addValue(Color.BLUE, wolves);
					getHostleEntityStatsViewer().addValue(Color.GREEN, mobs);
					
					getFriendlyEntityStatsViewer().addValue(Color.PINK, pigs);
					getFriendlyEntityStatsViewer().addValue(Color.WHITE, sheep);
					getFriendlyEntityStatsViewer().addValue(Color.GRAY, cows);
					getFriendlyEntityStatsViewer().addValue(Color.YELLOW, chickens);
					getFriendlyEntityStatsViewer().addValue(Color.CYAN, squids);
					getFriendlyEntityStatsViewer().addValue(Color.GREEN, animals);
					
					if(p++ % 2 == 0)
					{
						getMemoryMapViewer().repaint();
						getServerStatsViewer().repaint();
						getEntityStatsViewer().repaint();
						getPlayerStatsViewer().repaint();
						getHostleEntityStatsViewer().repaint();
						getFriendlyEntityStatsViewer().repaint();
					}
				}
				
				catch (Exception e) {}
			}
		}, 250, 250);
	}
	public void close()
	{
		timer.cancel();
		getJFrame().setVisible(false);
		getJFrame().dispose();
	}
	
	/**
	 * This method initializes jFrame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	private JFrame getJFrame() {
		if (window == null) {
			window = new JFrame();
			window.setSize(new Dimension(600, 800));
			window.setContentPane(getMainPanel());
			window.setTitle("Minecraft Server GUI");
		}
		return window;
	}

	/**
	 * This method initializes mainPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getMainPane() {
		if (mainPane == null) {
			mainPane = new JTabbedPane();
			mainPane.setFont(new Font("Verdana", Font.PLAIN, 16));
			mainPane.addTab("Server", IconsList.serverIcon, getServerPanel(), null);
			mainPane.addTab("Player", IconsList.userIcon, getPlayerPanel(), null);
			mainPane.addTab("World", IconsList.worldIcon, getPlayerListPanel(), null);
			mainPane.addTab("Event", IconsList.infoIcon, getEventScrollPane(), null);
			mainPane.addTab("Console", IconsList.terminalIcon, getConsolePanel(), null);
			mainPane.addTab("Graphs", IconsList.tableIcon, getGraphPane(), null);
		}
		return mainPane;
	}
	/**
	 * This method initializes playerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPlayerPanel() {
		if (playerPanel == null) {
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.insets = new Insets(3, 6, 3, 3);
			gridBagConstraints61.ipadx = 0;
			gridBagConstraints61.ipady = 0;
			gridBagConstraints61.anchor = GridBagConstraints.CENTER;
			gridBagConstraints61.gridwidth = 2;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.BOTH;
			gridBagConstraints51.gridy = 1;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.weighty = 1.0;
			gridBagConstraints51.insets = new Insets(0, 3, 3, 3);
			gridBagConstraints51.ipadx = 0;
			gridBagConstraints51.ipady = 0;
			gridBagConstraints51.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 0.0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 0.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridwidth = 1;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(0, 0, 3, 3);
			gridBagConstraints1.gridx = 1;
			playerPanel = new JPanel();
			playerPanel.setLayout(new GridBagLayout());
			playerPanel.setBorder(new LineBorder(Color.BLACK));
			playerPanel.add(getPlayerListLabel1(), gridBagConstraints61);
			playerPanel.add(getPlayerListScroll(), gridBagConstraints51);
			playerPanel.add(getPlayerActionsPanel(), gridBagConstraints1);
		}
		return playerPanel;
	}
	private JLabel getPlayerListLabel1()
	{
		if(playerListLabel1 == null)
		{
			playerListLabel1 = new JLabel("Online Players");
			playerListLabel1.setFont(new Font("Verdana", Font.PLAIN, 12));
		}
		return playerListLabel1;
	}
	/**
	 * This method initializes playerListScroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getPlayerListScroll() {
		if (playerListScroll == null) {
			playerListScroll = new JScrollPane();
			playerListScroll.setViewportView(getPlayerList());
		}
		return playerListScroll;
	}
	/**
	 * This method initializes playerActionsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPlayerActionsPanel() {
		if (playerActionsPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 1;
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridy = 5;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = -1;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.weighty = 1.0;
			gridBagConstraints7.gridx = -1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 4;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.anchor = GridBagConstraints.CENTER;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 3;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.anchor = GridBagConstraints.CENTER;
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.anchor = GridBagConstraints.CENTER;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.anchor = GridBagConstraints.CENTER;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 0;
			playerActionsPanel = new JPanel();
			playerActionsPanel.setLayout(new GridBagLayout());
			playerActionsPanel.add(getPlayerWarnAction(), gridBagConstraints2);
			playerActionsPanel.add(getPlayerWarnReasonAction(), gridBagConstraints3);
			playerActionsPanel.add(getPlayerKickAction(), gridBagConstraints4);
			playerActionsPanel.add(getPlayerKickReasonAction(), gridBagConstraints5);
			playerActionsPanel.add(getPlayerMessageAction(), gridBagConstraints6);
			playerActionsPanel.add(getPlayersRefreshAction(), gridBagConstraints13);
		}
		return playerActionsPanel;
	}
	/**
	 * This method initializes playerList	
	 * 	
	 * @return javax.swing.JList	
	 */
	public JList getPlayerList() {
		if (playerList == null) {
			playerList = new JList();
			playerList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			playerList.setModel(getPlayerListModel());
			playerList.setFont(new Font("Verdana", Font.PLAIN, 12));
			playerList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					if(playerList.isSelectionEmpty())
					{
						playerWarnAction.setEnabled(false);
						playerWarnReasonAction.setEnabled(false);
						playerKickAction.setEnabled(false);
						playerKickReasonAction.setEnabled(false);
						playerMessageAction.setEnabled(false);
					}
					else
					{
						playerWarnAction.setEnabled(true);
						playerWarnReasonAction.setEnabled(true);
						playerKickAction.setEnabled(true);
						playerKickReasonAction.setEnabled(true);
						playerMessageAction.setEnabled(true);
					}
				}
			});
		}
		return playerList;
	}
	/**
	 * This method initializes playerWarnAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPlayerWarnAction() {
		if (playerWarnAction == null) {
			playerWarnAction = new JButton();
			playerWarnAction.setText("Warn Player");
			playerWarnAction.setEnabled(false);
			playerWarnAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					if(!getPlayerList().isSelectionEmpty())
					{
						for(Object index : getPlayerList().getSelectedValues())
						{
							PlayerTracker pt = (PlayerTracker) index;
							ColoredMessageSender.sendInfoMessage(pt.getPlayer(), "You have been &6warned&e by the owner of this server.");
						}
					}
				}
			});
		}
		return playerWarnAction;
	}
	/**
	 * This method initializes playerWarnReasonAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPlayerWarnReasonAction() {
		if (playerWarnReasonAction == null) {
			playerWarnReasonAction = new JButton();
			playerWarnReasonAction.setText("Warn Player with Reason");
			playerWarnReasonAction.setEnabled(false);
			playerWarnReasonAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					if(!getPlayerList().isSelectionEmpty())
					{
						String input = (String) JOptionPane.showInputDialog(window, "Input a warning message.", "Input Message", JOptionPane.QUESTION_MESSAGE, IconsList.infoIcon, null, "");
						if(input != null && !input.trim().isEmpty())
						{
							for(Object index : getPlayerList().getSelectedValues())
							{
								PlayerTracker pt = (PlayerTracker) index;
								ColoredMessageSender.sendInfoMessage(pt.getPlayer(), input.trim());
								ColoredMessageSender.sendInfoMessage(pt.getPlayer(), "You have been &6warned&e by the owner of this server.");
							}
						}
					}
				}
			});
		}
		return playerWarnReasonAction;
	}
	/**
	 * This method initializes playerKickAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPlayerKickAction() {
		if (playerKickAction == null) {
			playerKickAction = new JButton();
			playerKickAction.setText("Kick Player");
			playerKickAction.setEnabled(false);
			playerKickAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					if(!getPlayerList().isSelectionEmpty())
					{
						for(Object index : getPlayerList().getSelectedValues())
						{
							PlayerTracker pt = (PlayerTracker) index;
							pt.getPlayer().kickPlayer("Kicked by owner of server.");
						}
					}
				}
			});
		}
		return playerKickAction;
	}
	/**
	 * This method initializes playerKickReasonAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPlayerKickReasonAction() {
		if (playerKickReasonAction == null) {
			playerKickReasonAction = new JButton();
			playerKickReasonAction.setText("Kick Player with Reason");
			playerKickReasonAction.setEnabled(false);
			playerKickReasonAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					if(!getPlayerList().isSelectionEmpty())
					{
						String input = (String) JOptionPane.showInputDialog(window, "Input a kick message.", "Input Message", JOptionPane.QUESTION_MESSAGE, IconsList.infoIcon, null, "");
						if(input != null && !input.trim().isEmpty())
						{
							for(Object index : getPlayerList().getSelectedValues())
							{
								PlayerTracker pt = (PlayerTracker) index;
								pt.getPlayer().kickPlayer("Kicked: " + input);
							}
						}
					}
				}
			});
		}
		return playerKickReasonAction;
	}
	/**
	 * This method initializes playerMessageAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPlayerMessageAction() {
		if (playerMessageAction == null) {
			playerMessageAction = new JButton();
			playerMessageAction.setText("Message Player");
			playerMessageAction.setEnabled(false);
			playerMessageAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					if(!getPlayerList().isSelectionEmpty())
					{
						String input = (String) JOptionPane.showInputDialog(window, "Input a message.", "Input Message", JOptionPane.QUESTION_MESSAGE, IconsList.infoIcon, null, "");
						if(input != null && !input.trim().isEmpty())
						{
							for(Object index : getPlayerList().getSelectedValues())
							{
								PlayerTracker pt = (PlayerTracker) index;
								ColoredMessageSender.sendInfoMessage(pt.getPlayer(), "CONSOLE: " + input.trim());
							}
						}
					}
				}
			});
		}
		return playerMessageAction;
	}
	/**
	 * This method initializes playerListPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPlayerListPanel() {
		if (worldPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.insets = new Insets(0, 0, 3, 3);
			gridBagConstraints10.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints10.gridy = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.insets = new Insets(0, 3, 3, 3);
			gridBagConstraints9.ipadx = 0;
			gridBagConstraints9.ipady = 0;
			gridBagConstraints9.gridx = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.ipadx = 0;
			gridBagConstraints8.ipady = 0;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.weighty = 0.0;
			gridBagConstraints8.weightx = 0.0;
			gridBagConstraints8.gridwidth = 2;
			gridBagConstraints8.insets = new Insets(3, 6, 3, 3);
			gridBagConstraints8.gridy = 0;
			worldListLabel = new JLabel();
			worldListLabel.setText("Online Worlds");
			worldListLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldPanel = new JPanel();
			worldPanel.setBorder(new LineBorder(Color.BLACK));
			worldPanel.setLayout(new GridBagLayout());
			worldPanel.add(worldListLabel, gridBagConstraints8);
			worldPanel.add(getWorldListScroll(), gridBagConstraints9);
			worldPanel.add(getWorldActionPanel(), gridBagConstraints10);
		}
		return worldPanel;
	}
	/**
	 * This method initializes worldListScroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getWorldListScroll() {
		if (worldListScroll == null) {
			worldListScroll = new JScrollPane();
			worldListScroll.setViewportView(getWorldList());
		}
		return worldListScroll;
	}
	/**
	 * This method initializes worldActionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWorldActionPanel() {
		if (worldActionPanel == null) {
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints35.insets = new Insets(3, 3, 0, 3);
			gridBagConstraints35.gridy = 9;
			worldEntities = new JLabel();
			worldEntities.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldEntities.setText("?");
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.gridx = 0;
			gridBagConstraints34.insets = new Insets(3, 3, 0, 3);
			gridBagConstraints34.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints34.gridy = 8;
			worldEntitesLabel = new JLabel();
			worldEntitesLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldEntitesLabel.setText("Entities in World:");
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.insets = new Insets(3, 3, 0, 3);
			gridBagConstraints33.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints33.gridy = 7;
			worldPlayers = new JLabel();
			worldPlayers.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldPlayers.setText("?");
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.insets = new Insets(3, 3, 0, 3);
			gridBagConstraints32.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints32.gridy = 6;
			worldPlayersLabel = new JLabel();
			worldPlayersLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldPlayersLabel.setText("Players in World:");
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new Insets(3, 3, 0, 3);
			gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.gridy = 5;
			worldDateTime = new JLabel();
			worldDateTime.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldDateTime.setText("??-??-???? ??:?? ??");
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.gridx = 0;
			gridBagConstraints30.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.ipadx = 0;
			gridBagConstraints30.ipady = 0;
			gridBagConstraints30.insets = new Insets(3, 3, 0, 3);
			gridBagConstraints30.gridy = 4;
			worldDateTimeLabel = new JLabel();
			worldDateTimeLabel.setText("Date and Time:");
			worldDateTimeLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints29.ipadx = 3;
			gridBagConstraints29.ipady = 3;
			gridBagConstraints29.gridy = 0;
			worldListActionsLabel = new JLabel();
			worldListActionsLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
			worldListActionsLabel.setText("World List");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.ipadx = 3;
			gridBagConstraints11.ipady = 3;
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 2;
			worldDetailsLabel = new JLabel();
			worldDetailsLabel.setText("World");
			worldDetailsLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints14.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new Insets(3, 3, 0, 3);
			gridBagConstraints12.gridy = 10;
			worldActionPanel = new JPanel();
			worldActionPanel.setLayout(new GridBagLayout());
			worldActionPanel.add(getWorldAnnounceAction(), gridBagConstraints12);
			worldActionPanel.add(getWorldRefreshAction(), gridBagConstraints14);
			worldActionPanel.add(worldDetailsLabel, gridBagConstraints11);
			worldActionPanel.add(worldListActionsLabel, gridBagConstraints29);
			worldActionPanel.add(worldDateTimeLabel, gridBagConstraints30);
			worldActionPanel.add(worldDateTime, gridBagConstraints31);
			worldActionPanel.add(worldPlayersLabel, gridBagConstraints32);
			worldActionPanel.add(worldPlayers, gridBagConstraints33);
			worldActionPanel.add(worldEntitesLabel, gridBagConstraints34);
			worldActionPanel.add(worldEntities, gridBagConstraints35);
		}
		return worldActionPanel;
	}
	/**
	 * This method initializes worldAnnounceAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getWorldAnnounceAction() {
		if (worldAnnounceAction == null) {
			worldAnnounceAction = new JButton();
			worldAnnounceAction.setText("Announce to World");
			worldAnnounceAction.setEnabled(false);
			worldAnnounceAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					if(!getWorldList().isSelectionEmpty())
					{
						String input = (String) JOptionPane.showInputDialog(window, "Input a message.", "Input Message", JOptionPane.QUESTION_MESSAGE, IconsList.infoIcon, null, "");
						if(input != null && !input.trim().isEmpty())
						{
							for(Object index : getWorldList().getSelectedValues())
							{
								WorldTracker wt = (WorldTracker) index;
								for(Player player : wt.getWorld().getPlayers())
								{
									ColoredMessageSender.sendInfoMessage(player, "&7World Announcement:");
									ColoredMessageSender.sendInfoMessage(player, input);
								}
							}
						}
					}
				}
			});
		}
		return worldAnnounceAction;
	}
	/**
	 * This method initializes worldList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getWorldList() {
		if (worldList == null) {
			worldList = new JList();
			worldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			worldList.setModel(getWorldListModel());
			worldList.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e)
				{
					if(worldList.isSelectionEmpty())
						worldAnnounceAction.setEnabled(false);
					else
					{
						worldAnnounceAction.setEnabled(true);
					}
				}
			});
		}
		return worldList;
	}
	/**
	 * This method initializes eventScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	public JScrollPane getEventScrollPane() {
		if (eventScrollPane == null) {
			eventScrollPane = new JScrollPane();
			eventScrollPane.setViewportView(getEventList());
		}
		return eventScrollPane;
	}
	/**
	 * This method initializes eventList	
	 * 	
	 * @return javax.swing.JList	
	 */
	public JList getEventList() {
		if (eventList == null) {
			eventList = new JList();
			eventList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			eventList.setModel(getEventListModel());
			eventList.setCellRenderer(new IconListCellRenderer());
			eventList.setFont(new Font("Verdana", Font.PLAIN, 12));
		}
		return eventList;
	}
	/**
	 * This method initializes eventListModel	
	 * 	
	 * @return javax.swing.DefaultListModel	
	 */
	public DefaultListModel getWorldListModel()
	{
		if (worldListModel == null) {
			worldListModel = new DefaultListModel();
		}
		return worldListModel;
	}
	/**
	 * This method initializes eventListModel	
	 * 	
	 * @return javax.swing.DefaultListModel	
	 */
	public DefaultListModel getPlayerListModel()
	{
		if (playerListModel == null) {
			playerListModel = new DefaultListModel();
		}
		return playerListModel;
	}
	/**
	 * This method initializes eventListModel	
	 * 	
	 * @return javax.swing.DefaultListModel	
	 */
	public DefaultListModel getEventListModel()
	{
		if (eventListModel == null) {
			eventListModel = new DefaultListModel();
		}
		return eventListModel;
	}
	/**
	 * This method initializes playersRefreshAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPlayersRefreshAction() {
		if (playersRefreshAction == null) {
			playersRefreshAction = new JButton();
			playersRefreshAction.setText("Refresh Players");
			playersRefreshAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					getPlayerListModel().clear();
					for(Player player : MonitorPlugin.getInstance().getServer().getOnlinePlayers())
						WindowInterface.addPlayer(player);
				}
			});
		}
		return playersRefreshAction;
	}
	/**
	 * This method initializes consoleScrollPanel	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	public JScrollPane getConsoleScrollPanel() {
		if (consoleScrollPanel == null) {
			consoleScrollPanel = new JScrollPane();
			consoleScrollPanel.setViewportView(getConsoleList());
		}
		return consoleScrollPanel;
	}
	/**
	 * This method initializes consoleList	
	 * 	
	 * @return javax.swing.JList	
	 */
	public JList getConsoleList() {
		if (consoleList == null) {
			consoleList = new JList();
			consoleList.setModel(getConsoleListModel());
			consoleList.setCellRenderer(new IconListCellRenderer());
			consoleList.setFont(new Font("Verdana", Font.PLAIN, 12));
		}
		return consoleList;
	}
	/**
	 * This method initializes consoleListModel	
	 * 	
	 * @return javax.swing.DefaultListModel	
	 */
	public DefaultListModel getConsoleListModel() {
		if (consoleListModel == null) {
			consoleListModel = new DefaultListModel();
		}
		return consoleListModel;
	}
	/**
	 * This method initializes worldRefreshAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getWorldRefreshAction() {
		if (worldRefreshAction == null) {
			worldRefreshAction = new JButton();
			worldRefreshAction.setText("Refresh Worlds");
			worldRefreshAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					getWorldListModel().clear();
					for(World world : MonitorPlugin.getInstance().getServer().getWorlds())
						WindowInterface.addWorld(world);
				}
			});
		}
		return worldRefreshAction;
	}
	/**
	 * This method initializes serverPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getServerPanel() {
		if (serverPanel == null) {
			GridBagConstraints gridBagConstraints48 = new GridBagConstraints();
			gridBagConstraints48.gridx = 0;
			gridBagConstraints48.fill = GridBagConstraints.BOTH;
			gridBagConstraints48.gridheight = 1;
			gridBagConstraints48.gridwidth = 4;
			gridBagConstraints48.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints48.gridy = 8;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridwidth = 4;
			gridBagConstraints41.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints41.gridy = 7;
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.gridx = 0;
			gridBagConstraints36.gridwidth = 4;
			gridBagConstraints36.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints36.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints36.gridy = 6;
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 0;
			gridBagConstraints28.gridwidth = 4;
			gridBagConstraints28.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints28.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints28.gridy = 5;
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 0;
			gridBagConstraints27.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints27.gridwidth = 4;
			gridBagConstraints27.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints27.gridy = 4;
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 1;
			gridBagConstraints26.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints26.insets = new Insets(0, 0, 0, 25);
			gridBagConstraints26.gridy = 3;
			serverMaximum = new JLabel();
			serverMaximum.setFont(new Font("Verdana", Font.PLAIN, 12));
			serverMaximum.setText("? MB");
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 0;
			gridBagConstraints25.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints25.insets = new Insets(0, 3, 0, 3);
			gridBagConstraints25.gridy = 3;
			serverMaximumLabel = new JLabel();
			serverMaximumLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			serverMaximumLabel.setText("Maximum Memory:");
			serverMaximumLabel.setHorizontalAlignment(JLabel.RIGHT);
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 3;
			gridBagConstraints24.gridwidth = 2;
			gridBagConstraints24.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints24.insets = new Insets(0, 0, 0, 3);
			gridBagConstraints24.gridy = 2;
			worldEntites = new JLabel();
			worldEntites.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldEntites.setText("?");
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 2;
			gridBagConstraints23.insets = new Insets(0, 0, 0, 3);
			gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.anchor = GridBagConstraints.CENTER;
			gridBagConstraints23.gridy = 2;
			worldEntitiesLabel = new JLabel();
			worldEntitiesLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			worldEntitiesLabel.setText("Entities:");
			worldEntitiesLabel.setHorizontalAlignment(JLabel.RIGHT);
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 1;
			gridBagConstraints22.insets = new Insets(0, 0, 0, 25);
			gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.gridy = 2;
			serverAvailable = new JLabel();
			serverAvailable.setFont(new Font("Verdana", Font.PLAIN, 12));
			serverAvailable.setText("? MB (?%)");
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.insets = new Insets(0, 3, 0, 3);
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 2;
			serverAvailableLabel = new JLabel();
			serverAvailableLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			serverAvailableLabel.setText("Available Memory:");
			serverAvailableLabel.setHorizontalAlignment(JLabel.RIGHT);
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 3;
			gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.insets = new Insets(0, 0, 0, 3);
			gridBagConstraints20.gridy = 1;
			serverPlayer = new JLabel();
			serverPlayer.setFont(new Font("Verdana", Font.PLAIN, 12));
			serverPlayer.setText("?");
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 2;
			gridBagConstraints19.insets = new Insets(0, 0, 0, 3);
			gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints19.gridy = 1;
			serverPeopleLabel = new JLabel();
			serverPeopleLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			serverPeopleLabel.setText("Players Online:");
			serverPeopleLabel.setHorizontalAlignment(JLabel.RIGHT);
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 2;
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.ipadx = 3;
			gridBagConstraints18.ipady = 3;
			gridBagConstraints18.insets = new Insets(3, 0, 0, 3);
			gridBagConstraints18.gridy = 0;
			worldsLabel = new JLabel();
			worldsLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
			worldsLabel.setText("World Statistics");
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.insets = new Insets(0, 0, 0, 25);
			gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 1;
			serverAllocated = new JLabel();
			serverAllocated.setText("? MB (?%)");
			serverAllocated.setFont(new Font("Verdana", Font.PLAIN, 12));
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.insets = new Insets(0, 3, 0, 3);
			gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 1;
			serverAllocatedLabel = new JLabel();
			serverAllocatedLabel.setText("Allocated Memory:");
			serverAllocatedLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			serverAllocatedLabel.setHorizontalAlignment(JLabel.RIGHT);
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.ipadx = 3;
			gridBagConstraints15.ipady = 3;
			gridBagConstraints15.gridwidth = 2;
			gridBagConstraints15.insets = new Insets(3, 3, 0, 0);
			gridBagConstraints15.gridy = 0;
			serverLabel = new JLabel();
			serverLabel.setText("Server");
			serverLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
			serverPanel = new JPanel();
			serverPanel.setLayout(new GridBagLayout());
			serverPanel.add(serverLabel, gridBagConstraints15);
			serverPanel.add(serverAllocatedLabel, gridBagConstraints16);
			serverPanel.add(serverAllocated, gridBagConstraints17);
			serverPanel.add(worldsLabel, gridBagConstraints18);
			serverPanel.add(serverPeopleLabel, gridBagConstraints19);
			serverPanel.add(serverPlayer, gridBagConstraints20);
			serverPanel.add(serverAvailableLabel, gridBagConstraints21);
			serverPanel.add(serverAvailable, gridBagConstraints22);
			serverPanel.add(worldEntitiesLabel, gridBagConstraints23);
			serverPanel.add(worldEntites, gridBagConstraints24);
			serverPanel.add(serverMaximumLabel, gridBagConstraints25);
			serverPanel.add(serverMaximum, gridBagConstraints26);
			serverPanel.add(getServerReloadAction(), gridBagConstraints27);
			serverPanel.add(getServerStopAction(), gridBagConstraints28);
			serverPanel.add(getServerAnnounceAction(), gridBagConstraints36);
			serverPanel.add(getServerMonitorOptionsAction(), gridBagConstraints41);
			serverPanel.add(getOpenMinecraftServerFolderButton(), gridBagConstraints48);
		}
		return serverPanel;
	}
	/**
	 * This method initializes serverReloadAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getServerReloadAction() {
		if (serverReloadAction == null) {
			serverReloadAction = new JButton();
			serverReloadAction.setText("Reload Server");
			serverReloadAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					ColoredMessageSender.announceMessage("&7Reloading Server...");
					MonitorPlugin.getInstance().getServer().reload();
					ColoredMessageSender.announceMessage("&7Reloaded Server...");
				}
			});
		}
		return serverReloadAction;
	}
	/**
	 * This method initializes serverStopAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getServerStopAction() {
		if (serverStopAction == null) {
			serverStopAction = new JButton();
			serverStopAction.setText("Stop Server");
			serverStopAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					ColoredMessageSender.announceMessage("&7Stopping Server...");
					MonitorPlugin.getInstance().getPM().disablePlugins();
					MonitorPlugin.getInstance().getServer().savePlayers();
					for(World world : MonitorPlugin.getInstance().getServer().getWorlds())
						world.save();
					System.exit(0);
				}
			});
		}
		return serverStopAction;
	}
	/**
	 * This method initializes serverAnnounceAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getServerAnnounceAction() {
		if (serverAnnounceAction == null) {
			serverAnnounceAction = new JButton();
			serverAnnounceAction.setText("Announce Message");
			serverAnnounceAction.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					String input = (String) JOptionPane.showInputDialog(window, "Input a message.", "Input Message", JOptionPane.QUESTION_MESSAGE, IconsList.infoIcon, null, "");
					if(input != null && !input.trim().isEmpty())
						ColoredMessageSender.announceMessage(input);
				}
			});
		}
		return serverAnnounceAction;
	}
	/**
	 * This method initializes consolePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getConsolePanel() {
		if (consolePanel == null) {
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints37.gridx = 0;
			gridBagConstraints37.gridy = 0;
			gridBagConstraints37.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints37.weightx = 1.0;
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.fill = GridBagConstraints.BOTH;
			gridBagConstraints38.gridy = 1;
			gridBagConstraints38.weightx = 1.0;
			gridBagConstraints38.weighty = 1.0;
			gridBagConstraints38.insets = new Insets(0, 3, 3, 3);
			gridBagConstraints38.gridx = 0;
			consolePanel = new JPanel();
			consolePanel.setLayout(new GridBagLayout());
			consolePanel.add(getConsoleInputField(), gridBagConstraints37);
			consolePanel.add(getConsoleScrollPanel(), gridBagConstraints38);
		}
		return consolePanel;
	}
	private int index = -1;
	private List<String> commandList = new ArrayList<String>();  //  @jve:decl-index=0:
	private JPanel mainPanel = null;
	private JLabel mainLabel = null;
	private JDialog optionsDialog = null;
	private JScrollPane optionsScrollPane = null;
	private JList optionList = null;
	private DefaultListModel optionsListModel = null;
	private JButton serverMonitorOptionsAction = null;
	private DataMapViewer memoryMapViewer = null;
	private DataMapViewer serverStatsViewer = null;
	private DataMapViewer entityStatsViewer = null;
	private JTabbedPane graphPane = null;
	private JPanel serverGraphPanel = null;
	private JPanel playerGraphsPanel = null;
	private DataMapViewer playerStatsViewer = null;
	private JPanel worldGraphPanel = null;
	private DataMapViewer hostleEntityStatsViewer = null;
	private DataMapViewer friendlyEntityStatsViewer = null;
	private JButton openMinecraftServerFolderButton = null;
	/**
	 * This method initializes consoleInputField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getConsoleInputField() {
		if (consoleInputField == null) {
			consoleInputField = new JTextField();
			consoleInputField.setFont(new Font("Verdana", Font.PLAIN, 12));
			consoleInputField.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_UP)
					{
						index++;
						if(index >= commandList.size())
							index--;
						if(index == -1)
							consoleInputField.setText("");
						else
							consoleInputField.setText(commandList.get(index));
					}
					else if(e.getKeyCode() == KeyEvent.VK_DOWN)
					{
						index--;
						if(index < -1)
							index++;
						if(index == -1)
							consoleInputField.setText("");
						else
							consoleInputField.setText(commandList.get(index));
					}
					else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						index = -1;
						consoleInputField.setText("");
					}
				}
			});
			consoleInputField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(!consoleInputField.getText().trim().isEmpty())
					{
						MinecraftServer server = ((CraftServer) MonitorPlugin.getInstance().getServer()).getHandle().server;
						WindowInterface.addLog(new Event("Console command: " + consoleInputField.getText().trim(), EventType.USER));
						server.issueCommand(consoleInputField.getText().trim(), server);
						commandList.add(0, consoleInputField.getText().trim());
						index = -1;
						consoleInputField.setText("");
					}
				}
			});
		}
		return consoleInputField;
	}
	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.gridx = 0;
			gridBagConstraints40.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints40.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints40.ipadx = 3;
			gridBagConstraints40.ipady = 3;
			gridBagConstraints40.gridy = 0;
			mainLabel = new JLabel();
			mainLabel.setText("Minecraft Server v1.6.6");
			mainLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
			mainLabel.setHorizontalAlignment(JLabel.CENTER);
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.fill = GridBagConstraints.BOTH;
			gridBagConstraints39.weighty = 1.0;
			gridBagConstraints39.gridy = 1;
			gridBagConstraints39.weightx = 1.0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getMainPane(), gridBagConstraints39);
			mainPanel.add(mainLabel, gridBagConstraints40);
		}
		return mainPanel;
	}
	/**
	 * This method initializes optionsDialog	
	 * 	
	 * @return javax.swing.JDialog	
	 */
	private JDialog getOptionsDialog() {
		if (optionsDialog == null) {
			optionsDialog = new JDialog(getJFrame());
			optionsDialog.setTitle("Options");
			optionsDialog.setContentPane(getOptionsScrollPane());
			optionsDialog.setSize(new Dimension(300, 400));
		}
		return optionsDialog;
	}
	/**
	 * This method initializes optionsScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getOptionsScrollPane() {
		if (optionsScrollPane == null) {
			optionsScrollPane = new JScrollPane();
			optionsScrollPane.setViewportView(getOptionList());
		}
		return optionsScrollPane;
	}
	/**
	 * This method initializes optionList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getOptionList() {
		if (optionList == null) {
			optionList = new JList();
			optionList.setCellRenderer(new CheckListCellRenderer());
			optionList.setModel(getOptionsListModel());
			optionList.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
			        int index = getOptionList().locationToIndex(e.getPoint());
			        if(index != -1)
			        {
			        	CheckableOption option = (CheckableOption)getOptionList().getModel().getElementAt(index);
			        	option.setSet(!option.isSet());
				        getOptionList().repaint(getOptionList().getCellBounds(index, index));
				        WindowInterface.updatePlayerLookup();
			        }
				}
			});
		}
		return optionList;
	}
	/**
	 * This method initializes optionsListModel	
	 * 	
	 * @return javax.swing.DefaultListModel	
	 */
	public DefaultListModel getOptionsListModel() {
		if (optionsListModel == null) {
			optionsListModel = new DefaultListModel();
		}
		return optionsListModel;
	}
	/**
	 * This method initializes serverMonitorOptionsAction	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getServerMonitorOptionsAction() {
		if (serverMonitorOptionsAction == null) {
			serverMonitorOptionsAction = new JButton();
			serverMonitorOptionsAction.setText("Monitor Options");
			serverMonitorOptionsAction
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							getOptionsDialog().setVisible(true);
						}
					});
		}
		return serverMonitorOptionsAction;
	}
	/**
	 * This method initializes memoryMapViewer	
	 * 	
	 * @return com.hsun324.monitor.swing.DataMapViewer	
	 */
	private DataMapViewer getMemoryMapViewer() {
		if (memoryMapViewer == null) {
			memoryMapViewer = new DataMapViewer();
			memoryMapViewer.setName("Memory Usage");
		}
		return memoryMapViewer;
	}
	/**
	 * This method initializes serverStatsViewer	
	 * 	
	 * @return com.hsun324.monitor.swing.DataMapViewer	
	 */
	private DataMapViewer getServerStatsViewer() {
		if (serverStatsViewer == null) {
			serverStatsViewer = new DataMapViewer();
			serverStatsViewer.setName("Server Statistics");
			serverStatsViewer.setInAuto(true);
		}
		return serverStatsViewer;
	}
	/**
	 * This method initializes entityStatsViewer	
	 * 	
	 * @return com.hsun324.monitor.swing.DataMapViewer	
	 */
	private DataMapViewer getEntityStatsViewer() {
		if (entityStatsViewer == null) {
			entityStatsViewer = new DataMapViewer();
			entityStatsViewer.setName("Entity Statistics");
			entityStatsViewer.setInAuto(true);
		}
		return entityStatsViewer;
	}
	/**
	 * This method initializes graphPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getGraphPane() {
		if (graphPane == null) {
			graphPane = new JTabbedPane();
			graphPane.setFont(new Font("Verdana", Font.PLAIN, 14));
			graphPane.addTab("Server", IconsList.serverIcon, getServerGraphPanel(), null);
			graphPane.addTab("Player", IconsList.userIcon, getPlayerGraphsPanel(), null);
			graphPane.addTab("World", IconsList.worldIcon, getWorldGraphPanel(), null);
		}
		return graphPane;
	}
	/**
	 * This method initializes serverGraphPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getServerGraphPanel() {
		if (serverGraphPanel == null) {
			GridBagConstraints gridBagConstraints43 = new GridBagConstraints();
			gridBagConstraints43.gridx = 0;
			gridBagConstraints43.weightx = 1.0;
			gridBagConstraints43.weighty = 1.0;
			gridBagConstraints43.fill = GridBagConstraints.BOTH;
			gridBagConstraints43.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints43.gridy = 1;
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.gridx = 0;
			gridBagConstraints42.weightx = 1.0;
			gridBagConstraints42.weighty = 1.0;
			gridBagConstraints42.fill = GridBagConstraints.BOTH;
			gridBagConstraints42.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints42.gridy = 0;
			serverGraphPanel = new JPanel();
			serverGraphPanel.setLayout(new GridBagLayout());
			serverGraphPanel.add(getMemoryMapViewer(), gridBagConstraints42);
			serverGraphPanel.add(getServerStatsViewer(), gridBagConstraints43);
		}
		return serverGraphPanel;
	}
	/**
	 * This method initializes playerGraphsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPlayerGraphsPanel() {
		if (playerGraphsPanel == null) {
			GridBagConstraints gridBagConstraints45 = new GridBagConstraints();
			gridBagConstraints45.gridx = 0;
			gridBagConstraints45.fill = GridBagConstraints.BOTH;
			gridBagConstraints45.weightx = 1.0;
			gridBagConstraints45.weighty = 1.0;
			gridBagConstraints45.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints45.gridy = 0;
			playerGraphsPanel = new JPanel();
			playerGraphsPanel.setLayout(new GridBagLayout());
			playerGraphsPanel.add(getPlayerStatsViewer(), gridBagConstraints45);
		}
		return playerGraphsPanel;
	}
	/**
	 * This method initializes playerStatsViewer	
	 * 	
	 * @return com.hsun324.monitor.swing.DataMapViewer	
	 */
	private DataMapViewer getPlayerStatsViewer() {
		if (playerStatsViewer == null) {
			playerStatsViewer = new DataMapViewer();
			playerStatsViewer.setName("Player Activity");
			playerStatsViewer.setInAuto(true);
		}
		return playerStatsViewer;
	}
	/**
	 * This method initializes worldGraphPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWorldGraphPanel() {
		if (worldGraphPanel == null) {
			GridBagConstraints gridBagConstraints47 = new GridBagConstraints();
			gridBagConstraints47.gridx = 0;
			gridBagConstraints47.weightx = 1.0;
			gridBagConstraints47.weighty = 1.0;
			gridBagConstraints47.fill = GridBagConstraints.BOTH;
			gridBagConstraints47.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints47.gridy = 2;
			GridBagConstraints gridBagConstraints46 = new GridBagConstraints();
			gridBagConstraints46.gridx = 0;
			gridBagConstraints46.weightx = 1.0;
			gridBagConstraints46.weighty = 1.0;
			gridBagConstraints46.fill = GridBagConstraints.BOTH;
			gridBagConstraints46.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints46.gridy = 1;
			GridBagConstraints gridBagConstraints44 = new GridBagConstraints();
			gridBagConstraints44.gridx = 0;
			gridBagConstraints44.weightx = 1.0;
			gridBagConstraints44.weighty = 1.0;
			gridBagConstraints44.fill = GridBagConstraints.BOTH;
			gridBagConstraints44.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints44.gridy = 0;
			worldGraphPanel = new JPanel();
			worldGraphPanel.setLayout(new GridBagLayout());
			worldGraphPanel.add(getEntityStatsViewer(), gridBagConstraints44);
			worldGraphPanel.add(getHostleEntityStatsViewer(), gridBagConstraints46);
			worldGraphPanel.add(getFriendlyEntityStatsViewer(), gridBagConstraints47);
		}
		return worldGraphPanel;
	}
	/**
	 * This method initializes hostleEntityStatsViewer	
	 * 	
	 * @return com.hsun324.monitor.swing.DataMapViewer	
	 */
	private DataMapViewer getHostleEntityStatsViewer() {
		if (hostleEntityStatsViewer == null) {
			hostleEntityStatsViewer = new DataMapViewer();
			hostleEntityStatsViewer.setName("Hostle Entity Statistics");
			hostleEntityStatsViewer.setInAuto(true);
		}
		return hostleEntityStatsViewer;
	}
	/**
	 * This method initializes friendlyEntityStatsViewer	
	 * 	
	 * @return com.hsun324.monitor.swing.DataMapViewer	
	 */
	private DataMapViewer getFriendlyEntityStatsViewer() {
		if (friendlyEntityStatsViewer == null) {
			friendlyEntityStatsViewer = new DataMapViewer();
			friendlyEntityStatsViewer.setName("Friendly Entity Statistics");
			friendlyEntityStatsViewer.setInAuto(true);
		}
		return friendlyEntityStatsViewer;
	}
	/**
	 * This method initializes openMinecraftServerFolderButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOpenMinecraftServerFolderButton() {
		if (openMinecraftServerFolderButton == null) {
			openMinecraftServerFolderButton = new JButton();
			openMinecraftServerFolderButton.setText("Open Server Folder");
			openMinecraftServerFolderButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) 
						{
							File dir = new File(System.getProperty("user.dir"));
							if(Desktop.isDesktopSupported())
							{
								try
								{
									Desktop.getDesktop().open(dir);
								}
								catch(Exception ex)
								{
									JOptionPane.showMessageDialog(getJFrame(), ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
								}
							}
							else
								JOptionPane.showMessageDialog(getJFrame(), "Your system is not supported!", "System Error", JOptionPane.ERROR_MESSAGE);
						}
					});
		}
		return openMinecraftServerFolderButton;
	}
}
