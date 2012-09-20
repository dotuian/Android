package com.ian.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class FileUtil {
	
	
	public static File[] listSortedFiles(File dirFile, boolean isCaseSensitive) {  
    	assert dirFile.isDirectory();  
  
        File[] files = dirFile.listFiles();  
          
        FileWrapper [] fileWrappers = new FileWrapper[files.length];
        for (int i=0; i<files.length; i++) {  
            fileWrappers[i] = new FileWrapper(files[i], isCaseSensitive);  
        }  
          
        Arrays.sort(fileWrappers);  
          
        File []sortedFiles = new File[files.length];  
        for (int i=0; i<files.length; i++) {  
            sortedFiles[i] = fileWrappers[i].getFile();  
        }  
          
        return sortedFiles;  
	} 

	
	public static String readFileByLines(String fileName) {
		File file = new File(fileName);

		return readFileByLines(file);
	}

	public static String readFileByLines(File file) {

		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
				sb.append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

	
	public static int countFileLines(File fileName) {

		String result = readFileByLines(fileName);

		return countLines(result);
	}

	
	public static int countLines(String content) {
		
//		String[] array = content.split("\n");
		
		StringTokenizer tokenizer = new StringTokenizer(content, "\n");  
	    return tokenizer.countTokens();  

		
//		if (array != null) {
//			return array.length;
//		} else {
//			return 0;
//		}
	}
	
}
