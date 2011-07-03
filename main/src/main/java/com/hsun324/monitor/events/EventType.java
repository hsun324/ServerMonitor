package com.hsun324.monitor.events;

import java.awt.Color;

import javax.swing.Icon;

import com.hsun324.monitor.icons.IconsList;

public enum EventType
{
	INFO(IconsList.infoIcon, new Color(0, 0, 204), new Color(102, 102, 255), new Color(230, 230, 255), new Color(0, 0, 102), new Color(255, 255, 255)),
	TEXT(IconsList.textIcon, new Color(100, 0, 128), new Color(200, 0, 255), new Color(240, 186, 255), new Color(102, 0, 130), new Color(255, 255, 255)),
	WARNING(IconsList.warningIcon, new Color(204, 204, 0), new Color(255, 255, 153), new Color(255, 255, 230), new Color(102, 102, 0), new Color(0, 0, 0)),
	ERROR(IconsList.errorIcon, new Color(204, 0, 0), new Color(255, 102, 102), new Color(255, 230, 230), new Color(102, 0, 0), new Color(0, 0, 0)),
	USER(IconsList.userIcon, new Color(0, 102, 153), new Color(51, 187, 255), new Color(204, 238, 255), new Color(0, 68, 102), new Color(255, 255, 255)),
	BUG(IconsList.bugIcon, new Color(0, 204, 0), new Color(153, 255, 153), new Color(230, 255, 230), new Color(0, 102, 0), new Color(0, 0, 0));
	
	private Icon icon;
	private Color borderColor;
	private Color backgroundColor;
	private Color upBackgroundColor;
	private Color upTextColor;
	private Color downTextColor;
	
	private EventType(Icon icon, Color borderColor, Color backgroundColor, Color upBackgroundColor, Color upTextColor, Color downTextColor)
	{
		this.icon = icon;
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
		this.upBackgroundColor = upBackgroundColor;
		this.upTextColor = upTextColor;
		this.downTextColor = downTextColor;
	}
	
	public Icon getIcon()
	{
		return icon;
	}
	
	public Color getBorderColor()
	{
		return borderColor;
	}
	
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}
	
	public Color getUpBackgroundColor()
	{
		return upBackgroundColor;
	}
	
	public Color getUpTextColor()
	{
		return upTextColor;
	}
	
	public Color getDownTextColor()
	{
		return downTextColor;
	}
}
