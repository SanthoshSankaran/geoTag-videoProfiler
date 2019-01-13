package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

public class KMLGenerator {
	
	VideoReader videoReader;
	File videoFile;
	Map<Double, Double> videoCoordinates;
	
	public KMLGenerator() {
		videoReader = new VideoReader();
		videoFile = videoReader.getVideoFile();
		videoCoordinates = videoReader.loadCoordinates();
	}
	
	/**
	 * Function to generate a KML file from the coordinates of the drone position for each second in the video read from the video coordinates map.
	 */
	public void createKMLFile() {

		String name = videoFile.getName().substring(0, videoFile.getName().length() - 4);
        String kmlstart = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n";

        String kmlelements = "";
        
        for(Map.Entry<Double, Double> entry : videoCoordinates.entrySet()) {
            kmlelements += "\t<Placemark>\n" +
                    	  "\t<name>Drone position</name>\n" +
                    	  "\t<description>"+name+"</description>\n" +
                    	  "\t<Point>\n" +
                          "\t\t<coordinates>"+ entry.getKey() + "," + entry.getValue() +"</coordinates>\n" +
                          "\t</Point>\n" +
                          "\t</Placemark>\n";
          }

        String kmlend = "</kml>";

        ArrayList<String> content = new ArrayList<String>();
        content.add(0,kmlstart);
        content.add(1,kmlelements);
        content.add(2,kmlend);

        String kmltest = content.get(0) + content.get(1) + content.get(2);

        Writer fwriter;

        try {
            fwriter = new FileWriter(videoFile.getParent() + "/" + name + ".kml");
            fwriter.write(kmltest);
            fwriter.flush();
            fwriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } 
	}
}
