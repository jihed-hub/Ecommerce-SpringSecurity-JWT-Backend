package com.example.demo.payload;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.Models.ViewOrder;


public class ViewOrderResponse {

	private String message;
	private List<ViewOrder> orderList=new ArrayList<>();
	public ViewOrderResponse() {}
	public ViewOrderResponse(String message,List<ViewOrder> orderList) {
		this.message = message;
		this.orderList = orderList;}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<ViewOrder> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<ViewOrder> orderList) {
		this.orderList = orderList;
	}
}
