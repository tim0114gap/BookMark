package kr.ac.sunmooon.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java_cup.internal_error;
import kr.ac.sunmooon.client.MyService;
import kr.ac.sunmooon.shared.bookMarkData;
import kr.ac.sunmooon.shared.oldBookmarkData;
import kr.ac.sunmooon.shared.recommendBookmark;

public class MyServiceImpl extends RemoteServiceServlet implements MyService{

	@Override
	public Vector<bookMarkData> getSql(int time) {
		// TODO Auto-generated method stub
		Vector<bookMarkData> strSql = new Vector<bookMarkData>();
		try {
			String url = "jdbc:mysql://localhost:3306/bookmark_db?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String password = "Tim19960114!";

			Connection con = DriverManager.getConnection(url, user, password);

			Statement stmt = con.createStatement();

			//String sql = "SELECT * FROM bookmark_db.userbookmark";
			String sql = "SELECT u.id, u.name, a.url as url, CAST(SUBSTRING(pub_data,1,4) as DECIMAL(10)) as pub_data, u.times, a.category as category FROM bookmark_db.userbookmark as u " + 
					"inner join bookmark_db.allwebsiteinfo as a " + 
					"on u.name = a.name";
			
			sql += " WHERE pub_data >= "+time;
			sql += " ORDER BY times DESC;";
			stmt.execute(sql);

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				bookMarkData bk = new bookMarkData();
				bk.setId(rs.getInt("id"));
				bk.setName(rs.getString("name"));
				bk.setUrl(rs.getString("url"));
				bk.setDate(rs.getString("pub_data"));
				bk.setTimes(rs.getInt("times"));
				bk.setCategory(rs.getString("category"));
				
				strSql.add(bk);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return strSql;
	}

	@Override
	public void setSql(String time, String id) {
		// TODO Auto-generated method stub
		try {

			String url = "jdbc:mysql://localhost:3306/bookmark_db?useSSL=false";
			String user = "root";
			String password = "Tim19960114!";

			Connection con = DriverManager.getConnection(url, user, password);

			Statement stmt = con.createStatement();

			String sql = "UPDATE bookmark_db.userbookmark "
					+ "SET times = "+time+" WHERE id = "+id+";";

			stmt.execute(sql);
			
			sql = "UPDATE bookmark_db.userbookmark SET pub_data = current_date() WHERE (id = "+ id +");";
			stmt.execute(sql);

			stmt.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}

	@Override
	public Vector<recommendBookmark> popRecommendSql(String name) {
		// TODO Auto-generated method stub
		Vector<recommendBookmark> strSql = new Vector<recommendBookmark>();
		try {

			String url = "jdbc:mysql://localhost:3306/bookmark_db?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String password = "Tim19960114!";

			Connection con = DriverManager.getConnection(url, user, password);

			Statement stmt = con.createStatement();

			//String sql = "SELECT * FROM bookmark_db.userbookmark";
			String sql = "SELECT name,url FROM bookmark_db.allwebsiteinfo " + 
					"where category like (SELECT category FROM bookmark_db.allwebsiteinfo " + 
					"where name like \""+ name +"\")  and name not like \"" + name + "\" " + 
					"order by favorite ASC;";

			stmt.execute(sql);

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				recommendBookmark rbook = new recommendBookmark();
				rbook.setName(rs.getString("name"));
				rbook.setUrl(rs.getString("url"));
				strSql.add(rbook);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return strSql;
	}

	@Override
	public Vector<oldBookmarkData> getOldBookMark(String time) {
		// TODO Auto-generated method stub
		Vector<oldBookmarkData> strSql = new Vector<oldBookmarkData>();
		try {
			String url = "jdbc:mysql://localhost:3306/bookmark_db?allowPublicKeyRetrieval=true&useSSL=false";
			String user = "root";
			String password = "Tim19960114!";

			Connection con = DriverManager.getConnection(url, user, password);

			Statement stmt = con.createStatement();

			//String sql = "SELECT * FROM bookmark_db.userbookmark";
			String sql = "SELECT u.id, u.name, a.url as url, CAST(SUBSTRING(pub_data,1,4) as DECIMAL(10)) as pub_data, u.times, a.category as category FROM bookmark_db.userbookmark as u " + 
					"inner join bookmark_db.allwebsiteinfo as a " + 
					"on u.name = a.name";
			
			sql += " WHERE pub_data < "+time;
			sql += " ORDER BY times DESC;";
			stmt.execute(sql);

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				oldBookmarkData old = new oldBookmarkData();
				old.setId(rs.getInt("id"));
				old.setName(rs.getString("name"));
				old.setUrl(rs.getString("url"));
				old.setDate(rs.getString("pub_data"));
				old.setTimes(rs.getInt("times"));
				old.setCategory(rs.getString("category"));
				
				strSql.add(old);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return strSql;
	}

}
