package com.hsun324.monitor.config;

import java.util.HashMap;
import java.util.Map;

import com.hsun324.simplebukkit.config.ConfigurationWrapper;

public class BooleanOption
{
	protected static final Map<String, BooleanOption> optionsMap = new HashMap<String, BooleanOption>();
	
	protected String name = "";
	protected String desc = "";
	protected ConfigurationWrapper config = null;
	protected boolean isSet = false;
	
	public BooleanOption(String name, String desc)
	{
		this(name, desc, false);
	}
	
	public BooleanOption(String name, String desc, boolean isSet)
	{
		this.name = name;
		this.desc = desc;
		this.isSet = isSet;
		BooleanOption.optionsMap.put(name, this);
	}

	public String getName()
	{
		return name;
	}
	
	public BooleanOption setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public String getDescription()
	{
		return desc;
	}
	
	public BooleanOption setDescription(String desc)
	{
		this.desc = desc;
		return this;
	}
	
	public boolean isSet()
	{
		return isSet;
	}
	public BooleanOption setSet(boolean isSet)
	{
		this.isSet = isSet;
		if(config != null)
		{
			config.setBoolean(name, isSet);
			config.save();
		}
		return this;
	}
	public BooleanOption setSet(ConfigurationWrapper config)
	{
		this.isSet = config.getBoolean(name, isSet);
		this.config = config;
		return this;
	}
	public BooleanOption setSet(ConfigurationWrapper config, boolean isSet)
	{
		this.isSet = config.getBoolean(name, isSet);
		this.config = config;
		return this;
	}
	
	public static BooleanOption getOption(String name)
	{
		return optionsMap.get(name);
	}
}
