package com.hsun324.monitor.swing;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import com.hsun324.monitor.events.Event;

public class IconListCellRenderer extends DefaultListCellRenderer
{
	private static final long serialVersionUID = -3048281252088506817L;
	
	@Override
	public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if(value instanceof Event)
		{
			Event event = (Event)value;
			label.setText(event.getFormattedMessage(isSelected));
			Icon icon = event.getType().getIcon();
			if(icon != null)
				label.setIcon(icon);
			if(isSelected)
			{
				label.setBackground(event.getType().getBackgroundColor());
				label.setFont(new Font(list.getFont().getFamily(), (list.getFont().isPlain()?Font.BOLD:list.getFont().getStyle() | Font.BOLD), list.getFont().getSize()));
			}
			else
				label.setBackground(event.getType().getUpBackgroundColor());
			if(cellHasFocus)
				label.setBorder(BorderFactory.createLineBorder(event.getType().getBorderColor()));
		}
		return label;
	}
}
