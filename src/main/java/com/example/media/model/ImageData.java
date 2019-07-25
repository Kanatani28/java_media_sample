package com.example.media.model;

public class ImageData extends DataModelBase {
    private int id;
    private String fileName;
    private byte[] data;
    private String imageToken;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getImageToken() {
		return imageToken;
	}
	public void setImageToken(String imageToken) {
		this.imageToken = imageToken;
	}
    
}
