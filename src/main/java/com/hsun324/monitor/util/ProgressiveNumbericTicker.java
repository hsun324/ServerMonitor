package com.hsun324.monitor.util;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class ProgressiveNumbericTicker
{
	public static final ProgressiveNumbericTicker playerActivityTicker = new ProgressiveNumbericTicker(2000);
	public static final ProgressiveNumbericTicker blockBreakTicker = new ProgressiveNumbericTicker(2000);
	public static final ProgressiveNumbericTicker blockPlaceTicker = new ProgressiveNumbericTicker(2000);
	public static final ProgressiveNumbericTicker playerInteractTicker = new ProgressiveNumbericTicker(2000);
	public static final ProgressiveNumbericTicker talkTicker = new ProgressiveNumbericTicker(2000);
	private final int phaseMilli;
	private final Map<Long, Integer> ticks = LimitedLinkedHashMap.createLimitedLinkedHashMap(5000);
	
	public ProgressiveNumbericTicker(int phaseMilliseconds)
	{
		this.phaseMilli = phaseMilliseconds;
	}
	
	public void tick()
	{
		tick(1);
	}
	public void tick(int i)
	{
		long now = new Date().getTime();
		ticks.put(now, i);
	}
	
	public int total()
	{
		long now = new Date().getTime();
		int res = 0;
		for(Entry<Long, Integer> entry : ticks.entrySet())
			if(now - entry.getKey() <= phaseMilli)
			res += entry.getValue();
		return res;
	}
}
