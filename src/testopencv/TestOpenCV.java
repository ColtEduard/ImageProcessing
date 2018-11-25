package testopencv;

import org.opencv.core.Core;

public class TestOpenCV{

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//System.loadLibrary("opencv_ffmpeg"+Core.VERSION_MAJOR+Core.VERSION_MINOR+Core.VERSION_REVISION+"_64");
		
		
		System.out.println(Core.NATIVE_LIBRARY_NAME);
		System.out.println("opencv_ffmpeg"+Core.VERSION_MAJOR+Core.VERSION_MINOR+Core.VERSION_REVISION+"_64");
	}
}
	
	