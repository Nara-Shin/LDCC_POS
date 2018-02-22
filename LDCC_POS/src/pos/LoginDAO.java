package pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*------------------------------------------------------------------
[LoginDAO JAVA]

Project    : LDCC_POS
Version : 1.0
Last change : 2018/02/22
Developer : Nara Shin
-------------------------------------------------------------------*/
/*------------------------------------------------------------------
 [Table of contents]

 1. Page function : 함수 호출
 -------------------------------------------------------------------*/

public class LoginDAO {
	private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	private static final String DB_URL = "jdbc:mariadb://localhost:3306/ldccpos";

	private static final String USERNAME = "root"; // DB ID
	private static final String PASSWORD = "intern05@ldcc"; // DB 패스워드

	private static Statement stmt;
	
	public LoginDAO() {

	}
	
	/** DB연결 메소드 */
	public Connection getConn() {
		Connection con = null;

		try {
			Class.forName(JDBC_DRIVER); // 1. 드라이버 로딩
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); // 2. 드라이버 연결

		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}
	
	/** 유저 정보를 얻는 메소드 */
	public List loginTry(LoginDTO vLog) {
		System.out.println("dto=" + vLog.toString());
		LoginDTO dto = new LoginDTO();
		boolean ok = false;
		
		Connection con = null; // 연결
		PreparedStatement ps = null; // 명령
		ResultSet rs = null; // 결과
		List<LoginDTO> result_list = new ArrayList<LoginDTO>();

		try {

			con = getConn();
			String sql = "select * from user_info where user_id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, vLog.getUser_id());

			rs = ps.executeQuery();

			while (rs.next()) {
				LoginDTO login = new LoginDTO();
				String id = rs.getString("user_id");
				String pw = rs.getString("user_pw");
				
				login.setUser_id(id);
				login.setUser_pw(pw);

				result_list.add(login);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result_list;
	}
}
