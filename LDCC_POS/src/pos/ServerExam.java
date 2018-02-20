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

import javax.swing.JOptionPane;

public class ServerExam {
	static final int PORT_NUM = 6060;
	// static final String JDBC_DRIVER = "org.mysql.jdbc.Driver";
	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	static final String DB_URL = "jdbc:mariadb://localhost:3306/ldccpos";

	static final String USERNAME = "root";
	static final String PASSWORD = "intern05@ldcc";
	
	//서버 소켓 및 소켓 선언
	private static ServerSocket serverSocket;
	private static Socket socket;

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;

		try {
			serverSocket = new ServerSocket(PORT_NUM);// 서버소켓 선언

			while (true) {
				System.out.println("\n클라이언트 접속 대기 중...");
				socket = serverSocket.accept(); // 서버소켓으로부터 소켓 객체 가져오기
				System.out.println(socket.getInetAddress() + "가 접속되었습니다.");
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// 클라이언트로부터 메시지 입력받음
//				String clientMessage = bufferedReader.readLine();

				// 입력받은 내용을 서버 콘솔에 출력
//				System.out.println("클라이언트가 보내온 내용 : " + clientMessage);
				// 클라이언트에게 보내기 위한 준비
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

//				int code = clientMessage;
				int i=0;
				String[] prod = new String[2];
				String line = "";
				while((line=bufferedReader.readLine())!=null){
					if(i==0) {
						prod[i] = line;
						System.out.println(i+": "+line);
						i++;
					}else {
						prod[i] = line;
						System.out.println(i+": "+line);
						break;
					}
				}
				bufferedReader.close();
				
				UpdateProduct(prod[0],  Integer.parseInt(prod[1]));
				
				
//				UpdateProduct(code, quantity);
				
				// if(clientMessage == "all") {
//				try {
//					Class.forName(JDBC_DRIVER);
//					conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
//					System.out.println("\n- MySQL Connection");
//					stmt = conn.createStatement();
//
//					String sql;
//					sql = "SELECT prod_code, prod_name FROM prod_info";
//					ResultSet rs = stmt.executeQuery(sql);
//
//					while (rs.next()) {
//						String prod_code = rs.getString("prod_code");
//						String prod_name = rs.getString("prod_name");
//
//						System.out.print("\n** Code : " + prod_code);
//						System.out.print("\n -> Name: " + prod_name);
//						bufferedWriter.write("상품명 : " + prod_name+"\r\n");
//						bufferedWriter.newLine(); // readLine()으로 읽으므로 한줄끝을 알림
//
//					}
//					rs.close();
//					stmt.close();
//					conn.close();
//				} catch (SQLException se1) {
//					se1.printStackTrace();
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				} finally {
//					try {
//						if (stmt != null)
//							stmt.close();
//					} catch (SQLException se2) {
//					}
//					try {
//						if (conn != null)
//							conn.close();
//					} catch (SQLException se) {
//						se.printStackTrace();
//					}
//				}
				// }

//				bufferedWriter.write("ㄱㄱㄱㄱㄱ");
//				bufferedWriter.newLine(); // readLine()으로 읽으므로 한줄끝을 알림
//				bufferedWriter.flush();

				socket.close();// 접속 종료
//				 break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void UpdateProduct(String code, int quantity) {
		System.out.println("aaaaa");
	       
        //1. 화면의 정보를 얻는다.
        ProductDTO dto = new ProductDTO();
        String prod_code = code;
        int prod_quantity = quantity;

        //2. 그정보로 DB를 수정
        ProductDAO dao = new ProductDAO();
        dto.setProd_code(prod_code);
        dto.setProd_quantity(prod_quantity);
        
        boolean ok = dao.updateProduct(dto);
       
        if(ok){
        	System.out.println("환불 성공");
        }else{
            System.out.println("수정실패: 에러");
        }
    }

}