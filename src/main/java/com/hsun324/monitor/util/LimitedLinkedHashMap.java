package com.hsun324.monitor.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedLinkedHashMap<K, V> extends LinkedHashMap<K, V>
{
	private static final long serialVersionUID = -7349530867961919593L;
	private int MAX_ENTRIES = 2000;
	
	public static <K, V> LimitedLinkedHashMap<K, V> createLimitedLinkedHashMap(int maxEntries)
	{
		LimitedLinkedHashMap<K, V> map = new LimitedLinkedHashMap<K, V>();
		map.setMaxEntries(maxEntries);
		return map;
	}
	
	private void setMaxEntries(int maxEntries)
	{
		MAX_ENTRIES = maxEntries;
	}
	
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
    {
       return size() > MAX_ENTRIES;
    }
}
