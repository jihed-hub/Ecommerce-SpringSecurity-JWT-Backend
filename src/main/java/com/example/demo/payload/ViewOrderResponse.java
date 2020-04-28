package com.example.demo.payload;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.Models.ViewOrder;


public class ViewOrderResponse {

	private String message;
	private List<ViewOrder> orderlist=new ArrayList<>();
	public ViewOrderResponse() {}
	public ViewOrderResponse(String message, List<ViewOrder> orderlist) {
		super();
		this.message = message;
		this.orderlist = orderlist;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<ViewOrder> getOrderlist() {
		return orderlist;
	}
	public void setOrderlist(List<ViewOrder> orderlist) {
		this.orderlist = orderlist;
	}
	
}
