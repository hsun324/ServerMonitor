package com.hsun324.monitor.util;

import java.awt.Color;

public class LineInfo implements Comparable<LineInfo>
{
	private Color color;
	private double lastValue;
	public LineInfo(Color color, double lastValue)
	{
		this.color = color;
		this.lastValue = lastValue;
	}
	public Color getColor()
	{
		return color;
	}
	public void setColor(Color color)
	{
		this.color = color;
	}
	public double getLastValue()
	{
		return lastValue;
	}
	public void setLastValue(double lastValue)
	{
		this.lastValue = lastValue;
	}
	public int hashCode()
	{
		return color.getRGB();
	}
	public boolean equals(Object obj)
	{
		return obj instanceof LineInfo && this.color.getRGB() == ((LineInfo)obj).color.getRGB();
	}
	public int compareTo(LineInfo anotherInfo)
	{
		if(color.getRGB() == anotherInfo.color.getRGB())
			return 0;
		if(lastValue > anotherInfo.lastValue)
			return 1;
		return -1;
	}
}