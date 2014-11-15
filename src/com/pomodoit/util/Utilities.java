package com.pomodoit.util;

public class Utilities {

	public static boolean isFinishedTimer(int _m, int _s, int _ms)
	{
		if(_m == 0 && _s == 0 && _ms == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
