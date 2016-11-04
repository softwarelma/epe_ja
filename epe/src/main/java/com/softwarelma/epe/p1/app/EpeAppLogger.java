package com.softwarelma.epe.p1.app;

public abstract class EpeAppLogger {

	public static void log(EpeAppException e) {
		System.out.println(e.getMessage());
		e.printStackTrace();

		if (e.getCause() != null) {
			System.out.println(e.getCause().getMessage());
			e.getCause().printStackTrace();
		}
	}

	public static void log(String text) {
		System.out.println(text);
	}

}
