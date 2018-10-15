package com.sp.giftshop;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class GiftShop {
	private long goodsCode;
	private String goodsName;
	private int price;
	private int state; // 0:비활성 1:활성
	private long gubunCode;
	private String gubunName;
	private long parentCode;
	private int quantity;
	private int likeCount;
	private String content;
	private String thumbnail;
	private List<Map<String, Object>> fileList;
	private List<String> saveFileName;
	private List<String> orignalFileName;
	private List<MultipartFile> upload;

	public long getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(long goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getGubunCode() {
		return gubunCode;
	}

	public void setGubunCode(long gubunCode) {
		this.gubunCode = gubunCode;
	}

	public String getGubunName() {
		return gubunName;
	}

	public void setGubunName(String gubunName) {
		this.gubunName = gubunName;
	}

	public long getParentCode() {
		return parentCode;
	}

	public void setParentCode(long parentCode) {
		this.parentCode = parentCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public List<Map<String, Object>> getFileList() {
		return fileList;
	}

	public void setFileList(List<Map<String, Object>> fileList) {
		this.fileList = fileList;
	}

	public List<String> getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(List<String> saveFileName) {
		this.saveFileName = saveFileName;
	}

	public List<String> getOrignalFileName() {
		return orignalFileName;
	}

	public void setOrignalFileName(List<String> orignalFileName) {
		this.orignalFileName = orignalFileName;
	}

	public List<MultipartFile> getUpload() {
		return upload;
	}

	public void setUpload(List<MultipartFile> upload) {
		this.upload = upload;
	}

}
