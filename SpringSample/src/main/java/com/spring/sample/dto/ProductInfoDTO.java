package com.spring.sample.dto;

public class ProductInfoDTO {
	private String code;
	private String name;
	private int quantity;
	private int price;
	private int discount;
	private String description;
	
	public ProductInfoDTO() {
	}
	
	public ProductInfoDTO(String code, String name, int quantity, int price, int discount, String description) {
		this.code = code;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.discount = discount;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ProductInfoDTO {code [" + this.code + "] name [" + this.name
				+ "] quantity [" + this.quantity + "] price [" + this.price
				+ "] discount [" + this.discount + "] description [" + this.description + "]}";
	}
}
