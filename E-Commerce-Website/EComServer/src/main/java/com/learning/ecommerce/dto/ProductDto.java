package com.learning.ecommerce.dto;


//In summary, both classes have distinct purposes: Product is for database mapping and persistence logic, while
//ProductDto is for representing data to be transferred between the server and the client. Skipping
//the ProductDto class can lead to tighter coupling between your backend and frontend, making your
//application less flexible and maintainable as changes in the database structure can directly affect
//your client-side code. Using DTOs helps decouple these concerns and provides a cleaner API for your
//clients.

import java.util.ArrayList;
import java.util.List;


public class ProductDto {
	private int pid;

	private String pname;

	private String brand;

	private String description;

	private double price;

	private String imageUrl;

	private List<ServiceabilityDto> serviceability = new ArrayList<>();

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<ServiceabilityDto> getServiceability() {
		return serviceability;
	}

	public void setServiceability(List<ServiceabilityDto> serviceability) {
		this.serviceability = serviceability;
	}
	
	
}
