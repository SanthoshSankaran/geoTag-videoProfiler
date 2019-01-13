package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoReader {
	
	double longitude, latitude;
	File photoDirectory;
	File[] files;
	JpegGeoTagReader jpgReader;
	
	public PhotoReader() {
		photoDirectory = new File("/Users/santhoshsankaran/Downloads/software_dev/images");
		files = photoDirectory.listFiles();
		jpgReader = new JpegGeoTagReader();
	}
	
	/**
	 * Function to generate a map containing the photo filename and its corresponding coordinates.
	 * @return
	 */
	public Map<String, List<Double>> generateGpsList() {
		
		Map<String, List<Double>> resultMap = new HashMap<String, List<Double>>();
		
		try {
			for(File photo : files) {
				
				if(photo.getAbsolutePath( ).endsWith( ".JPG" )) { // Safety check for file type, IOException.
					
					GeoTag geotag = jpgReader.readMetadata(photo);
					longitude = geotag.getLongitude();
					latitude = geotag.getLatitude();
					
					resultMap.put(photo.getName(), new ArrayList<Double>(){
						private static final long serialVersionUID = 1L;{
							add(longitude); 
							add(latitude);
							}
						});
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}    
}
