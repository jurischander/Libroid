package com.schander.libroid;

import java.io.File;

public class BookEntry {
	
	private String author;
	private String title;
	private String annotation;
	private String pathToImage = "";
	private File file;
	private boolean transferredToDevice = false;

	public BookEntry(File file, String image, String author, String title, String annotation){
		this.file = file;
		this.author = author;
		this.annotation = annotation;
		this.title = title;
		this.pathToImage = image;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setAnnotation(String ann){
		this.annotation = ann;
	}
	
	public String getAnnotation(){
		return this.annotation;
	}
	
	public void setPathToImage(String image){
		this.pathToImage = image;
	}
	
	public String getPathToImage(){
		return this.pathToImage;
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public File getFile(){
		return this.file;
	}
	
	public void setTransferred(boolean tr){
		this.transferredToDevice = tr;
	}
	
	public boolean getTransferred(){
		return this.transferredToDevice;
	}
	
}
