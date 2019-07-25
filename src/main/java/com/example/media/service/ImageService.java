package com.example.media.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.media.mapper.ImageMapper;
import com.example.media.mapper.ArticleImageMapper;
import com.example.media.model.Admin;
import com.example.media.model.ImageData;
import com.example.media.model.ArticleImage;
import com.example.media.security.SecurityUser;
import com.example.media.utils.CommonUtils;

@Service
@Transactional
public class ImageService {

	@Autowired
	private ImageMapper imageMapper;
	
	@Autowired
	private ArticleImageMapper userImageMapper;
	
	public String add(MultipartFile file) throws IOException {
		
		String token = null;
		
		CommonUtils.createToken();
		boolean tokenIsNotUnique = true;
		while(tokenIsNotUnique) {
			token = CommonUtils.createToken();
			ImageData found = imageMapper.selectByToken(token);
			if(found == null) {
				tokenIsNotUnique = false;
			}
		}
		
		ImageData image = new ImageData();
		image.setFileName(file.getOriginalFilename());
		image.setData(file.getBytes());
		image.setImageToken(token);
		imageMapper.insert(image);
		
		image = imageMapper.selectByToken(token);
		
//		ArticleImage userImage = new ArticleImage();
//		userImage.setArticleId(articleId);
//		userImage.setImageId(image.getId());
//		userImageMapper.insert(userImage);
		
		return token;
	}
	
	public ImageData find(String token) {
		ImageData image = new ImageData();
		image = imageMapper.selectByToken(token);
		return image;
	}
}
