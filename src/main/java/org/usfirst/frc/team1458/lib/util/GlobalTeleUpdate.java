package org.usfirst.frc.team1458.lib.util;

import java.util.ArrayList;

/**
 * Used when 1458lib classes require direct access to teleUpdate
 *
 * @author asinghani
 */
public class GlobalTeleUpdate {
	private static final ArrayList<Runnable> handlers = new ArrayList<>();

	public static void teleUpdate() {
		handlers.stream().forEach(Runnable::run);
	}

	public static void registerHandler(Runnable handler) {
		handlers.add(handler);
	}
}
