package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class VideoReader {
	File videoFile = new File("/Users/santhoshsankaran/Downloads/software_dev/videos/DJI_0301.SRT");
	BufferedReader reader;
	
	public VideoReader( ) {
		try {
			reader = new BufferedReader(new FileReader(videoFile));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printCoordinates() {
		String latti = "", longi = "";
		int count = 1;
		try {
			while(reader.readLine() != null) {
				reader.readLine();
				for(String s : reader.readLine().split(",")) {
					if(count == 1 && !s.equals("0")) {
						latti = s;
						count++;
					} else if (count == 2 && !s.equals("0")) {
						longi = s;
						count--;
					}
				}
				System.out.println("Lattitude : " + latti + " Longitude : " + longi);
				reader.readLine();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		VideoReader videoReader = new VideoReader();
		videoReader.printCoordinates();
	}
}
