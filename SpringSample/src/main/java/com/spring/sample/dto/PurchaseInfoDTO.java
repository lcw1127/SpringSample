package com.spring.sample.dto;

public class PurchaseInfoDTO {
	private String id;
	private String productName;
	private int quantity;

	public PurchaseInfoDTO() {
	}
	
	public PurchaseInfoDTO(String id, String productName, int quantity) {
		this.id = id;
		this.productName = productName;
		this.quantity = quantity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "PurchaseInfoDTO {id [" + this.id
				+ "] productName [" + this.productName + "] quantity [" + this.quantity + "]}";
	}
}
