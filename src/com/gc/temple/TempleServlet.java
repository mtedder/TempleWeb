package com.gc.temple;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gc.temple.dao.TempleDao;
import com.gc.temple.dto.ContactInfo;
import com.gc.temple.dto.Patron;

/**
 * Servlet implementation class TempleServlet
 */
@WebServlet("/TempleServlet")
public class TempleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TempleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get list of patrons by name of phone
		//Get form parameters
		String fullname = request.getParameter("fullname");
		String phonenumber = request.getParameter("phonenumber");
		
		TempleDao dao = new TempleDao();
		List<Patron> result = null;
		//Do different search depending on user name of phone number
		if(fullname.isEmpty() && phonenumber.isEmpty()){//do phone number search			
			response.getWriter().append("Invalid input! Input full name (first and last name!)");
			return;
		}else if(phonenumber.isEmpty()){//do name search
			String[] name = fullname.split(" ");	
			if(name.length <= 1){				
				response.getWriter().append("Invalid input! Input full name (first and last name!)");
				return;
			}else{
				System.out.println("test1:" + name.length);
				if(name.length > 2){//handle middle names etc...
					name[1] = name[1].concat(" ").concat(name[2]);
				}
				System.out.println("test2:" + name[0] + "," + name[1] );
				result = dao.getPatronsByName(name[0], name[1]);
			}			
		}else {//do phone number search
			System.out.println("Phonenumber:" + phonenumber);
			result = dao.getPatronsByPhone(phonenumber);
			System.out.println("Name:" + result.get(0).getFirstName());
		}
		StringBuilder returnString = new StringBuilder();
		
//		for(Patron patron:result){
//			returnString.append(patron.toString().replaceAll("\n", "<br>") + "<br>");
//		}		
//		response.getWriter().append(returnString.toString());//manually write raw html
//		request.setAttribute("result", returnString.toString());//return formated string
		request.setAttribute("result", result);//result arraylist of patrons
		getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		//Get form parameters
		String firstname = request.getParameter("fisrtname");
		String lastname = request.getParameter("lastname");
		String address = request.getParameter("address");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String zip = request.getParameter("zip");
		String phonenumber = request.getParameter("phonenumber");
		String membership = request.getParameter("membership");
		
		ContactInfo contactInfo = new ContactInfo(address, city, state, zip, phonenumber);		
		Patron patron = new Patron(firstname, lastname, Boolean.valueOf(membership), contactInfo);
		TempleDao dao = new TempleDao();
		dao.insertPatron(patron);
		
		response.getWriter().append("Patron successfully inserted: ").append(request.getContextPath());
	}

}
