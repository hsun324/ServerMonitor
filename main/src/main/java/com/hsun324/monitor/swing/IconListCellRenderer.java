package com.hsun324.monitor.swing;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.hsun324.monitor.events.Event;
import com.hsun324.monitor.icons.IconsList;

public class IconListCellRenderer extends DefaultListCellRenderer
{
	private static final long serialVersionUID = -3048281252088506817L;
	private Font downFont = null;
	private Border noBorder = new EmptyBorder(1, 1, 1, 1);
	
	@Override
	public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		setComponentOrientation(list.getComponentOrientation());
		setEnabled(list.isEnabled());
		if(value instanceof Event)
		{
			Event event = (Event)value;
			setText(event.getFormattedMessage(isSelected));
			Icon icon = event.getType().getIcon();
			if(index > 0 && ((Event) list.getModel().getElementAt(index - 1)).getType().equals(event.getType()))
				icon = IconsList.clearIcon;
			if(icon != null)
				setIcon(icon);
			if(isSelected)
			{
				setBackground(event.getType().getBackgroundColor());
				if(downFont == null)
					downFont = new Font(list.getFont().getFamily(), Font.BOLD, list.getFont().getSize());
				setFont(downFont);
			}
			else
			{
				setBackground(event.getType().getUpBackgroundColor());
				setFont(list.getFont());
			}
			if(cellHasFocus)
				setBorder(event.getType().getBorder());
			else
				setBorder(noBorder);
		}
		return this;
	}
}
