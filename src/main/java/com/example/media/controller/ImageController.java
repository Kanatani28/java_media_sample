package com.example.media.controller;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeEditor;
import org.springframework.http.MediaTypeFactory;
import org.springframework.ui.Model;
import org.springframework.web.accept.MediaTypeFileExtensionResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.example.media.model.ImageData;
import com.example.media.service.ImageService;


@RestController
@RequestMapping("/images")
public class ImageController {
    
	@Autowired
	private ImageService imageService;
	
    @PostMapping("/add-image")
    public String addUserImage(Model model, @RequestParam("imageFile") MultipartFile file, 
    		HttpServletRequest req) throws IOException {
    	String token = imageService.add(file);
    	return "/images/get-image/" + token;
    }
    
    @GetMapping("/get-image/{token}")
    public HttpEntity<byte[]> getImage(@PathVariable("token") String token) {
    	ImageData image = imageService.find(token);
    	byte[] data = image.getData();
    	HttpHeaders headers = new HttpHeaders();
    	MediaType type = MediaTypeFactory.getMediaType(image.getFileName()).get();
    	headers.setContentType(type);
    	headers.setContentLength(data.length);
    	return new HttpEntity<byte[]>(image.getData(), headers);
    }
}
