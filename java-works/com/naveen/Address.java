package com.naveen;

public final class Address {

	private int houseNo; 
    private String street;
    private String city;
    private String zip;
    
    public Address() {}
    
	public Address(int houseNo, String street, String city, String zip) {
		super();
		this.houseNo = houseNo;
		this.street = street;
		this.city = city;
		this.zip = zip;
	}

	public Address(String street, String city, String zip) {
		super();
		this.street = street;
		this.city = city;
		this.zip = zip;
	}
	public int getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(int houseNo) {
		this.houseNo = houseNo;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "Address [houseNo=" + houseNo + ", street=" + street + ", city=" + city + ", zip=" + zip + "]";
	}
    
	
    
    
}