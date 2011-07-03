package com.hsun324.monitor.swing;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

public class CheckListCellRenderer extends JCheckBox implements ListCellRenderer
{
	private static final long serialVersionUID = 4873958148911971457L;
    public CheckListCellRenderer()
    {
		setBackground(UIManager.getColor("List.textBackground"));
		setForeground(UIManager.getColor("List.textForeground"));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus)
    {
    	if(value instanceof CheckableOption)
    	{
    		CheckableOption cho = (CheckableOption)value;
			setEnabled(list.isEnabled());
			setSelected(cho.isSet());
			setFont(list.getFont());
			setText(cho.getText());
			return this;
    	}
    	return null;
    }
}
