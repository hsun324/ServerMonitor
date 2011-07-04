package com.hsun324.monitor.events;

import java.awt.Color;

public class Event
{
	private final String message;
	private final EventType type;
	private final org.bukkit.event.Event.Type nativeType;
	
	public Event(String message, EventType type)
	{
		this(message, type, null);
	}
	public Event(String message, EventType type, org.bukkit.event.Event.Type nativeType)
	{
		this.message = message;
		this.type = type;
		this.nativeType = nativeType;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public EventType getType()
	{
		return type;
	}

	public org.bukkit.event.Event.Type getNativeType()
	{
		return nativeType;
	}
	@Override
	public String toString()
	{
		return message;
	}
	
	private final int[] colorMapping = new int[]{0, 0x000077, 0x007700, 0x007777, 0x770000, 0x770077, 0x997700, 0x777777, 0x111111, 0x222299, 0x229922, 0x229999, 0x992222, 0x992299, 0x999922, 0};
	
	public String getFormattedMessage(boolean isSelected)
	{
		String message = this.message.replace("<", "&lt;").replace(">", "&gt;");
		boolean changed = false;
		if(!isSelected)
		{
			String localMessage = message;
			for(int i = 0; i < colorMapping.length; i++)
				localMessage = localMessage.replaceAll("[&^\u00A7]" + Integer.toHexString(i), "<font color=\""+getColorInHex(colorMapping[i])+"\">");
			if(!localMessage.equals(message))
				changed = true;
			message = localMessage;
		}
		else
			message = message.replaceAll("[&^\u00A7][a-fA-F0-9]", "");
		if(changed)
			message += "</font>";
		return "<html><font color=\"" + getColorInHex(isSelected?type.getDownTextColor():type.getUpTextColor()) + "\">" + message;
	}
	private String getColorInHex(Color color)
	{
		return getColorInHex(color.getRGB());
	}
	private String getColorInHex(int color)
	{
		return "#" + String.format("%06x", color & 0x00FFFFFF);
	}
}
