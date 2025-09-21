package com.kqube.cani.Controller;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kqube.cani.Impl.Question;
import com.kqube.cani.Service.CaniService;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/openai")
public class CaniController {
	@Autowired
	CaniService caniService;
	
	@PostMapping
	public Object getAnswer( @RequestBody Question question) {
		return caniService.getResult(question);
	}


    @PostMapping("/voice")
    public AssistantMessage handleVoice(@RequestParam("file") MultipartFile file) throws Exception {
        return caniService.transcribeVoice(file);
    }
}
