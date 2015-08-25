package com.schander.libroid;

import java.io.File;

public class FileEntry {
	
	private int icon;
	private String fileName;
	private File file;
	
	public FileEntry(int icon, String fileName, File file){
		this.fileName = fileName;
		this.icon = icon;
		this.file = file;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setFileName(String file){
		this.fileName = file;
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public File getFile(){
		return this.file;
	}

}
