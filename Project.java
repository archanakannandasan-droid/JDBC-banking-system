package Projects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.Scanner;

public class Project {
	 public static void main(String[] args) throws Exception {

		  Class.forName("com.mysql.cj.jdbc.Driver");

		  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee?user=root&password=root");
		  System.out.println("<---------------------  Login --------------------------------->");
		  Scanner sc = new Scanner(System.in);
		  System.out.println("Pls enter the acc_number:");
		  int acc_no = sc.nextInt();
		  System.out.println("Pls enter the Pin:");
		  int pin = sc.nextInt();

		  String q1 = "select * from employees where acc_no = ? and pin = ?  ";

		  PreparedStatement statement1 = connection.prepareStatement(q1);
		  statement1.setInt(1, acc_no);
		  statement1.setInt(2, pin);

		  System.out.println("Pls wait you get Balance !!!!!!");

		  Thread.sleep(10000);

		  System.out.println("Thank You we are ready to displayy u r balance !!!!!!");
		  Thread.sleep(10000);

		  ResultSet res = statement1.executeQuery();

		  res.next();

		  String name = res.getString(2);
		  int balance = res.getInt(4);

		  System.out.println("welcome : " + name);
		  System.out.println("Available balance is: " + balance);
		  System.out.println("<--------------------Transfer Module-------------------------------->");

		  connection.setAutoCommit(false);

		  Savepoint s = connection.setSavepoint();

		  System.out.println("Pls enter the Transfer Amount: ");
		  int t_amount = sc.nextInt();
		  System.out.println("Pls enter the destination acc_Num ");
		  int r_accNum = sc.nextInt();

		  String q2 = "update employees set balance = balance - ? where acc_no = ? ";

		  PreparedStatement statement2 = connection.prepareStatement(q2);

		  statement2.setInt(1, t_amount);
		  statement2.setInt(2, acc_no);
		  statement2.executeUpdate();

		  System.out.println("<---------------Incoming Credit Request---------------------->");

		  System.out.println(" Hi " + name + "  wants to Send Money " + t_amount + " account No : " + acc_no);
		  System.out.println("Pls press Yes to Receive : ");
		  System.out.println("Pls press No to Reject!!");

		  String choice = sc.next();

		  if (choice.equalsIgnoreCase("yes")) {

		   String q3 = "update employees set balance = balance + ? where acc_no = ?";

		   PreparedStatement statement3 = connection.prepareStatement(q3);
		   statement3.setInt(1, t_amount);
		   statement3.setInt(2, r_accNum);
		   statement3.executeUpdate();

		   String q4 = "select * from employees where acc_no= ? ";

		   PreparedStatement statement4 = connection.prepareStatement(q4);

		   statement4.setInt(1, r_accNum);
		   ResultSet res1 = statement4.executeQuery();

		   res1.next();

		   System.out.println("Updated Balance " + res1.getInt(4));

		  } else {

		   connection.rollback(s);
		   String q4 = "select * from emloyees where acc_no= ? ";

		   PreparedStatement statement4 = connection.prepareStatement(q4);

		   statement4.setInt(1, r_accNum);
		   ResultSet res1 = statement4.executeQuery();

		   res1.next();

		   System.out.println("Existing Balance " + res1.getInt(4));
		  }

		  connection.commit();

		 }
		}



