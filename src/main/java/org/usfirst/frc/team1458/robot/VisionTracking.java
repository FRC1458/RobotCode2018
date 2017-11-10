package org.usfirst.frc.team1458.robot;

import edu.wpi.cscore.HttpCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VisionTracking {
	private VideoSource videoSource;
	private static int uniqueId = 0;

	public static final Object lock = new Object();

	private List<Rect> contours = new ArrayList<>();

	private HttpCamera httpCamera;

	/**
	 * Instantiates BlastoiseVision with a VideoSource object.
	 * This method is not recommended to be used directly, use one of the others instead.
	 * @param videoSource
	 */
	public VisionTracking(VideoSource videoSource) {
		this.videoSource = videoSource;
		videoSource.setResolution(320,240);

		VisionThread visionThread = new VisionThread(videoSource, new DetectVisionTarget(), pipeline -> {
			synchronized (lock) {
				processContours(pipeline.filterContours0Output());
			}
		});
		visionThread.start();
	}

	private VisionTracking(HttpCamera camera) {
		this((VideoSource) camera);
		this.httpCamera = camera;
	}

	/**
	 * Instantiates BlastoiseVision with a MJPG stream url. This is the recommended method.
	 * @param streamUrl
	 */
	public VisionTracking(String streamUrl) {
		this(new HttpCamera(streamUrl+uniqueId, streamUrl, HttpCamera.HttpCameraKind.kMJPGStreamer));
		uniqueId++;

		//httpCamera.setBrightness(5);
		//httpCamera.setExposureManual(0);
		//httpCamera.setExposureAuto();

		/*try {
			CameraSetup.initialSetup("localhost", 5800);
			CameraSetup.startVision("localhost", 5800);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * Instantiates BlastoiseVision with a USB camera. USE AT YOUR OWN RISK, THIS CAUSES MEMORY LEAKS!
	 * @param cameraNumber
	 */
	public VisionTracking(int cameraNumber) {
		this(new UsbCamera(cameraNumber+""+uniqueId, cameraNumber));
	}

	protected void processContours(List<MatOfPoint> data) {
		System.out.println(data.toString());
		contours.clear();
		if (!data.isEmpty()) {
			for(MatOfPoint matOfPoint : data) {
				contours.add(Imgproc.boundingRect(matOfPoint));
				matOfPoint.release();
			}
		}
	}

	public ArrayList<Rect> getContours() {
		synchronized (lock) {
			return new ArrayList<>(contours);
		}
	}

	/*public double getShooterTargetDistance() {
		ArrayList<Rect> contours = getCorrectContours();
		if(contours.size() != 2){
			return -1;
		}

		double x1 = contours.get(0).x + (contours.get(0).width/2.0);
		double x2 = contours.get(1).x + (contours.get(1).width/2.0);

		double y1 = contours.get(0).y + (contours.get(0).height/2.0);
		double y2 = contours.get(1).y + (contours.get(1).height/2.0);

		double xCoord = (x1 + x2) / 2.0;
		double yCoord = (y1 + y2) / 2.0;

		SmartDashboard.putNumber("X of target", xCoord);
		SmartDashboard.putNumber("Y of target", yCoord);

		double cameraHeightDifference = (78 - Constants.ShooterVision.Camera.MOUNT_HEIGHT);
		double pixelDifference = (Constants.ShooterVision.Camera.HEIGHT_PX - yCoord) / Constants.ShooterVision.Camera.HEIGHT_PX;
		double angle = Constants.ShooterVision.Camera.MOUNT_ANGLE +
				(Constants.ShooterVision.Camera.HEIGHT_FOV * pixelDifference);
		angle = Math.toRadians(angle);
		double distance = cameraHeightDifference * (1 / Math.tan(angle));

		SmartDashboard.putNumber("DistanceToBoiler", distance);

		return distance;
	}*/

	public ArrayList<Rect> getCorrectContours() {
		ArrayList<Rect> contours = getContours();
		if(contours.size() < 2){
			return new ArrayList<>();
		}

		Collections.sort(contours, (Rect r1, Rect r2) -> ((int) (r1.area() - r2.area())));
		Collections.reverse(contours);
		Rect contour1 = contours.get(0);
		Rect contour2 = new Rect(100000, 100000, 100000, 100000);
		for(int i = 1; i < contours.size(); i++) {
			if(Math.abs(contour1.x - contour2.x) > Math.abs(contour1.x - contours.get(i).x)){
				contour2 = contours.get(i);
			}
		}

		//System.out.println(contour1.toString() + ", "+contour2.toString());
		SmartDashboard.putString("ContoursInfo", contour1.toString() + ", "+contour2.toString());

		return new ArrayList<>(Arrays.asList(new Rect[]{contour1, contour2}));
	}

	public double getShooterTargetX() {
		ArrayList<Rect> contours = getCorrectContours();
		//System.out.println(contours.toString());
		if(contours.size() != 2){
			return -1;
		}

		double x1 = contours.get(0).x + (contours.get(0).width/2.0);
		double x2 = contours.get(1).x + (contours.get(1).width/2.0);

		double y1 = contours.get(0).y + (contours.get(0).height/2.0);
		double y2 = contours.get(1).y + (contours.get(1).height/2.0);

		double xCoord = (x1 + x2) / 2.0;
		double yCoord = (y1 + y2) / 2.0;

		SmartDashboard.putNumber("X of target", xCoord);
		SmartDashboard.putNumber("Y of target", yCoord);


		return xCoord;
	}

	public void close() {
		videoSource.free();
	}
}
