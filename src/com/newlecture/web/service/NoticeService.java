/**
 * 
 */
package com.newlecture.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;

public class NoticeService {
	
	public int removeNoticeAll(int[] ids) {
		
		return 0;
	}
	public int pubNoticeAll(int[] oids, int[] cids){
		
		return 0;
	}
	public int pubNoticeAll(List<String> oids, List<String> cids){

		String oidsCSV = String.join(",", oids);
		String cidsCSV = String.join(",", cids);
		
//		System.out.println(oidsCSV);
//		System.out.println(cidsCSV);
		
		return pubNoticeAll(oidsCSV, cidsCSV);
	}	
	
	
	public int pubNoticeAll(String oidsCSV, String cidsCSV){
		
		int result = 0;
		
		
		String sqlOpen = String.format("UPDATE NOTICE1 SET PUB=1 WHERE ID IN (%s)", oidsCSV);
		String sqlClose = String.format("UPDATE NOTICE1 SET PUB=0 WHERE ID IN (%s)", cidsCSV);
		
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			Statement stOpen = con.createStatement();
			result += stOpen.executeUpdate(sqlOpen);
			
			Statement stClose = con.createStatement();
			result += stOpen.executeUpdate(sqlClose);
			

				stOpen.close();
				stClose.close();
				con.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}		
	
	
	public int insertNotice(Notice notice) {
		
		int result = 0;
		
		
		String sql = "INSERT INTO NOTICE1 (ID, TITLE, CONTENT, WRITER_ID, PUB, FILES) VALUES (jindanseq.nextval, ?,?,?,?,?) ";
		

		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		
		try {

//			System.out.println("testetseetset::::");
//			System.out.println(notice.getFiles());
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, notice.getTitle());
			st.setString(2, notice.getContent());
			st.setString(3, notice.getWriterId());
			st.setBoolean(4, notice.getPub());
			st.setString(5, notice.getFiles());
			
			result = st.executeUpdate();

				st.close();
				con.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}		
	
	public int deleteNotice(int id){

		return 0;
	}
	public int updateNotice(Notice notice){

		return 0;
	}
	List<Notice> getNoticeNewestList(){
		
		return null;
	}
	
	public List<NoticeView> getNoticeList(){
		return getNoticeList("title", "", 1);
	}
	
	public List<NoticeView> getNoticeList(int page){
		return getNoticeList("title", "", page);
	}

	public List<NoticeView> getNoticePubList(String field, String query, int page){
		List<NoticeView> list = new ArrayList<>();
		
		String sql="SELECT   Z.* \r\n" + 
				"  FROM   (SELECT   ROWNUM AS NUM, A.*\r\n" + 
				"            FROM   (  SELECT   *\r\n" + 
				"                        FROM   NOTICE_VIEW_SERVLET WHERE "+field+" LIKE ? \r\n" + 
				"                    ORDER BY   REGDATE DESC) A) Z    \r\n" + 
				" WHERE PUB=1 AND NUM BETWEEN ? AND ?   order by id ";
		
		
		// 1, 11, 21, 31 -> 1+(page-1)*10
		// 10,20, 30, 40 -> page*10

		
		String url = "jdbc:oracle:thin:@localhost:1521:XE";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			
			//System.out.println(sql);
			
			PreparedStatement st = con.prepareStatement(sql);
			//System.out.println(sql);
			st.setString(1, "%" + query + "%");
			st.setInt(2, 1+(page-1)*10);
			st.setInt(3, page*10);
			ResultSet rs= st.executeQuery();

			 while(rs.next()){ 
			 	int id = rs.getInt("ID");
			 	String title=rs.getString("TITLE");
				String writerId=rs.getString("WRITER_ID");
				Date regdate=rs.getDate("REGDATE"); 
				String hit=rs.getString("HIT");
				String files=rs.getString("FILES");
				//String content=rs.getString("CONTENT"); 
				boolean pub = rs.getBoolean("PUB");
				int cmtCount = rs.getInt("CMT_COUNT");

				NoticeView notice= new NoticeView(
						id,
						title,
						writerId,
						regdate,
						hit,
						files,
						//content,
						pub,
						cmtCount
						);
				list.add(notice);
			} 
			 	rs.close();
				st.close();
				con.close();

			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
		
		
	}
	
	
	public List<NoticeView> getNoticeList(String field, String query, int page){

		List<NoticeView> list = new ArrayList<>();
		
		String sql="SELECT   Z.* \r\n" + 
				"  FROM   (SELECT   ROWNUM AS NUM, A.*\r\n" + 
				"            FROM   (  SELECT   *\r\n" + 
				"                        FROM   NOTICE_VIEW_SERVLET WHERE "+field+" LIKE ? \r\n" + 
				"                    ORDER BY   REGDATE DESC) A) Z    \r\n" + 
				" WHERE   NUM BETWEEN ? AND ?  order by id ";
		
		
		// 1, 11, 21, 31 -> 1+(page-1)*10
		// 10,20, 30, 40 -> page*10

		
		String url = "jdbc:oracle:thin:@localhost:1521:XE";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			PreparedStatement st = con.prepareStatement(sql);
			//System.out.println(sql);
			st.setString(1, "%" + query + "%");
			st.setInt(2, 1+(page-1)*10);
			st.setInt(3, page*10);
			ResultSet rs= st.executeQuery();

			 while(rs.next()){ 
			 	int id = rs.getInt("ID");
			 	String title=rs.getString("TITLE");
				String writerId=rs.getString("WRITER_ID");
				Date regdate=rs.getDate("REGDATE"); 
				String hit=rs.getString("HIT");
				String files=rs.getString("FILES");
				//String content=rs.getString("CONTENT"); 
				boolean pub = rs.getBoolean("PUB");
				int cmtCount = rs.getInt("CMT_COUNT");

				NoticeView notice= new NoticeView(
						id,
						title,
						writerId,
						regdate,
						hit,
						files,
						//content,
						pub,
						cmtCount
						);
				list.add(notice);
			} 
			 	rs.close();
				st.close();
				con.close();

			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list;
	}	
	
