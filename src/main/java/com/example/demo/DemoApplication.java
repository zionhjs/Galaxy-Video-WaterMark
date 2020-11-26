package com.example.demo;

import com.example.demo.Exception.WrongFileException;
import com.example.demo.utils.ProcessExec;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ws.schild.jave.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// String basePath = "/Users/zionhugh/Desktop/SpringProject/GalaxyProject/static/Videos";
		String basePath = "/Volumes/1/SpringProject/GalaxyProject/static/Videos";
		String[] videonames = getVideos(basePath + "/originVideo");
		for(String videoname: videonames){
			System.out.println("videoname:" + videoname);
			encodeVideo(basePath + "/originVideo" + "/" + videoname, basePath + "/4K" + "/" + "4k" + videoname,  3840, 2160);
		}
		videonames = getVideos(basePath + "/4K");
		for(String videoname: videonames){
			System.out.println("videoname:" + videoname);
			addWatermark(videoname);
		}
		SpringApplication.run(DemoApplication.class, args);
	}

	private static String[] getVideos(String path){
		File f = new File(path);
		String[] pathnames = f.list();

		pathnames = Arrays.stream(pathnames).filter(x -> (x.contains(".") && !x.equals(".DS_Store"))).toArray(String[]::new);
		for(String pathname: pathnames) System.out.println(pathname);
		return pathnames;
	}

	private static void addWatermark(String videoname){
		ProcessExec ps = new ProcessExec();
		HashMap<String, String> dto = new HashMap<String, String>();
		String basePath = "/Volumes/1/SpringProject/GalaxyProject";
		// dto.put("ffmpeg_path", "/Users/zionhugh/Desktop/SpringProject/GalaxyProject/static/ffmpeg");
		dto.put("ffmpeg_path", basePath + "/static/ffmpeg");
		// dto.put("input_path", "/Users/zionhugh/Desktop/SpringProject/GalaxyProject/static/Videos/4K/" + videoname);
		dto.put("input_path", basePath + "/static/Videos/4K/" + videoname);
		// dto.put("video_converted_path", "/Users/zionhugh/Desktop/SpringProject/GalaxyProject/static/Videos/converted/" + ("watermarked" + videoname));
		dto.put("video_converted_path", basePath + "/static/Videos/converted/" + ("watermarked" + videoname));
		// dto.put("logo", "/Users/zionhugh/Desktop/SpringProject/GalaxyProject/static/logo/logo-11.png");
		// dto.put("logo", "/Users/zionhugh/Desktop/SpringProject/GalaxyProject/static/logo/2x/logo-11-2x.png");
		dto.put("logo", basePath + "/static/logo/2x/logo-11-2x.png");
		ps.execute(dto);
	}

	// for encode file & resize File
	private static File encodeVideo(String sourcePath, String targetPath, int width, int height){
		System.out.println("sourcePath: " + sourcePath + " targetPath: " + targetPath);
		File source = new File(sourcePath);
		File target = new File(targetPath);
		// Audio
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("aac");
		audio.setBitRate(64000);
		audio.setChannels(2);
		audio.setSamplingRate(44100);

		// Video
		VideoAttributes video = new VideoAttributes();
		video.setCodec("h264");
		video.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
		// video.setBitRate(160000);
		video.setBitRate(12000000);
		// keep the frames based on mobile devices
		video.setFrameRate(60);
		video.setSize(new VideoSize(width,height));

		// Attrs
		// it.sauronsoftware.jave.EncodingAttributes attrs = new it.sauronsoftware.jave.EncodingAttributes();
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp4");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);

		// Listener

		// execute
		try{
			MultimediaObject multimediaObject = new MultimediaObject(source);
			Encoder encoder = new Encoder();
			encoder.encode(multimediaObject, target, attrs);
		}catch(Exception e){
			e.printStackTrace();
			throw new WrongFileException();
		}
		return target;
	}
}


