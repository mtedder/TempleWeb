/**
 * 
 */
package com.gc.temple.dto;

/**
 * @author Maurice Tedder
 * Data Transfer Object for Temple Patron database table
 */
public class Patron {
	private int id;
	private int keyId;
	private String firstName;
	private String lastName;
	private String imageName;
	private boolean member;
	//
	private ContactInfo contactInfo;
		
	/**
	 * Initialization constructor. 
	 * @param firstName
	 * @param lastName
	 * @param member
	 * @param contactInfo
	 */
	public Patron(String firstName, String lastName, boolean member, ContactInfo contactInfo) {				
		this.firstName = firstName;
		this.lastName = lastName;
		this.member = member;
		this.contactInfo = contactInfo;
	}
	
	public Patron(String firstName, String lastName, boolean member, String imageName, ContactInfo contactInfo) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.member = member;
		this.imageName = imageName;
		this.contactInfo = contactInfo;
	}

	/**
	 * @return the keyId
	 */
	public int getKeyId() {
		return keyId;
	}

	/**
	 * @param keyId the keyId to set
	 */
	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the member
	 */
	public boolean isMember() {
		return member;
	}
	/**
	 * @param member the member to set
	 */
	public void setMember(boolean member) {
		this.member = member;
	}
	/**
	 * @return the contactInfo
	 */
	public ContactInfo getContactInfo() {
		return contactInfo;
	}
	/**
	 * @param contactInfo the contactInfo to set
	 */
	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	/* 
	 * Create a formated output of data contained in this object
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {		
		String value = "First Name:\t" + firstName + "\n"
				+ "Last Name:\t" + lastName + "\n"
				+ "Member:\t\t" + member + "\n"
				+ "Address:\t" + contactInfo.getAddress() + "\n"
				+ "City:\t\t" + contactInfo.getCity() + "\n"
				+ "State:\t\t" + contactInfo.getState() + "\n"
				+ "Zip:\t\t" + contactInfo.getZip() + "\n"
				+ "Phone Number:\t" + contactInfo.getPhoneNumber() + "\n";
		return value;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imageName;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imageName = imagePath;
	}

	
	
}
