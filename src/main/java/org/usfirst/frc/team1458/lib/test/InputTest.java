package org.usfirst.frc.team1458.lib.test;

import org.usfirst.frc.team1458.lib.input.DigitalInput;
import org.usfirst.frc.team1458.lib.input.fake.FakeSwitch;
import org.usfirst.frc.team1458.lib.util.GlobalTeleUpdate;

public class InputTest {
	public static void main(String[] args) throws Exception {
		FakeSwitch s = new FakeSwitch();
		FakeSwitch up = new FakeSwitch();
		FakeSwitch down = new FakeSwitch();
		DigitalInput number = DigitalInput.fromUpDown(up, down, 0, 50);

		while(true){
			if(System.in.available() > 0) {
				while(System.in.available() > 0) {
					int read = System.in.read();
					if(read == 'u') {
						up.set(true);
						// TODO FIX
						//GlobalTeleUpdate.teleUpdate();
						up.set(false);
						System.out.println("UP");
					} else if(read == 'd') {
						down.set(true);
						// TODO FIX
						//GlobalTeleUpdate.teleUpdate();
						down.set(false);
						System.out.println("DOWN");
					}
				}

			}
			System.out.println("Number= "+number.getValue());
			// TODO FIX
			//GlobalTeleUpdate.teleUpdate();
			Thread.sleep(500);
		}
	}
}
