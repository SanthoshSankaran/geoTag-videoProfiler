package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultWriter {

	VideoReader vidReader;
	PhotoReader picReader;
	Map<String, List<Double>> photoCoordinates;
	Map<Double, Double> gpsCoordinates;
	Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
	int focusLimit, POIFocusLimit;
	
	public int getFocusLimit() {
		return focusLimit;
	}

	public void setFocusLimit(int setFocusLimit) {
		this.focusLimit = setFocusLimit;
	}

	public int getPOIFocusLimit() {
		return POIFocusLimit;
	}

	public void setPOIFocusLimit(int setPOIFocusLimit) {
		this.POIFocusLimit = setPOIFocusLimit;
	}

	/**
	 * Constructor. Initializes and generates the lists required for future computation.
	 */
	public ResultWriter() {
		
		vidReader = new VideoReader();
		picReader = new PhotoReader();
		photoCoordinates = picReader.generateGpsList();
		gpsCoordinates = vidReader.loadCoordinates();
		
		// Setting the default values for the focus limit for the photos
		setFocusLimit(35);
		setPOIFocusLimit(50);
	}

	/**
	 * This function is for writing the results for the special POIs at the shooting location.
	 */
	private void loadPointsOfInterestPhotos() {
		
		try {
			// The assets file is read to recreate a similar structure in the output CSV file.
			File assets = new File("/Users/santhoshsankaran/Downloads/software_dev/assets.csv");
			File output = new File("/Users/santhoshsankaran/Downloads/software_dev/assetsOutput.csv");
			
			//To check for the full read of the file. Acts as a counter.
			int lines = (int) Files.lines(assets.toPath()).count();

			BufferedReader reader = new BufferedReader(new FileReader(assets));
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			
			String curr = reader.readLine();
			StringBuilder str = new StringBuilder();
			for(String s : curr.split(",")) {
				writer.write(s + ", ");
			}
			
			writer.newLine();
			
			curr = reader.readLine();
			
			while(curr != null && lines > 1) {
				String[] s = curr.split(",");
				str.append(curr);
				double currLongi = Double.parseDouble(s[1]);
				double currLatti = Double.parseDouble(s[2]);
				double distance = 0;
				
				for(Map.Entry<String, List<Double>> photoEntry : photoCoordinates.entrySet()) {
					List<Double> photoPoints = photoEntry.getValue();
					if(photoPoints.size() == 2) {
						distance = calculateDistance(currLongi, currLatti, photoPoints.get(0), photoPoints.get(1));
					}
					if(distance <= POIFocusLimit) {
						str.append(photoEntry.getKey() + ", ");
					}
				}
				//Writing the composed line to the output file every time we have the list of photos for the particular POI
				writer.write(str.toString());

				str.setLength(0); // Clearing the string builder contents.
				writer.newLine();
				lines--;
				curr = reader.readLine();
			}
		
			reader.close();
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This function is to populate the list of photos within 35 meters of the drone location for each second of the video and write it onto a CSV file.
	 */
	public void loadResultToFiles() {
		
		File output = new File("/Users/santhoshsankaran/Downloads/software_dev/output.csv");
		BufferedWriter writer;
		
		try {
			writer = new BufferedWriter(new FileWriter(output));
			//Headers for the file.
			writer.write("Time, ");
			writer.write("Filenames");
			writer.newLine();
			
			Set<Integer> keySet = result.keySet();
			Iterator<Integer> ite = keySet.iterator();
			
			while(ite.hasNext()) {
				int key =ite.next();
				
				//As each entry in the map is populated for each second of the video (read from gpsCoordinates map), we use the index as the value for time and the list of photo filenames as the value.
				writer.write(key + ", ");
				writer.write(result.get(key).toString());
				writer.newLine();
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to calculate the distance between two GPS coordinates.
	 * 
	 * @param videoLongi
	 * @param videoLatti
	 * @param photoLongi
	 * @param photoLatti
	 * @return
	 */
	private double calculateDistance(double videoLongi, double videoLatti, double photoLongi, double photoLatti) {
		
		int R = 6371000; // Radius of the earth in meters
		// 1 degree = 0.01754533 radians
		double lat1 = videoLatti*0.0174533, lat2 = photoLatti*0.0174533, delta = lat2 - lat1, deltaLambda = (photoLongi - videoLongi)*0.0174533;
		
		double a = (Math.sin(delta) * Math.sin(delta/2)) + (Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R * c;
		
		return d; // In meters
	}
	
	/**
	 * Function to load the list of photos into a map with the key as the time in seconds from the video file.
	 */
	private void findPhotos() {
		
		double distance = 0;
		int index = 1;
		
		for(Map.Entry<Double, Double> entry : gpsCoordinates.entrySet()) {
			
			double currLongi = entry.getKey();
			double currLatti = entry.getValue();
			
			for(Map.Entry<String, List<Double>> photoEntry : photoCoordinates.entrySet()) {
				List<Double> photoPoints = photoEntry.getValue();
				
				if(photoPoints.size() == 2) { // Safety bounds check for the entries in the photo coordinates map.
					distance = calculateDistance(currLongi, currLatti, photoPoints.get(0), photoPoints.get(1));
				}
				
				if(distance <= focusLimit) {
					if(result.containsKey(index)) { // Updating the existing list
						List<String> list = result.get(index);
						list.add(photoEntry.getKey());
						result.put(index, list);
					} else { // Adding a new list
						result.put(index, new ArrayList<String>(){
							private static final long serialVersionUID = 1L;
						{add(photoEntry.getKey());}});
					}
				}
			}
			index++;
		}	
	}
	
	/**
	 * Main function to instantiate and generate the desired output to various files.
	 * @param args
	 */
	public static void main(String[] args) {

		ResultWriter result = new ResultWriter();
		KMLGenerator kmlGenerator = new KMLGenerator();
		result.findPhotos();
		result.loadResultToFiles();
		result.loadPointsOfInterestPhotos();
		kmlGenerator.createKMLFile();
	}

}
