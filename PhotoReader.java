package main;

import java.io.File;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;

public class PhotoReader {
	File photoDirectory = new File("/Users/santhoshsankaran/Downloads/software_dev/images");
	File[] images = photoDirectory.listFiles();
	
	public void readNames() {
		try {
			for(File photo : images) {
				ImageMetadata metadata = Imaging.getMetadata(photo);
				System.out.println(photo.getName());

		        if (metadata instanceof JpegImageMetadata) {
		            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
		            
		            final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
		            
		            if (null != exifMetadata) {
		                final TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
		                if (null != gpsInfo) {
		                    final String gpsDescription = gpsInfo.toString();
		                    final double longitude = gpsInfo.getLongitudeAsDegreesEast();
		                    final double latitude = gpsInfo.getLatitudeAsDegreesNorth();

		                    System.out.println("    " + "GPS Description: "
		                            + gpsDescription);
		                    System.out.println("    "
		                            + "GPS Longitude (Degrees North): " + latitude);
		                    System.out.println("    "
		                            + "GPS Latitude (Degrees East): " + longitude);
		                }
		            }
		          }
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    
	public static void main(String[] args) {
		PhotoReader reader = new PhotoReader();
		reader.readNames();
	}
}