	public int getNoticeCount() {
		return getNoticeCount("title", "");
	}
	
	public int getNoticeCount(String field, String query) {
		
		int count = 0;
		
		String sql="SELECT   COUNT(ID) COUNT \r\n" + 
				"  FROM   (SELECT   ROWNUM AS NUM, A.*\r\n" + 
				"            FROM   (  SELECT   *\r\n" + 
				"                        FROM   NOTICE1 WHERE "+field+" LIKE ? \r\n" + 
				"                    ORDER BY   REGDATE DESC) A) Z\r\n" ;

		String url = "jdbc:oracle:thin:@localhost:1521:XE";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%" + query + "%");
			
			
			//System.out.println(sql);
			ResultSet rs= st.executeQuery();

			
			if(rs.next())
				count = rs.getInt("COUNT");
			
			
			 	rs.close();
				st.close();
				con.close();

			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return count;
	}
	
	public Notice getNotice(int id) {
		
		Notice notice = new Notice();
		
		String sql="SELECT * from NOTICE1 WHERE ID = ? ";
		
		
		
		
		String url = "jdbc:oracle:thin:@localhost:1521:XE";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			PreparedStatement st = con.prepareStatement(sql);
			//System.out.println(sql);
			st.setInt(1, id);
			ResultSet rs= st.executeQuery();

			 if(rs.next()){ 
			 	int nid = rs.getInt("ID");
			 	String title=rs.getString("TITLE");
				String writerId=rs.getString("WRITER_ID");
				Date regdate=rs.getDate("REGDATE"); 
				String hit=rs.getString("HIT");
				String files=rs.getString("FILES");
				String content=rs.getString("CONTENT"); 
				boolean pub = rs.getBoolean("PUB");

				notice= new Notice(
						nid,
						title,
						writerId,
						regdate,
						hit,
						files,
						content,
						pub
						);
			 	} 
			 
			 	rs.close();
				st.close();
				con.close();

			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return notice;		
				
		
		
	}

		

	
	public Notice getNextNotice(int id) {
		Notice notice = new Notice();
		String sql = "SELECT   *\r\n" + 
				"  FROM   NOTICE1\r\n" + 
				" WHERE   ID = (SELECT   ID\r\n" + 
				"                 FROM   NOTICE1\r\n" + 
				"                WHERE   REGDATE > (SELECT   REGDATE\r\n" + 
				"                                     FROM   NOTICE1\r\n" + 
				"                                    WHERE   ID = ?)\r\n" + 
				"                        AND ROWNUM = 1)";
		

		String url = "jdbc:oracle:thin:@localhost:1521:XE";		

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			PreparedStatement st = con.prepareStatement(sql);
			//System.out.println(sql);
			st.setInt(1, id);
			ResultSet rs= st.executeQuery();

			 if(rs.next()){ 
			 	int nid = rs.getInt("ID");
			 	String title=rs.getString("TITLE");
				String writerId=rs.getString("WRITER_ID");
				Date regdate=rs.getDate("REGDATE"); 
				String hit=rs.getString("HIT");
				String files=rs.getString("FILES");
				String content=rs.getString("CONTENT"); 
				boolean pub = rs.getBoolean("PUB");

				notice= new Notice(
						nid,
						title,
						writerId,
						regdate,
						hit,
						files,
						content,
						pub
						);
			 	} 
			 
			 	rs.close();
				st.close();
				con.close();

			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		return notice;
	}
	
	public Notice getPrevNotice(int id) {
		Notice notice = new Notice();
		String sql = "SELECT   ID\r\n" + 
				"  FROM   (  SELECT   *\r\n" + 
				"              FROM   NOTICE1\r\n" + 
				"          ORDER BY   REGDATE DESC)\r\n" + 
				" WHERE   REGDATE < (SELECT   REGDATE\r\n" + 
				"                      FROM   NOTICE1\r\n" + 
				"                     WHERE   ID = ?)\r\n" + 
				"         AND ROWNUM = 1";
		

		String url = "jdbc:oracle:thin:@localhost:1521:XE";	

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			PreparedStatement st = con.prepareStatement(sql);
			//System.out.println(sql);
			st.setInt(1, id);
			ResultSet rs= st.executeQuery();

			 if(rs.next()){ 
			 	int nid = rs.getInt("ID");
			 	String title=rs.getString("TITLE");
				String writerId=rs.getString("WRITER_ID");
				Date regdate=rs.getDate("REGDATE"); 
				String hit=rs.getString("HIT");
				String files=rs.getString("FILES");
				String content=rs.getString("CONTENT"); 
				boolean pub = rs.getBoolean("PUB");

				notice= new Notice(
						nid,
						title,
						writerId,
						regdate,
						hit,
						files,
						content,
						pub
						);
			 	} 
			 
			 	rs.close();
				st.close();
				con.close();

			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		return notice;
	}
	/**
	 * @param ids
	 * @return
	 */
	public int deleteNoticeAll(int[] ids) {
		
		int result = 0;
		
		String params = "";
		
		for(int i=0; i< ids.length; i++) {
			params += ids[i];
			
			if(i < ids.length-1)
				params += ",";
		}
		
		String sql = "DELETE NOTICE1 WHERE ID IN ("+params+")";
		

		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url, "system", "1234");
			Statement st = con.createStatement();
			//System.out.println(sql);
			result = st.executeUpdate(sql);

				st.close();
				con.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}	
	
}
