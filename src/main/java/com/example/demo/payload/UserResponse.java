package com.example.demo.payload;

import java.util.HashMap;

import com.example.demo.Models.Address;
import com.example.demo.Models.User;

public class UserResponse {
	private String message;
	private HashMap<String, String> map;
	public UserResponse(String message, HashMap<String, String> map) {
		this.message = message;
		this.map = map;
	}
	public UserResponse(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HashMap<String, String> getMap() {
		return map;
	}
	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}
	
}