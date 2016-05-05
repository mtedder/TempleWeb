/**
 * 
 */
package com.gc.temple.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.gc.temple.dto.ContactInfo;
import com.gc.temple.dto.Patron;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * @author Maurice Tedder
 * Data Access Object
 */
public class TempleDao {
	private Connection c;
	//Declare SQL statement String for ContactInfo table
	private String sqlContactInfoInsert = "INSERT INTO templedb.contactinfo (address,city,state,zip, phonenum) VALUES (?, ?, ?, ?,?)";
	private String sqlContactInfoId = "SELECT id FROM templedb.contactinfo WHERE address=?";
	private String sqlPatronInsert = "INSERT INTO templedb.patron (keyid,firstname,lastname,member) VALUES (?, ?, ?, ?)";
	private String sqlPatronUpdate = "UPDATE templedb.patron SET keyid=?, member=? WHERE firstname=? and lastname=?";
	private String sqlContactInfoUpdate = "UPDATE templedb.contactinfo SET address=? , city=? ,  state=? , zip=? WHERE phonenum =?";
	//Declare SQL statement String for ContactInfo table
	private String sqlSelectByPhoneNumber = "SELECT keyid, firstname, lastname, member, imagename, address, city,state, zip, phonenum FROM templedb.patron as p, templedb.contactinfo as c WHERE c.id = p.keyid and c.phonenum = ?";
	private String sqlSelectByName = "SELECT keyid, firstname, lastname, member,address, city,state, zip, phonenum, imagename FROM templedb.patron as p, templedb.contactinfo as c WHERE c.id = p.keyid and p.firstname = ? and p.lastname=?";
	/*
	 * Insert new patron into the Patron table
	 */
	public void insertPatron(Patron patron) {
		//get connection to DB
		c = getConnection();
		try {
			//Create prepared statement for ContactInfo table
			PreparedStatement stmtContactInsert = c.prepareStatement(sqlContactInfoInsert);
			//Set prepared statement values
			stmtContactInsert.setString(1, patron.getContactInfo().getAddress());
			stmtContactInsert.setString(2, patron.getContactInfo().getCity());
			stmtContactInsert.setString(3, patron.getContactInfo().getState());
			stmtContactInsert.setString(4, patron.getContactInfo().getZip());
			stmtContactInsert.setString(5, patron.getContactInfo().getPhoneNumber());						

			//Create update prepared statement for ContactInfo table
			PreparedStatement stmtContactUpdate = c.prepareStatement(sqlContactInfoUpdate);
			stmtContactUpdate.setString(1, patron.getContactInfo().getAddress());
			stmtContactUpdate.setString(2, patron.getContactInfo().getCity());
			stmtContactUpdate.setString(3, patron.getContactInfo().getState());
			stmtContactUpdate.setString(4, patron.getContactInfo().getZip());
			stmtContactUpdate.setString(5, patron.getContactInfo().getPhoneNumber());
			
			//Create prepared statement for ContactInfo table
			PreparedStatement stmtContactInfoId = c.prepareStatement(sqlContactInfoId);
			stmtContactInfoId.setString(1, patron.getContactInfo().getAddress());
			
			int rowCount = stmtContactUpdate.executeUpdate();//try update contact table
			System.out.println("rowCount:" + rowCount);
			if(rowCount == 0){//update failed (record does not exist)-try insert
				stmtContactInsert.executeUpdate();//do insert
				//get row id for the previously inserted record
				ResultSet resultSet = stmtContactInfoId.executeQuery();
				if(resultSet != null){//we have data - proceed with insert into patron table
					resultSet.next();
					int keyId = resultSet.getInt(1);
					patron.setKeyId(keyId);
					//insert patron into patron table
					PreparedStatement stmt3 = c.prepareStatement(sqlPatronInsert);
					stmt3.setInt(1, keyId);
					stmt3.setString(2, patron.getFirstName());
					stmt3.setString(3,patron.getLastName());
					stmt3.setInt(4, (patron.isMember())?1:0);
					stmt3.execute();				
				}
			}else{//update succeeded-continue with insert into patron table
				System.out.println("Need to insert new person for this phonenumber");
				//get row id for the previously inserted contact table record				
				ResultSet resultSet = stmtContactInfoId.executeQuery();
				if(resultSet != null){//we have data
					resultSet.next();
					int keyId = resultSet.getInt(1);
					patron.setKeyId(keyId);
					//try update first,insert patron into patron table if does not already exist
					PreparedStatement stmtUpdatePatron = c.prepareStatement(sqlPatronUpdate);
					stmtUpdatePatron.setInt(1, keyId);
					stmtUpdatePatron.setInt(2, (patron.isMember())?1:0);
					stmtUpdatePatron.setString(3, patron.getFirstName());
					stmtUpdatePatron.setString(4, patron.getLastName());
					int updateCount = stmtUpdatePatron.executeUpdate();
					if(updateCount == 0){//update failed (record does not exist)-try insert						
						PreparedStatement stmt3 = c.prepareStatement(sqlPatronInsert);
						stmt3.setInt(1, keyId);
						stmt3.setString(2, patron.getFirstName());
						stmt3.setString(3,patron.getLastName());
						stmt3.setInt(4, (patron.isMember())?1:0);
						stmt3.execute();//do insert	
					}					
				}
			}
		}catch (MySQLIntegrityConstraintViolationException e){
			int erroCode = e.getErrorCode();
			if(erroCode == 1062){
				//Duplicate record found-handle it here
				//Not implementing this technique for now
			}	
			e.printStackTrace();
		}catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			closeConnection();
		}			
	}
	
	/*
	 * Get a list of patron objects by phonenumber
	 * search criteria.
	 */
	public List<Patron> getPatronsByPhone(String phoneNumber){
		ArrayList<Patron> returnList = new ArrayList<Patron>();
		//get connection to DB
		c = getConnection();
		//Create prepared statement for select statment
		try {
			PreparedStatement stmt1 = c.prepareStatement(sqlSelectByPhoneNumber);
			stmt1.setString(1, phoneNumber);
			ResultSet resultSet = stmt1.executeQuery();			
			while (resultSet.next()) {
				
				int keyid = resultSet.getInt("keyid");
				String firstName = resultSet.getString("firstname");				
				String lastname = resultSet.getString("lastname");
				int member = resultSet.getInt("member");
				String address = resultSet.getString("address");				
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				String zip = resultSet.getString("zip");
				String phonenum = resultSet.getString("phonenum");
				String imagename = resultSet.getString("imagename");
				//New ContactInfo object
				ContactInfo contactInfo = new ContactInfo(address,city,state,zip,phonenum);
				//New Patron object
				System.out.println("HEY!" + firstName);
				Patron patron = new Patron(firstName, lastname, ((member == 1)?true:false), imagename, contactInfo);
				patron.setKeyId(keyid);
				//Add to array list
				returnList.add(patron);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeConnection();
		}
		System.out.println("HEY2!" + returnList.get(0).getFirstName());
		return returnList;
	}
	
	/*
	 * Get patrons by first name and last name
	 */
	public List<Patron> getPatronsByName(String firstName, String lastName) {
		ArrayList<Patron> returnList = new ArrayList<Patron>();
		//get connection to DB
		c = getConnection();
		//Create prepared statement for select statment
		try {
			PreparedStatement stmt1 = c.prepareStatement(sqlSelectByName);
			stmt1.setString(1, firstName);
			stmt1.setString(2, lastName);
			ResultSet resultSet = stmt1.executeQuery();
			while (resultSet.next()) {				
				int keyid = resultSet.getInt("keyid");
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				int member = resultSet.getInt("member");
				String address = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				String zip = resultSet.getString("zip");
				String phonenum = resultSet.getString("phonenum");
				String imagename = resultSet.getString("imagename");
				//New ContactInfo object
				ContactInfo contactInfo = new ContactInfo(address,city,state,zip,phonenum);
				//New Patron object
				Patron patron = new Patron(firstName, lastname, ((member == 1)?true:false), imagename, contactInfo);
				patron.setKeyId(keyid);
				//Add to array list
				returnList.add(patron);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeConnection();
		}
		return returnList;
	}
	/*
	 * Get DB connection to temple DB
	 */
	private Connection getConnection() {
		try {
			 Class.forName("com.mysql.jdbc.Driver");
//			 Connection c;
			 String connectionString = "jdbc:mysql://localhost:3306";			 
			 return DriverManager.getConnection(connectionString, "root","admin");

//			Context ctx = new InitialContext();
//			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/dbb");
//			Connection c = ds.getConnection();
//			return c;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Close Db connection if it exist.
	 */
	private void closeConnection() {
		if(c != null){
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
