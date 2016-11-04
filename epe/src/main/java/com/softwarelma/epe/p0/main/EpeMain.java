package com.softwarelma.epe.p0.main;

import com.softwarelma.epe.p1.app.EpeApp;
import com.softwarelma.epe.p1.app.EpeAppException;

public class EpeMain {

	public static void main(String[] args) {
		try {
			EpeApp app = new EpeApp();
			app.start(args);
		} catch (Exception e) {
			if (!(e instanceof EpeAppException)) {
				new EpeAppException("main", e);
			}

			System.exit(0);
		}
	}
}
