package testopencv;

import java.awt.geom.Area;
import java.beans.MethodDescriptor;
import java.util.concurrent.Executors; 
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoCapture;



import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Utils;

/**
 * The controller associated with the only view of our application. The
 * application logic is implemented here. It handles the button for
 * starting/stopping the camera, the acquired video stream, the relative
 * controls and the histogram creation.
 * 
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @version 2.0 (2017-03-10)
 * @since 1.0 (2013-11-20)
 * 
 */
public class VideoController
{
	// the FXML button
	@FXML
	private Button button;
	
	// the FXML area for showing the current frame
	@FXML
	private ImageView currentFrame;
	
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that realizes the video capture
	private VideoCapture capture;
	// a flag to change the button behavior
	private boolean cameraActive;
	// the logo to be loaded
	private Mat logo;
	
	/**
	 * Initialize method, automatically called by @{link FXMLLoader}
	 */
	public void initialize()
	{
		System.out.println("initialized");
		this.capture = new VideoCapture();
		this.cameraActive = false;
	}
	
	/**
	 * The action triggered by pushing the button on the GUI
	 */
	@FXML
	protected void startCamera()
	{
		// set a fixed width for the frame
		this.currentFrame.setFitWidth(600);
		// preserve image ratio
		this.currentFrame.setPreserveRatio(true);
		
		if (!this.cameraActive)
		{
			
			// start the video capture
			//this.capture.open(0);
			//Read the video (Chage video)
			this.capture.open("D:\\Git\\ImageProcessing\\AB.wmv");
			
			
			System.out.println("start capture...");
			// is the video stream available?
			if (this.capture.isOpened())
			{
				System.out.println("inif");
				this.cameraActive = true;
				
				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run()
					{
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						
						// convert and show the frame
//						Image imageToShow = Utils.mat2Image(frame);
//						updateImageView(currentFrame, imageToShow);
					}
				};
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				// update the button content
				this.button.setText("Stop Video");
			}
			else
			{
				// log the error
				System.err.println("Impossible to open the video...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.button.setText("Start Video");
			
			// stop the timer
			this.stopAcquisition();
		}
	}
	
	
	
	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link Image} to show
	 */
	private Mat grabFrame()
	{
		Mat frame = new Mat();
		
		// check if the capture is open
		if (this.capture.isOpened()) 
		{
			
			try
			{
//				this.capture.set( Videoio.CV_CAP_PROP_FRAME_WIDTH, 640);
//				this.capture.set( Videoio.CV_CAP_PROP_FRAME_HEIGHT, 480);
				this.capture.grab();
				// read the current frame
				this.capture.read(frame);
				
				
				
				// if the frame is not empty, process it
				if (!frame.empty())
				{
					// Processing starts here
					
					// Mat hsv = new Mat();
					// Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV);
					//
					// Scalar lowerTreshold = new Scalar(132,135,160);
					// Scalar upperTreshold = new Scalar(139,210,192);
					//
					// Core.inRange(frame, lowerTreshold, upperTreshold, hsv);
					//
					// Image imageToShow = Utils.mat2Image(hsv);
					// updateImageView(currentFrame, imageToShow);
					
				
					// create a new Mat so we don't alter the original frame
					
					Mat modifiedFrame = new Mat();
				
//					// wider range of blue
//					Scalar lowerTreshold = new Scalar(235,150,90);
//					Scalar upperTreshold = new Scalar(250,230,155);

					// what works
					Scalar lowerTreshold = new Scalar(235,150,110);
					Scalar upperTreshold = new Scalar(245,205,155);
					
					
					Core.inRange(frame, lowerTreshold, upperTreshold, modifiedFrame);
					
					Mat canny = new Mat();
					
					Imgproc.Canny(modifiedFrame, canny , 200, 400);
					
					//Erode

					int erosionsize = 5;
					Size ksize = new Size(erosionsize, erosionsize);
					//kernel pentru eroziune si dilatare
					Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, ksize);
//					Imgproc.erode(canny, erode, element);
//					
					//Dilate
					Mat dilate = new Mat();
					Imgproc.dilate(canny, dilate, kernel);
					
					
					
//					//Opening
//					Mat opening = new Mat();
//					Imgproc.dilate(erode, opening, element);
//					
					//Closing
					Mat erode = new Mat();
					Imgproc.erode(dilate, erode, kernel);
					

	
					// find contours
					Mat drawContour = new Mat();
					Imgproc.cvtColor(erode, drawContour, Imgproc.COLOR_GRAY2BGR);
					
					List<MatOfPoint> contours = new ArrayList<>();
					Imgproc.findContours(erode,contours,new Mat(),Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_NONE);

					Iterator<MatOfPoint> iterator = contours.iterator();
					
				
					while (iterator.hasNext()){
						MatOfPoint contour = iterator.next();
						//calculate area of contour
						double area = Imgproc.contourArea(contour);
						System.out.println("Area:"+area);
						
						
					//	if(area > 15) {
						// draw rectangles on frame
						Rect rect = Imgproc.boundingRect(contour);
						Imgproc.rectangle(frame, new Point(rect.x,rect.y), 
								new Point(rect.x+rect.width,rect.y+rect.height), 
								new Scalar(0, 0, 160), 2); 
						System.out.println(rect.x+":"+rect.y+":"+rect.width+":"+rect.height);
					}
					
				//	}
					
					Image imageToShow = Utils.mat2Image(erode);
					updateImageView(currentFrame, imageToShow);
					
					
				}
				
				
				
				
			}
			catch (Exception e)
			{
				// log the error
				System.err.println("Exception during the frame elaboration: " + e);
			}
		}
		System.out.println(frame);
		return frame;
	}
	
	
	
	/**
	 * Stop the acquisition from the camera and release all the resources
	 */
	private void stopAcquisition()
	{
		if (this.timer != null && !this.timer.isShutdown())
		{
			try
			{
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to close the video now... " + e);
			}
		}
		
		if (this.capture.isOpened())
		{
			// release the camera
			this.capture.release();
		}
	}
	
	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view
	 *            the {@link ImageView} to update
	 * @param image
	 *            the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image)
	{
		Utils.onFXThread(view.imageProperty(), image);
	}
	
	/**
	 * On application close, stop the acquisition from the camera
	 */
	protected void setClosed()
	{
		this.stopAcquisition();
	}
}
