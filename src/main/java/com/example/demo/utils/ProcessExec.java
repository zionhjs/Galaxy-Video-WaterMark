package com.example.demo.utils;

import com.example.demo.Exception.WrongFileException;
import ws.schild.jave.*;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * to configure the parameters
 * https://www.youtube.com/watch?v=indYhp5zaog&t=176s
 */
public class ProcessExec {

    private Process process;

    public void execute(Map<String,String> dto) {
        StringBuffer waterlogo = new StringBuffer();
        waterlogo.append(" -i ");
        waterlogo.append(dto.get("input_path"));

        // test
        waterlogo.append(" -i " + dto.get("logo"));
        waterlogo.append(" -filter_complex" + " overlay=120:main_h-overlay_h-120 ");

        waterlogo.append(dto.get("video_converted_path"));
        System.out.println(waterlogo);
        Runtime run = Runtime.getRuntime();
        String ffmegPath = dto.get("ffmpeg_path");
        // execute the command
        try {
            System.out.println("all path: " + ffmegPath+waterlogo);
            java.lang.Process process = run.exec(ffmegPath+waterlogo);
        // async read & write
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
        /* BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,"gbk"));
                      String str1="";
                      while((str=br.readLine())!=null){
                          System.out.println(str1);
                      }*/

            ExecutorService service = Executors.newFixedThreadPool(2);

            ResultStreamHandler inputStreamHandler = new ResultStreamHandler(inputStream);
            ResultStreamHandler errorStreamHandler = new ResultStreamHandler(errorStream);

            service.execute(inputStreamHandler);
            service.execute(errorStreamHandler);
            process.waitFor();
            service.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}




