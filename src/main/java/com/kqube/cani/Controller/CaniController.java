package com.kqube.cani.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kqube.cani.Impl.Question;
import com.kqube.cani.Service.CaniService;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@RestController

@CrossOrigin(origins = "*")
@RequestMapping("/api/openai")

public class CaniController {

    @Autowired
    private CaniService caniService;

    // --- Text input
    @PostMapping
    public Object getAnswer(@RequestBody Question question) {
        return caniService.getResult(question);
    }

    // --- Audio input -> text output
    @PostMapping("/voice")
    public String handleVoice(@RequestParam("file") MultipartFile file) throws Exception {
        return caniService.transcribeVoice(file);
    }

    // --- Audio input -> audio output (voice mode)
    @PostMapping("/voice-mode")
    public ResponseEntity<byte[]> handleVoiceMode(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] audioBytes = caniService.voiceMode(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=response.mp3")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(audioBytes);
    }
    
    @GetMapping("/test")
    public String getData() {
    	return "According to a listing on ExportersIndia, KQubeSolutions is a manufacturer located in Secunderabad, Telangana, India. ExportersIndia+1\r\n"
    			+ "Their primary product is egg shell powder (Grade A) with a quoted price range of ₹500–₹600 per kg and a minimum order quantity (MOQ) of 10 kg. ExportersIndia\r\n"
    			+ "The company claims it will also in future supply chemicals and digital services. ExportersIndia\r\n"
    			+ "The business name “KQUBESOLUTIONS” appears to have been formally established in 2022 per the listing. ExportersIndia+1\r\n"
    			+ "The “About” listing indicates a small size: 6–20 employees";
    }
    
    @GetMapping("/voicetest")
    public String getVoiceData() {
    	return "According to a listing on ExportersIndia, KQubeSolutions is a manufacturer located in Secunderabad, Telangana, India. ExportersIndia+1\r\n"
    			+ "Their primary product is egg shell powder (Grade A) with a quoted price range of ₹500–₹600 per kg and a minimum order quantity (MOQ) of 10 kg. ExportersIndia\r\n"
    			+ "The company claims it will also in future supply chemicals and digital services. ExportersIndia\r\n"
    			+ "The business name “KQUBESOLUTIONS” appears to have been formally established in 2022 per the listing. ExportersIndia+1\r\n"
    			+ "The “About” listing indicates a small size: 6–20 employees";
    }
    
    @GetMapping("/speechtest")
    public String getspeectoVoiceData() {
    	return "According to a listing on ExportersIndia, KQubeSolutions is a manufacturer located in Secunderabad, Telangana, India. ExportersIndia+1\r\n"
    			+ "Their primary product is egg shell powder (Grade A) with a quoted price range of ₹500–₹600 per kg and a minimum order quantity (MOQ) of 10 kg. ExportersIndia\r\n"
    			+ "The company claims it will also in future supply chemicals and digital services.";
    }
    
}

