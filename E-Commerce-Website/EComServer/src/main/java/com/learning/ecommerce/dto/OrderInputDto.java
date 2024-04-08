package com.learning.ecommerce.dto;

import java.util.List;


public class OrderInputDto {

    private String fullName;
    private String fullAddress;
    private String contactNumber;
    private String alternateContactNumber;
    private int pincode;
    private List<OrderProductQuantityDto> orderProductQuantityDtoList;

    public OrderInputDto(String fullName, String fullAddress, String contactNumber, String alternateContactNumber, int pincode, List<OrderProductQuantityDto> orderProductQuantityDtoList) {
        this.fullName = fullName;
        this.fullAddress = fullAddress;
        this.contactNumber = contactNumber;
        this.alternateContactNumber = alternateContactNumber;
        this.pincode=pincode;
        this.orderProductQuantityDtoList = orderProductQuantityDtoList;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAlternateContactNumber() {
        return alternateContactNumber;
    }

    public void setAlternateContactNumber(String alternateContactNumber) {
        this.alternateContactNumber = alternateContactNumber;
    }

    public int getPincode() {
        return pincode;
    }
    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public List<OrderProductQuantityDto> getOrderProductQuantityList() {
        return orderProductQuantityDtoList;
    }

    public void setOrderProductQuantityList(List<OrderProductQuantityDto> orderProductQuantityDtoList) {
        this.orderProductQuantityDtoList = orderProductQuantityDtoList;
    }
}
