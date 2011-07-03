package com.hsun324.monitor.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import com.hsun324.monitor.util.LimitedLinkedHashMap;
import com.hsun324.monitor.util.LineInfo;

public class DataMapViewer extends JComponent
{
	private static final long serialVersionUID = -7646879692600486118L;
	protected static double pixelSecondsValue = .25;
	protected Map<LineInfo, Map<Long, Double>> provider = new LinkedHashMap<LineInfo, Map<Long, Double>>();
	protected Map<LineInfo, Double> sortedProvider = new LinkedHashMap<LineInfo, Double>();
	protected Map<Color, LineInfo> keys = new HashMap<Color, LineInfo>();
	protected int bottomBound = 0;
	protected int topBound = 100;
	protected boolean automode = false;
	protected Color textBackgroundColor = new Color(255, 255, 255, 204);
	protected Map<Color, Color> textColors = new HashMap<Color, Color>();
	protected Color backgroundLineColor = new Color(0, 128, 64);
	protected String name = "";
	protected Font font = new Font("Verdana", Font.PLAIN, 10);
	protected Border graphAreaBorder = BorderFactory.createLoweredBevelBorder();
	protected BufferedImage image;
	private int lastWidth = 0;
	private int lastHeight = 0;
	private int topMul = 1;
	private int bottomMul = 1;
	private int topBoundN = 1;
	private int bottomBoundN = 0;
	private boolean boundNSet = false;
	private long startPaintTime = new Date().getTime();
	private long now = startPaintTime;
	private int phase = 0;
	
	public DataMapViewer()
	{
		super();
	}
	
	public int getBottomBound()
	{
		return bottomBound;
	}
	public int getTopBound()
	{
		return topBound;
	}
	public String getName()
	{
		return name;
	}
	public void setBottomBound(int bottomBound)
	{
		if(bottomBound >= topBound)
			this.bottomBound = topBound - 1;
		this.bottomBound = bottomBound;
	}
	public void setTopBound(int topBound)
	{
		if(topBound <= bottomBound)
			this.topBound = bottomBound + 1;
		this.topBound = topBound;
	}
	public void setName(String name)
	{
		if(name == null)
			this.name = "";
		else
			this.name = name.trim();
	}
	public boolean isInAuto()
	{
		return automode;
	}
	public void setInAuto(boolean auto)
	{
		automode = auto;
	}
	public synchronized void addValue(Color color, double value)
	{
		synchronized(this)
		{
			if(!automode && value > topBound)
				value = topBound;
			if(!automode && value < bottomBound)
				value = bottomBound;
			
			LineInfo info = new LineInfo(color, value);
			
			if(provider.get(info) == null)
			{
				provider.put(info, new LimitedLinkedHashMap<Long, Double>());
				keys.put(color, info);
			}
			provider.get(info).put(new Date().getTime(), value);
			sortedProvider.put(info, value);
			keys.get(color).setLastValue(value);
			sortedProvider = sort(sortedProvider);
		}
	}
	public <K, V> Map<K, V> sort(Map<K, V> map)
	{
		LinkedList<Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Entry<K, V>>(){
			@SuppressWarnings("unchecked")
			public int compare(Entry<K, V> entry1, Entry<K, V> entry2)
			{
				return ((Comparable<K>)entry1.getKey()).compareTo(entry2.getKey());
			}
		});
		
