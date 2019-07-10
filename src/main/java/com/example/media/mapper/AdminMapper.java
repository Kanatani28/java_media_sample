package com.example.media.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.media.model.Admin;

@Mapper
public interface AdminMapper {

	@Select("  SELECT "
			+ "  id,  "
			+ "  name, "
			+ "  email, "
			+ "  password, "
			+ "  created_at, "
			+ "  updated_at, "
			+ "  deleted_at "
			+ "FROM "
			+ "  users "
			+ "WHERE "
			+ "  email = #{email} "
			+ "AND "
			+ "  deleted_at IS NULL")
	public Admin find(String id);
}
