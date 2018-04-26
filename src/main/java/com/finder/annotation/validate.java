package com.finder.annotation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.persistence.Table;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Table(name = "finder")
public class validate extends HttpServlet {
	private static final long serialVersionUID = -734503860925086969L;
	
	@RequestMapping(value = {"/data/save"}, method = RequestMethod.GET)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    try {
	        String connectionURL = "jdbc:mysql://localhost:3306/test?useSSL=false"; 
	        Connection connection = null;
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        connection = (Connection) DriverManager.getConnection(connectionURL, "root", "123456");
	        String recepti = request.getParameter("recepti");
	        PreparedStatement ps = (PreparedStatement) connection.prepareStatement("select recepti from finder where recepti=?");
	        //ps.setString(1,request.getParameter("recepti"));
	        ps.setString(1,recepti);
	        ResultSet rs = ps.executeQuery();
	        
	        if (!rs.next()) {
	            out.println(""+recepti+" is avaliable");
	        }
	        else{
	        out.println("<font color=red><b>"+recepti+"</b> is already in use</font>");
	        }
	        out.println();

	    } catch (Exception ex) {

	        out.println("Error ->" + ex.getMessage());

	    } finally {
	        out.close();
	    }
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		doPost(request, response);
    }
}