		LinkedHashMap<K, V> result = new LinkedHashMap<K, V>();
		for(Entry<K, V> entry : list)
			result.put(entry.getKey(), entry.getValue());
		return result;
	}
	
	@Override
	public synchronized void paint(Graphics graphicsComponent)
	{
		synchronized(this)
		{
			if(!this.isVisible())
				return;
			now = new Date().getTime();
			phase = 9 + ((int) (((now - startPaintTime) / (pixelSecondsValue * 1000)) % 10) * -1);
			if(automode)
			{
				if(topBoundN == bottomBoundN)
					topBoundN++;
				topBound = topBoundN;
				bottomBound = bottomBoundN;
				topBoundN = 1;
				bottomBoundN = 0;
				boundNSet = false;
			}
			int height = this.getHeight();
			int width = this.getWidth();
			
			if(width < 0 || height < 0)
				return;
			
			if(image == null || width != lastWidth || height != lastHeight)
			{
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				lastWidth = width;
				lastHeight = height;
			}
			
			Graphics2D graphics = image.createGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			height = this.getHeight();
			width = this.getWidth() - 34;
			
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0, 0, width, height);
			graphics.setColor(SystemColor.control);
			graphics.fillRect(width, 0, width + 34, height);
	
			height = this.getHeight() - 4;
			width = this.getWidth() - 38;
			
			graphics.setColor(backgroundLineColor);
			drawBackgroundGrid(graphics, 2, 2, width, height);
			
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			drawGraph(graphics, 0, 0, width, height);
			drawName(graphics, 4, 4);
	
			height = this.getHeight();
			width = this.getWidth();
	
			graphics.setColor(Color.BLACK);
			graphics.drawString(Integer.toString(topBound), width - 30, graphics.getFontMetrics().getHeight() - 2);
			graphics.drawString(Integer.toString(bottomBound), width - 30, height + graphics.getFontMetrics().getHeight() - 19);
			
			height = this.getHeight();
			width = this.getWidth() - 34;
			
			((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			
			graphAreaBorder.paintBorder(this, graphics, 0, 0, width, height);
			
			graphicsComponent.drawImage(image, 0, 0, width + 34, height, 0, 0, width + 34, height, this);
			
			graphicsComponent.dispose();
			graphics.dispose();
			topMul = 1;
			bottomMul = 1;
		}
	}
	
	private synchronized void drawGraph(Graphics2D graphics, int x, int y, int width, int height)
	{
		for(Entry<LineInfo, Map<Long, Double>> providerEntry : provider.entrySet())
		{
			graphics.setColor(providerEntry.getKey().getColor());
			int lastx = 0;
			int lasty = 0;
			int lastv = 0;
			int lowv = 0;
			int highv = 0;
			boolean setLast = false;
			for(Entry<Long, Double> entry : providerEntry.getValue().entrySet())
			{
				double time = now - entry.getKey();
				if(time < pixelSecondsValue * 1000 * (width - 2))
				{
					int ypos = (int) (height - (double)(entry.getValue() - bottomBound) / (topBound - bottomBound) * height + 2);
					int xpos = (int) (width - time / (pixelSecondsValue * 1000) + 1);
					if(ypos == height + 2)
						ypos--;
					if(setLast)
					{
						graphics.drawLine(xpos, ypos - 1, lastx, lasty - 1);
						graphics.drawLine(xpos, ypos, lastx, lasty);
						graphics.drawLine(xpos, ypos + 1, lastx, lasty + 1);
					}
					else
						graphics.fillRect(xpos - 1, ypos, 1, 1);
					
					lastx = xpos;
					lasty = ypos;
					lastv = (int) Math.round(entry.getValue());
					if(setLast)
					{
						lowv = Math.min(lowv, lastv);
						highv = Math.max(highv, lastv);
					}
					else
					{
						lowv = lastv;
						highv = lastv;
					}
					setLast = true;
				}
			}
			
			if(setLast)
			{
				if(boundNSet)
				{
					topBoundN = Math.max(topBoundN, highv);
					bottomBoundN = Math.min(bottomBoundN, lowv);
				}
				else
				{
					topBoundN = highv;
					bottomBoundN = lowv;
					boundNSet = true;
				}
			}
			
			graphics.drawLine(lastx, lasty - 1, x + width + 1, lasty - 1);
			graphics.drawLine(lastx, lasty, x + width + 1, lasty);
			graphics.drawLine(lastx, lasty + 1, x + width + 1, lasty + 1);
		}
		for(Entry<LineInfo, Double> sortedEntry : sortedProvider.entrySet())
		{
			Color currentTextColor = textColors.get(sortedEntry.getKey().getColor());
			if(currentTextColor == null)
			{
				Color currentColor = sortedEntry.getKey().getColor();
				currentTextColor = new Color(currentColor.getRed() / 2, currentColor.getGreen() / 2, currentColor.getBlue() / 2);
				textColors.put(currentColor, currentTextColor);
			}
			graphics.setFont(font);
			graphics.setColor(currentTextColor);
			
			int lastv = (int) Math.round(sortedEntry.getValue());
			int ypos = (int) (height - (double)(sortedEntry.getValue() - bottomBound) / (topBound - bottomBound) * height + 2);
			
			if(lastv >= (double)topBound - ((double)(topBound - bottomBound) * (0.05d * topMul) - (8d / height)))
			{
				graphics.drawString(Integer.toString(lastv), getWidth() - 30, graphics.getFontMetrics().getHeight() - 2 + 12 * topMul);
				topMul++;
			}
			else if(lastv <= (double)bottomBound + ((double)(topBound - bottomBound) * (0.05d * bottomMul) + (8d / height)))
			{
				graphics.drawString(Integer.toString(lastv), getWidth() - 30, height + graphics.getFontMetrics().getHeight() - 15 - 12 * bottomMul);
				bottomMul++;
			}
			else
				graphics.drawString(Integer.toString(lastv), getWidth() - 30, (int)ypos + graphics.getFontMetrics().getHeight() - 10);
		}
	}

	private void drawBackgroundGrid(Graphics graphics, int x, int y, int width, int height)
	{
		for(int i = 0; i < width; i++)
			if(i % 10 == phase)
				graphics.drawLine(i + x, y, i + x, height + y - 1);
		int lastPercent = -1;
		for(int i = 0; i < height; i++)
		{
			int percent = (int)((double)i / (double)(height - 1) * 10d);
			if(lastPercent < percent)
				graphics.drawLine(x, i + y, width + x - 1, i + y);
			lastPercent = percent;
		}
	}
	
	private void drawName(Graphics graphics, int x, int y)
	{
		graphics.setFont(font);
		if(!name.isEmpty())
		{
			graphics.setColor(textBackgroundColor);
			char[] namec = name.toCharArray();
			int fbwidth = graphics.getFontMetrics().charsWidth(namec, 0, namec.length) + 4;
			int fbheight = graphics.getFontMetrics().getHeight();
			graphics.fillRect(x, y, fbwidth, fbheight + 1);
			graphics.drawRect(x, y, fbwidth - 1, fbheight);
			graphics.setColor(Color.BLACK);
			graphics.drawString(name, x + 2, y - 3 + graphics.getFontMetrics().getHeight());
		}
	}
}
