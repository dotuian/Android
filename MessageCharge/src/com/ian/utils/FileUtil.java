package com.ian.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {

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

		return countFileLines(result);
	}

	public static int countFileLines(String content) {

		String[] array = content.split("\n");

		if (array != null) {
			return array.length;
		} else {
			return 0;
		}
	}

}
