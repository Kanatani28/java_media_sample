package com.example.tool;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {

	public static void main(String[] args) {
		String rawPassword = "a";

		if (args.length > 0) {
			rawPassword = args[0];
		}
		
		System.out.println(new BCryptPasswordEncoder().encode(rawPassword));
	}
}
