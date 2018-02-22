package pos;

/*------------------------------------------------------------------
[ServerExam JAVA]

Project    : LDCC_POS
Version : 1.0
Last change : 2018/02/22
Developer : Nara Shin
-------------------------------------------------------------------*/
/*------------------------------------------------------------------
 [Table of contents]

 1. Page function : 함수 호출
 -------------------------------------------------------------------*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ServerExam {
	static final int PORT_NUM = 6060;

	// 서버 소켓 및 소켓 선언
	private static ServerSocket serverSocket;
	private static Socket socket;

	private static BufferedReader bufferedReader;
	private static BufferedWriter bufferedWriter;

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;

		try {
			while (true) {
				serverSocket = new ServerSocket(PORT_NUM);// 서버소켓 선언
				System.out.println("\n클라이언트 접속 대기 중...");
				socket = serverSocket.accept(); // 서버소켓으로부터 소켓 객체 가져오기

				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				System.out.println(socket.getInetAddress() + "가 접속되었습니다.");

				// 클라이언트로부터 메시지 입력받음
				String clientMessage = bufferedReader.readLine();

				// 입력받은 내용을 서버 콘솔에 출력
				System.out.println("클라이언트가 보내온 내용 : " + clientMessage);
				// 클라이언트에게 보내기 위한 준비
				if (clientMessage.equals("login")) {
					int i = 1;
					String[] prod = new String[3];

					String line = "";
					while ((line = bufferedReader.readLine()) != null) {
						if (i == 1) {
							prod[i] = line;
							System.out.println(i + ": " + line);
							i++;
						} else {
							prod[i] = line;
							System.out.println(i + ": " + line);
							break;
						}
					}

					LoginLogic(prod[1], prod[2]);
				}
				if (clientMessage.equals("sell")) {
					int i = 1;
					int prod_num = 0;
					int cn = 0;
					int qn = 0;

					String[] prod = new String[3];
					String[] code_arr = new String[100];
					String[] qunatity_arr = new String[100];

					String line = "";
					while ((line = bufferedReader.readLine()) != null) {
						if (i == 0) {
							prod[i] = line;
							if (prod[i].equals("fin")) {
								break;
							}
							i++;
						} else if (i == 1) {
							prod[i] = line;
							code_arr[cn] = prod[i];
							System.out.println(i + ": " + prod[i]);
							cn++;
							i++;
							prod_num++;
						} else {
							prod[i] = line;
							qunatity_arr[qn] = prod[i];
							System.out.println(i + ": " + prod[i]);
							i = 0;
							qn++;
						}
					}

					for (int j = 0; j < prod_num; j++) {
						SellProduct(code_arr[j], Integer.parseInt(qunatity_arr[j]));
					}

				} else if (clientMessage.equals("refund")) {
					int i = 1;
					String[] prod = new String[3];

					String line = "";
					while ((line = bufferedReader.readLine()) != null) {
						if (i == 1) {
							prod[i] = line;
							System.out.println(i + ": " + line);
							i++;
						} else {
							prod[i] = line;
							System.out.println(i + ": " + line);
							break;
						}
					}

					UpdateProduct(prod[1], Integer.parseInt(prod[2]));

				} else if (clientMessage.equals("check")) {
					CheckProduct();
				} else if (clientMessage.equals("bar_check")) {
					int i = 1;
					String[] prod = new String[3];

					String line = "";
					while ((line = bufferedReader.readLine()) != null) {
						if (i == 1) {
							prod[i] = line;
							System.out.println(i + ": " + line);
							break;
						}
					}

					CheckBarProduct(prod[1]);
				} else if (clientMessage.equals("exit")) {
					System.out.println("\n접속 종료...");
					bufferedReader.close();
					serverSocket.close();
					socket.close();// 접속 종료
					break;
				}
				serverSocket.close();
				socket.close();// 접속 종료
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void LoginLogic(String id, String pw) {
		List<LoginDTO> result_list = new ArrayList<LoginDTO>();
		LoginDAO dao = new LoginDAO();
		LoginDTO dto = new LoginDTO();

		
		String user_id = id;
		String user_pw = pw;
		dto.setUser_id(user_id);
		dto.setUser_pw(user_pw);

		
		result_list = dao.loginTry(dto);

		loginflushBuffer(result_list);

	}

	private static void SellProduct(String code, int quantity) {
		ProductDTO dto = new ProductDTO();
		String prod_code = code;
		int prod_quantity = quantity;
		List<ProductDTO> result_list = new ArrayList<ProductDTO>();

		ProductDAO dao = new ProductDAO();
		dto.setProd_code(prod_code);
		dto.setProd_quantity(prod_quantity);
		boolean ok = dao.sellProduct(dto);
		if (ok) {
			System.out.println("결제 성공");
		} else {
			System.out.println("결제 실패");
		}

		result_list = dao.sellProductList(dto);

		flushBuffer(result_list);

	}

	private static void UpdateProduct(String code, int quantity) throws Exception {
		ProductDTO dto = new ProductDTO();
		String prod_code = code;
		int prod_quantity = quantity;
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		// 2. 그정보로 DB를 수정
		ProductDAO dao = new ProductDAO();
		dto.setProd_code(prod_code);
		dto.setProd_quantity(prod_quantity);

		boolean ok = dao.updateProduct(dto);

		if (ok) {
			System.out.println("환불 성공");
			bufferedWriter.write("환불 성공");
			bufferedWriter.newLine(); // readLine()으로 읽으므로 한줄끝을 알림
			bufferedWriter.flush();

		} else {
			System.out.println("환불 실패");
			bufferedWriter.write("환불 실패");
			bufferedWriter.newLine(); // readLine()으로 읽으므로 한줄끝을 알림
			bufferedWriter.flush();
		}
	}

	private static void CheckProduct() {

		ProductDAO dao = new ProductDAO();

		List<ProductDTO> result_list = new ArrayList<ProductDTO>();
		result_list = dao.getProductList();

		flushBuffer(result_list);

	}

	private static void CheckBarProduct(String code) throws IOException {
		ProductDTO dto = new ProductDTO();
		String prod_code = code;

		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		// 2. 그정보로 DB를 수정
		ProductDAO dao = new ProductDAO();
		dto.setProd_code(prod_code);

		List<ProductDTO> result_list = new ArrayList<ProductDTO>();
		result_list = dao.getProductCheck(dto);

		flushBuffer(result_list);

	}

	private static void flushBuffer(List<ProductDTO> result_list) {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			for (int i = 0; i < result_list.size(); i++) {
				ProductDTO prod = result_list.get(i);
				bufferedWriter.write(prod.getProd_code() + " ");

				bufferedWriter.write(prod.getProd_name() + " ");

				bufferedWriter.write(prod.getProd_price() + " ");

				bufferedWriter.write(prod.getProd_quantity() + " ");

				bufferedWriter.write(prod.getProd_weight() + " ");
				bufferedWriter.newLine(); // readLine()으로 읽으므로 한줄끝을 알림
			}
			bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void loginflushBuffer(List<LoginDTO> result_list) {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			for (int i = 0; i < result_list.size(); i++) {
				LoginDTO login = result_list.get(i);
				bufferedWriter.write(login.getUser_id() + " ");

				bufferedWriter.write(login.getUser_pw() + " ");

				bufferedWriter.newLine(); // readLine()으로 읽으므로 한줄끝을 알림
			}
			bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}