package org.usfirst.frc.team1458.lib.hardware;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import org.usfirst.frc.team1458.lib.input.*;
import org.usfirst.frc.team1458.lib.input.AnalogInput;
import org.usfirst.frc.team1458.lib.input.XboxController;
import org.usfirst.frc.team1458.lib.motor.*;
import org.usfirst.frc.team1458.lib.motor.TalonSRX;
import org.usfirst.frc.team1458.lib.motor.abilities.BrakeMode;
import org.usfirst.frc.team1458.lib.sensor.AngleSensor;
import org.usfirst.frc.team1458.lib.sensor.ThreeAxisAccelerometer;
import org.usfirst.frc.team1458.lib.util.maths.InputFunction;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the class which controls access to any hardware device (input/output).
 * @implNote This class should be the only class which instantiates the access to WPILib hardware objects.
 *
 * @author asinghani
 */
public class Hardware {

	/**
	 * Factory to instantiate joysticks, gamepads, and other controllers for the driver to use.
	 */
	public static class HumanInterfaceDevices {
		/**
		 * Get a flight controller connected on the specified USB port
		 * @param usbPort
		 * @return a FlightStick which is connected to the specified USB port
		 */
		public static FlightStick FlightStick(int usbPort) {
			return new FlightStick() {
				Joystick joystick = new Joystick(usbPort);

				public AnalogInput getAxis(int axis) {
					return () -> joystick.getRawAxis(axis);
				}

				public Switch getButton(int button) {
					return () -> joystick.getRawButton(button);
				}

				public POV getPOV() {
					return () -> POV.Direction.fromAngle(joystick.getPOV());
				}
			};
		}

