package com.example.demo.configs;

import com.example.demo.Models.Product;

public class Validator {
	
	public static boolean isAlphaNumerical(String input) {
		if (input != null && input != "") {
			if (input.matches("[a-zA-Z0-9]*")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNumerical(String input) {
		if (input != null && input != "") {
			if (input.matches("[0-9]*")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isImageFile(String input) {
		if (input != null && input != "") {
			input = input.toLowerCase();
			if (input.endsWith(".png") || input.endsWith(".jpg") || input.endsWith(".jpeg") || input.endsWith(".gif")) {
				return true;
			}
		}
		return false;
	}
	
	public static String removeSpaces(String input) {
		String filterInput = "";
		if (input != null && input != "") {
			filterInput = input.replace(" ", "");
		}
		return filterInput;
	}
	
	public static boolean isProductEmpty(Product prod) {

		if (prod.getProductname() == null || prod.getProductname() == "") {
			return true;
		}
		if (prod.getDescription() == null || prod.getDescription() == "") {
			return true;
		}
		if (prod.getPrice() == 0) {
			return true;
		}
		if (prod.getQuantity() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isStringEmpty(String input) {
		if (input == null || input.equals("")) {
			return true;
		}
		return false;
	}

}
