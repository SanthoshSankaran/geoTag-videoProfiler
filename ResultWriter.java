package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {

	VideoReader vidReader; 
	PhotoReader picReader; 
	
	
	public ResultWriter() {
		vidReader = new VideoReader();
		picReader = new PhotoReader();
	}

	public void loadResultToFile() {
		File output = new File("/Users/santhoshsankaran/Downloads/software_dev/output.csv");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {

		ResultWriter result = new ResultWriter();
		result.loadResultToFile();
	}

}
