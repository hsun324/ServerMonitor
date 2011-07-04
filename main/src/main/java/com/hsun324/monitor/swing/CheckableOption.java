package com.hsun324.monitor.swing;

import com.hsun324.monitor.config.BooleanOption;

public class CheckableOption
{
    private BooleanOption booleanOption;
    
    public CheckableOption(BooleanOption booleanOption)
    {
		this.booleanOption = booleanOption;
    }
    
    public BooleanOption getBooleanOption()
    {
    	return booleanOption;
    }
    public boolean isSet()
    {
    	return booleanOption.isSet();
    }
    public void setSet(boolean isSet)
    {
    	booleanOption.setSet(isSet);
    }
    public String getText()
    {
    	return booleanOption.getDescription();
    }
    public void setText(String text)
    {
    	booleanOption.setDescription(text);
    }

    public String toString()
    {
    	return booleanOption.getName();
    }
}
