package com.tim.plainsailing;

import android.graphics.Rect;

public class Util {
	static boolean collidesWith(Rect a, Rect b){
		return !(b.left > a.right	|| b.right  < a.left
		      || b.top  > a.bottom  || b.bottom < a.top );
	}
}
