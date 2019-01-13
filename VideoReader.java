package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class VideoReader {
	File videoFile;
	BufferedReader reader;
	
	public VideoReader() {
		try {
			videoFile = new File("/Users/santhoshsankaran/Downloads/software_dev/videos/DJI_0301.SRT");
			reader = new BufferedReader(new FileReader(videoFile));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setVideoFile(File videoFile) {
		this.videoFile = videoFile;
	}

	public File getVideoFile() {
		return videoFile;
	}

	/**
	 * Function to generate a map with the coordinates of the drone for each second from the video.
	 * @return
	 */
	public Map<Double, Double> loadCoordinates() {
		
		Map<Double, Double> resultMap = new HashMap<Double, Double>();
		double latti = 0, longi = 0;
		int count = 1;
		
		try {
			while(reader.readLine() != null) {
				reader.readLine();
				for(String s : reader.readLine().split(",")) {
					if(count == 1 && !s.equals("0")) {
						longi = Double.valueOf(s);
						count++; // counter set as 1 or 2 depending upon the reading of longitude and latitude respectively, to avoid any junk value from the file causing parsing exception.
					} else if (count == 2 && !s.equals("0")) {
						latti = Double.valueOf(s);
						count--;
					}
				}
				resultMap.put(longi, latti);
				reader.readLine();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
}
