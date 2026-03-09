package Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Scanner;

public class Banking {
public static void main(String[] args) {
	
	try{
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/banking?user=root&password=root");
		
		Statement stmt= con.createStatement();
		String q="create table account(acc_no INT PRIMARY KEY, name Varchar(50), pin INT, balance INT)";
		stmt.executeUpdate(q);
		
		String q1="insert into account (acc_no,name,pin, balance) values(101,'Priya',111,5000)";
		String q2="insert into account (acc_no,name,pin, balance) values(102,'Archana',222,1000)";
		String q3="insert into account (acc_no,name,pin, balance) values(103,'Saii',333,4000)";
		
		stmt.addBatch(q1);
		stmt.addBatch(q2);
		stmt.addBatch(q3);
		stmt.executeBatch();
	
		System.out.println("================login portal===========");
		Scanner sc=new Scanner(System.in);
		System.out.println("please enter the Account No : ");
		int acc_no=sc.nextInt();

		System.out.println("please enter the Pin No : ");
	
		int pin_no=sc.nextInt();
		
		
		String q4="select * from account where acc_no=? and pin=?";
		PreparedStatement pstmt1 = con.prepareStatement(q4);
		pstmt1.setInt(1,acc_no);
		pstmt1.setInt(2, pin_no);
		ResultSet res = pstmt1.executeQuery();
		res.next();
		String name=res.getString(2);
		int balance=res.getInt(4);
		
		System.out.println("Hii "+ name +" your avaliable balance is " + balance );
		
		
		
		System.out.println("=============Transfer module===============");
		con.setAutoCommit(false);
		Savepoint s = con.setSavepoint();
		System.out.println("enter the amount to be transfered");
		int amount=sc.nextInt();
		System.out.println("enter the account number to be transfered");
		int racc_no=sc.nextInt();
		
		String q5="update account set balance=balance-? where acc_no=?";
		
		PreparedStatement pstmt2 = con.prepareStatement(q5);
		pstmt2.setInt(1, amount);
		pstmt2.setInt(2, acc_no);
		pstmt2.executeUpdate();
		
		
		System.out.println("================incoming credit=============");
		
		System.out.println("Hii" + name + "wants to send the money to you of " +amount + "from the account No of" + acc_no);
		System.out.println("press yes to accept");
		System.out.println("press No to reject");
		
		String choice=sc.next();
		if(choice.equalsIgnoreCase("yes"))
		{
			String q6="update account set balance=balance + ? where acc_no=?";
			
			PreparedStatement pstmt3 = con.prepareStatement(q6);
			pstmt3.setInt(1, amount);
			pstmt3.setInt(2, racc_no);
			
			pstmt3.executeUpdate();
			
			String q7="select * from account where acc_no=?";
			PreparedStatement pstmt4 = con.prepareStatement(q7);
			pstmt4.setInt(1, racc_no);
			
			ResultSet res2 = pstmt4.executeQuery();
		res2.next();
		System.out.println("the updated balance is" +res2.getInt(4));
		
			
		}
		else {
		con.rollback(s);

			String q7="select * from account where acc_no=?";
			PreparedStatement pstmt4 = con.prepareStatement(q7);
			pstmt4.setInt(1, racc_no);
			
			ResultSet res2 = pstmt4.executeQuery();
			res2.next();
		System.out.println("the existing  balance is" +res2.getInt(4));
			
		}
		
		con.commit();
	}

catch(ClassNotFoundException e)
{
	e.printStackTrace();
}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
}
}
