package com.example.media.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.example.media.model.ArticleImage;

@Mapper
public interface ArticleImageMapper {
	
	@Insert("  INSERT INTO article_images( "
			+ "  article_id, "
			+ "  image_id "
			+ ") VALUES("
			+ "  #{articleId},"
			+ "  #{imageId} "
			+ ")")
	public int insert(ArticleImage image);
}
