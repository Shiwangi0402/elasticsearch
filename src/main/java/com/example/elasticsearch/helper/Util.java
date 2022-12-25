package com.example.elasticsearch.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;

public class Util {

	public static String loadAsString(final String path) {
		try {
			final File resource = new ClassPathResource(path).getFile();
			return new String(Files.readAllBytes(resource.toPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
}
