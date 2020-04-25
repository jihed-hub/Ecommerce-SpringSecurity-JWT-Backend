package com.example.demo.Models;

import java.util.ArrayList;
import java.util.List;

public class ViewOrder {
	private int orderId;
	private String orderBy;
	private String orderStatus;
	private List<Cart> cartList = new ArrayList<>();
	public ViewOrder() {}
	public ViewOrder(int orderId, String orderBy, String orderStatus, List<Cart> cartList) {
		this.orderId = orderId;
		this.orderBy = orderBy;
		this.orderStatus = orderStatus;
		this.cartList = cartList;}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public List<Cart> getCartList() {
		return cartList;
	}
	public void setCartList(List<Cart> cartList) {
		this.cartList = cartList;
	}
}
