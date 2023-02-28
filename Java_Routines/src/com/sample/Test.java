package com.sample;

public class Test {
	
	public static Long add(Long a,Long b) {
		Long c=a+b;
		return c;
	}

	public static Long sub(Long a,Long b) {
		Long c;
		if(a>b) {
			
			c=a-b;
			
		}else {
			
			c=b-a;
		}
		
		return c;
	}
	
	public static Long mul(Long a,Long b) {
		Long c=a*b;
		return c;
	}
	public static Long div(Long a,Long b) {
		Long c=a/b;
		return c;
	}
}
