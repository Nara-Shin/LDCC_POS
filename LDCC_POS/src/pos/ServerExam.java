package pos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerExam {
	static final int PORT_NUM = 6060;
	// static final String JDBC_DRIVER = "org.mysql.jdbc.Driver";
	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	static final String DB_URL = "jdbc:mariadb://localhost:3306/ldccpos";

	static final String USERNAME = "root";
	static final String PASSWORD = "intern05@ldcc";

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;

		try {
			ServerSocket serverSocket = new ServerSocket(PORT_NUM);// 서버소켓 선언

			while (true) {
				System.out.println("\n클라이언트 접속 대기 중...");
				Socket socket = serverSocket.accept(); // 서버소켓으로부터 소켓 객체 가져오기

				System.out.println(socket.getInetAddress() + "가 접속되었습니다.");

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// 클라이언트로부터 메시지 입력받음
				String clientMessage = bufferedReader.readLine();

				// 입력받은 내용을 서버 콘솔에 출력
				System.out.println("클라이언트가 보내온 내용 : " + clientMessage);
				// 클라이언트에게 보내기 위한 준비
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				// if(clientMessage == "all") {
				try {
					Class.forName(JDBC_DRIVER);
					conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
					System.out.println("\n- MySQL Connection");
					stmt = conn.createStatement();

					String sql;
					sql = "SELECT code, name FROM pos_info";
					ResultSet rs = stmt.executeQuery(sql);

					while (rs.next()) {
						String code = rs.getString("code");
						String name = rs.getString("name");

						System.out.print("\n** Code : " + code);
						System.out.print("\n -> Name: " + name);
						bufferedWriter.write("상품명 : " + name+"\r\n");
						bufferedWriter.newLine(); // readLine()으로 읽으므로 한줄끝을 알림

					}
					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException se1) {
					se1.printStackTrace();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						if (stmt != null)
							stmt.close();
					} catch (SQLException se2) {
					}
					try {
						if (conn != null)
							conn.close();
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				// }

				bufferedWriter.flush();

				socket.close();// 접속 종료
				// break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}