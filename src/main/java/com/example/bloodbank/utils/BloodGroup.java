package com.example.bloodbank.utils;

public enum BloodGroup {

	O_POS("O+"), O_NEG("O-"), A_POS("A+"), A_NEG("A-"), B_POS("B+"), B_NEG("B-"), AB_POS("AB+"), AB_NEG("AB-");

	private String bloodGroup;

	private BloodGroup(String group) {
		bloodGroup = group;
	}

	public String toString() {
		return this.bloodGroup;
	}
}
