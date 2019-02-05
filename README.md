# Geotag Reader, Video Profiler & KML generator

The task is meant to present the person viewing a video(in this project, used as an SRT file with geographical information) to have the functionality to pause the video and take a look at the photos captured in and around(within 35 meters) the drone position at that second. It also has a few points of interest for which the images taken within 50 meters are required to be looked at. 

The project uses the JPEG metadata extractor by "Alec Dhuse" (GeoTag and JpegGeoTagReader classes)for obtaining the GPS lattitude and longitude information from the images and computes for each second from the video file, the images that are within the bounds of 35 meters from the drone position. And similarly for the special points of interest.
