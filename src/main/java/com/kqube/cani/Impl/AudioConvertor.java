package com.kqube.cani.Impl;

import java.io.File;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

public class AudioConvertor {

    public static File convertToMp3(File source) throws Exception {
        File target = new File(source.getParent(), "converted.mp3");

        // Configure audio attributes
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");   
        audio.setBitRate(128000);         
        audio.setChannels(2);           
        audio.setSamplingRate(44100);     

        // Encoding attributes
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");         
        attrs.setAudioAttributes(audio);

        // Perform conversion
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);

        return target;
    }
}
