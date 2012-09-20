package com.ian.utils;

import java.io.File;

@SuppressWarnings("rawtypes")
class FileWrapper implements Comparable {

	private File file;
	
	private boolean isCaseSensitive = true;
	
	public FileWrapper(File file) {
		this.file = file;
	}
	
	public FileWrapper(File file, boolean isCaseSensitive) {
		this.file = file;
		this.isCaseSensitive = isCaseSensitive;
	}


	public int compareTo(Object obj) {
		assert obj instanceof FileWrapper;

		FileWrapper castObj = (FileWrapper) obj;
		
		String str1 = "";
		String str2 = "";
		
		if(isCaseSensitive){
			str1 = this.file.getName();
			str2 = castObj.getFile().getName();
		} else {
			str1 = this.file.getName().toLowerCase();
			str2 = castObj.getFile().getName().toLowerCase();
		}
		
		if (str1.compareTo(str2) > 0) {
			return 1;
		} else if (str1.compareTo(str2) < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	public File getFile() {
		return this.file;
	}
}