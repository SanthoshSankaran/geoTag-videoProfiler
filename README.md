# Geotag Reader

The task is meant to present the person viewing a video to have the functionality to pause and take a look at the photos captured in and around the drone position (within 35 meters). It also has a few points of interest for which the images taken within 50 meters are required. 

The project uses the JPEG metadata extractor by "Alec Dhuse" (GeoTag and JpegGeoTagReader classes)for obtaining the GPS lattitude and longitude information from the images and computes for each second from the video file, the images that are within the bounds of 35 meters from the drone position. And similarly for the special points of interest.