package com.example.media.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.media.model.ImageData;

@Mapper
public interface ImageMapper {
	
	@Insert("  INSERT INTO images( "
			+ "  data, "
			+ "  file_name, "
			+ "  image_token "
			+ ") VALUES("
			+ "  #{data},"
			+ "  #{fileName},"
			+ "  #{imageToken} "
			+ ")")
	public int insert(ImageData image);

	@Select("  SELECT"
			+ "  id, "
			+ "  file_name, "
			+ "  image_token, "
			+ "  data, "
			+ "  created_at, "
			+ "  updated_at, "
			+ "  deleted_at "
			+ "FROM "
			+ " images "
			+ "WHERE "
			+ "  image_token = #{token} "
			+ "AND "
			+ "  deleted_at IS NULL")
	public ImageData selectByToken(String token);
}