		/**
		 * Get an xbox controller connected on the specified USB port
		 * @param usbPort
		 * @return an XboxController which is connected to the specified USB port
		 */
		public static XboxController XboxController(int usbPort) {
			return new XboxController() {

				Joystick joystick = new Joystick(usbPort);

				public AnalogInput getAxis(int axis) {
					return () -> joystick.getRawAxis(axis);
				}

				public Switch getButton(int button) {
					return () -> joystick.getRawButton(button);
				}

				public POV getPOV() {
					return () -> POV.Direction.fromAngle(joystick.getPOV());
				}

				@Override
				public void rumble(float strength, long millis) {
					joystick.setRumble(Joystick.RumbleType.kLeftRumble, strength);
					joystick.setRumble(Joystick.RumbleType.kRightRumble, strength);

					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							joystick.setRumble(Joystick.RumbleType.kLeftRumble, 0);
							joystick.setRumble(Joystick.RumbleType.kRightRumble, 0);
						}
					}, millis);
				}
			};
		}
	}

	/**
	 * Factory to instantiate motor controllers.
	 */
	public static class Motors {

		/**
		 * Return a new PWM Jaguar motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @return an instance of Motor representing this Jaguar controller
		 */
		public static Motor Jaguar(int channel) {
			return Jaguar(channel, InputFunction.IDENTITY);
		}

		/**
		 * Return a new PWM Jaguar motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @param scale an InputFunction which will scale all values applied to the motor
		 * @return an instance of Motor representing this Jaguar controller
		 */
		public static Motor Jaguar(int channel, InputFunction scale) {
			return new HardwareMotorPWM(new Jaguar(channel)).scale(scale);
		}



		/**
		 * Return a new PWM Spark motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @return an instance of Motor representing this Spark controller
		 */
		public static Motor Spark(int channel) {
			return Spark(channel, InputFunction.IDENTITY);
		}

		/**
		 * Return a new PWM Spark motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @param scale an InputFunction which will scale all values applied to the motor
		 * @return an instance of Motor representing this Spark controller
		 */
		public static Motor Spark(int channel, InputFunction scale) {
			return new HardwareMotorPWM(new Spark(channel)).scale(scale);
		}



		/**
		 * Return a new PWM Talon SR motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @return an instance of Motor representing this Talon SR controller
		 */
		public static Motor TalonSR(int channel) {
			return TalonSR(channel, InputFunction.IDENTITY);
		}

		/**
		 * Return a new PWM Talon SR motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @param scale an InputFunction which will scale all values applied to the motor
		 * @return an instance of Motor representing this Talon SR controller
		 */
		public static Motor TalonSR(int channel, InputFunction scale) {
			return new HardwareMotorPWM(new Talon(channel)).scale(scale);
		}



		/**
		 * Return a new PWM Victor 888 motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @return an instance of Motor representing this Victor 888 controller
		 */
		public static Motor Victor888(int channel) {
			return Victor888(channel, InputFunction.IDENTITY);
		}

		/**
		 * Return a new PWM Victor 888 motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @param scale an InputFunction which will scale all values applied to the motor
		 * @return an instance of Motor representing this Victor 888 controller
		 */
		public static Motor Victor888(int channel, InputFunction scale) {
			return new HardwareMotorPWM(new Victor(channel)).scale(scale);
		}



		/**
		 * Return a new PWM Victor SP motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @return an instance of Motor representing this Victor SP controller
		 */
		public static Motor VictorSP(int channel) {
			return VictorSP(channel, InputFunction.IDENTITY);
		}

		/**
		 * Return a new PWM Victor SP motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @param scale an InputFunction which will scale all values applied to the motor
		 * @return an instance of Motor representing this Victor SP controller
		 */
		public static Motor VictorSP(int channel, InputFunction scale) {
			return new HardwareMotorPWM(new VictorSP(channel)).scale(scale);
		}



		/**
		 * Return a new PWM Talon SRX motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @return an instance of Motor representing this Talon SRX controller
		 */
		public static Motor TalonSRX_PWM(int channel) {
			return TalonSRX_PWM(channel, InputFunction.IDENTITY);
		}

		/**
		 * Return a new PWM Talon SRX motor controller
		 * @param channel the PWM channel on the RoboRIO which the controller is connected to
		 * @param scale an InputFunction which will scale all values applied to the motor
		 * @return an instance of Motor representing this Talon SRX controller
		 */
		public static Motor TalonSRX_PWM(int channel, InputFunction scale) {
			return new HardwareMotorPWM(new edu.wpi.first.wpilibj.TalonSRX(channel)).scale(scale);
		}



		/**
		 * Return a new CAN Talon SRX controller
		 * @param CANid the CAN id of the Talon SRX controller
		 * @return an instance of TalonSRX representing this Talon SRX controller
		 */
		public static TalonSRX TalonSRX_CAN(int CANid) {
			return new TalonSRX(CANid);
		}

		/**
		 * Return a new CAN Talon SRX controller
		 * @param CANid the CAN id of the Talon SRX controller
		 * @param brakeMode the brake mode to set on the Talon SRX controller
		 * @return an instance of TalonSRX representing this Talon SRX controller
		 */
		public static TalonSRX TalonSRX_CAN(int CANid, BrakeMode brakeMode) {
			return new TalonSRX(CANid, brakeMode);
		}

		/**
		 * Return a new CAN Talon SRX controller
		 * @param CANid the CAN id of the Talon SRX controller
		 * @param master the Talon SRX which this motor should follow
		 * @return an instance of TalonSRX representing this Talon SRX controller
		 */
		public static TalonSRX TalonSRX_CAN(int CANid, TalonSRX master) {
			return new TalonSRX(CANid, master);
		}
	}

	/**
	 * Factory to instantiate accelerometers.
	 */
	public static final class Accelerometers {
		/**
		 * Return an ADXL345 on the specified I2C port.
		 *
		 * @param port the I2C port used by the accelerometer; not null
		 * @param range the desired range of the accelerometer; not null
		 * @return the accelerometer; never null
		 */
		public static ThreeAxisAccelerometer I2C(I2C.Port port, Accelerometer.Range range) {
			if (port == null) throw new IllegalArgumentException("The I2C port must be specified");
			if (range == null) throw new IllegalArgumentException("The accelerometer range must be specified");
			ADXL345_I2C accel = new ADXL345_I2C(port, range);
			return ThreeAxisAccelerometer.create(accel::getX, accel::getY, accel::getZ);
		}

		/**
		 * Return an ADXL345 on the specified SPI port.
		 *
		 * @param port the SPI port used by the accelerometer; not null
		 * @param range the desired range of the accelerometer; not null
		 * @return the accelerometer; never null
		 */
		public static ThreeAxisAccelerometer SPI(SPI.Port port, Accelerometer.Range range) {
			if (port == null) throw new IllegalArgumentException("The SPI port must be specified");
			if (range == null) throw new IllegalArgumentException("The accelerometer range must be specified");
			ADXL345_SPI accel = new ADXL345_SPI(port, range);
			return ThreeAxisAccelerometer.create(accel::getX, accel::getY, accel::getZ);
		}

		/**
		 * Return the RoboRIO's built-in accelerometer.
		 *
		 * @return the accelerometer; never null
		 */
		public static ThreeAxisAccelerometer builtIn() {
			BuiltInAccelerometer accel = new BuiltInAccelerometer();
			return ThreeAxisAccelerometer.create(accel::getX, accel::getY, accel::getZ);
		}
	}

	/**
	 * Factory to instantiate angle sensors.
	 */
	public static final class AngleSensors {

		/**
		 * TODO ADD JAVADOC
		 * @param a
		 * @param b
		 * @param distancePerPulse
		 * @return
		 */
		public static AngleSensor encoder(int a, int b, double distancePerPulse) {
			Encoder encoder = new Encoder(a, b);
			encoder.setDistancePerPulse(distancePerPulse);
			return AngleSensor.create(encoder::getDistance);
		}
	}
}
