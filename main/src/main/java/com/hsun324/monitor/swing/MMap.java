package com.hsun324.monitor.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class MMap extends Component
{
	private static final long serialVersionUID = -596677576528496469L;
	private World renderingWorld = null;
	
	public void setRenderingWorld(World world)
	{
		renderingWorld = world;
	}
	public World getRenderingWorld()
	{
		return renderingWorld;
	}

	@Override
	public void paint(Graphics graphics)
	{
		if(renderingWorld != null)
		{
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(Player player : renderingWorld.getPlayers())
			{
				minX = (player.getLocation().getBlockX() < minX ? player.getLocation().getBlockX() : minX);
				minY = (-player.getLocation().getBlockZ() < minY ? -player.getLocation().getBlockZ() : minY);
				maxX = (player.getLocation().getBlockX() > maxX ? player.getLocation().getBlockX() : maxX);
				maxY = (-player.getLocation().getBlockZ() > maxY ? -player.getLocation().getBlockZ() : maxY);
			}
			minX -= 4;
			minY -= 4;
			maxX += 50;
			maxY += 4;
			
			int sizeX = maxX - minX;
			int sizeY = maxY - minY;
			
			if(sizeX > 0 && sizeY > 0)
			{
				Image bufferImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
				Graphics buffer = bufferImage.getGraphics();
				
				buffer.setColor(Color.BLACK);
				buffer.fillRect(minX, minY, maxX, maxY);
				
				for(Player player : renderingWorld.getPlayers())
				{
					int playerProjX = player.getLocation().getBlockX();
					int playerProjY = -player.getLocation().getBlockZ();
					buffer.setColor(Color.BLACK);
					buffer.fillArc(playerProjX - 1, playerProjY - 1, 1, 1, 0, 360);
					buffer.setColor(Color.WHITE);
					buffer.drawArc(playerProjX - 1, playerProjY - 1, 1, 1, 0, 360);
				}
				
				graphics.drawImage(bufferImage, 0, 0, getWidth(), getHeight(), minX, minY, maxX, maxY, getParent());
			}
			else
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
		}
	}
}
