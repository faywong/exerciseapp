package io.github.faywong.exerciseapp.thirdparty;

import java.io.File; 
import java.io.FilenameFilter;

public class MusicFilter implements FilenameFilter{

	@Override
	public boolean accept(File dir, String filename) {
		// TODO Auto-generated method stub
		return (filename.endsWith(".mp3") || filename.endsWith(".oga") || filename.endsWith(".aac"));
	}

	 
}
