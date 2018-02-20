package pos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Handler;

public class ClientExam {
	private String html = "";
	private Handler mHandler;

	private static Socket socket;

	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	private static String IP = "127.0.0.1"; // IP
	private static int PORT_NUM = 6060; // PORT번호

	public static void main(String[] args) {
		ClientExam Cli = new ClientExam();
		// Cli.login();

		try {
			socket = new Socket(IP, PORT_NUM);
			Cli.draw_menu(socket);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login() {

	}

	public void draw_menu(Socket socket) {
		int swValue;
		ClientExam Cli = new ClientExam();

		while (true) {
			// Display menu graphics
			System.out.println("\n============================");
			System.out.println("|      POS 메인 페이지     |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 결제           |");
			System.out.println("|        2. 환불           |");
			System.out.println("|        3. 재고관리       |");
			System.out.println("|        4. Exit           |");
			System.out.println("============================");
			swValue = Keyin.inInt(" ▶ 메뉴 선택 : ");

			// Switch construct
			switch (swValue) {
			case 1: // 결제
				Cli.func_sell(socket);
				continue;
			case 2: // 환불
				Cli.func_refund(socket);
				continue;
			case 3: // 재고관리
				Cli.func_check(socket);
				continue;
			case 4:
				System.out.println("★프로그램 종료★");
				System.exit(0); // 프로그램 종료
				break;
			default:
				System.out.println("메뉴의 번호를 선택해주세요");
				continue; // This break is not really necessary
			}
			break;
		}
	}

	public void func_sell(Socket socket) {
		int swValue;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\n============================");
			System.out.println("|     POS 결제 페이지      |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 바코드 입력    |");
			System.out.println("|        2. 뒤로가기       |");
			System.out.println("============================");
			swValue = Keyin.inInt(" ▶ 메뉴 선택 ");

			switch (swValue) {
			case 1:
				// ###################수량조절 개발하기
				// ###################여러 개 등록하는거 개발하기
				System.out.println("★바코드 입력하기★");
//				try {
//					socket = new Socket(IP, PORT_NUM);
//
//					// 서버에게 보내기 위한 준비
//					BufferedWriter bufferedWriter = new BufferedWriter(
//							new OutputStreamWriter(socket.getOutputStream()));
//
//					System.out.print("바코드 번호 : ");
//					String prod_code = sc.nextLine();
//
//					bufferedWriter.write(prod_code);
//					bufferedWriter.newLine(); // readLine()으로 읽기 때문에 개행 추가
//					bufferedWriter.flush();
//
//					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//					// 서버부터 메시지 입력받음
//					String serverMessage = bufferedReader.readLine();
//
//					// 입력받은 내용을 서버 콘솔에 출력
//					System.out.println("서버가 보내온 내용 : " + serverMessage);
//
//					socket.close();// 접속 종료
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				continue;
			case 2:
				System.out.println("★뒤로 가기★");
				break;
			default:
				System.out.println("Invalid selection");
				continue; // This break is not really necessary
			}
			break;
		}

	}

	public void func_refund(Socket socket) {
		int swValue;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\n============================");
			System.out.println("|     POS 환불 페이지      |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 환불하기       |");
			System.out.println("|        2. 교환하기       |");
			System.out.println("|        3. 뒤로가기       |");
			System.out.println("============================");
			swValue = Keyin.inInt(" ▶ 메뉴 선택 : ");

			switch (swValue) {
			case 1:
				System.out.println("★환불하기★");
				String[] prod = new String[2];
				try {
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					System.out.print("바코드 번호 : ");
					prod[0] = sc.nextLine();
					System.out.print("수량 : ");
					prod[1] = sc.nextLine();
					
					bufferedWriter.write(prod[0]);
					bufferedWriter.newLine();
					bufferedWriter.write(prod[1]);
					bufferedWriter.newLine();
					bufferedWriter.flush();
					
					// 서버부터 메시지 입력받음
					String serverMessage = bufferedReader.readLine();

					// 입력받은 내용을 서버 콘솔에 출력
					System.out.println("서버가 보내온 내용 : " + serverMessage);
				}catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			case 2:
				System.out.println("★교환하기★");
				continue;
			case 3:
				System.out.println("★뒤로 가기★");
				break;
			default:
				System.out.println("Invalid selection");
				continue; // This break is not really necessary
			}
			break;
		}
	}

	public void func_check(Socket socket) {
		int swValue;
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("\n============================");
			System.out.println("|    POS 재고관리 페이지   |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 전체 품목 조회 |");
			System.out.println("|        2. 특정 품목 조회 |");
			System.out.println("|        3. 입고 품목 등록 |");
			System.out.println("|        4. 뒤로가기       |");
			System.out.println("============================");
			swValue = Keyin.inInt(" ▶ 메뉴 선택 : ");

			switch (swValue) {
			case 1:
				System.out.println("★전체 품목 조회★");
//				try {
//					Socket socket = new Socket(IP, PORT_NUM);
//
//					// 서버에게 보내기 위한 준비
//					BufferedWriter bufferedWriter = new BufferedWriter(
//							new OutputStreamWriter(socket.getOutputStream()));
//
//					String code = "all";
//
//					bufferedWriter.write(code);
//					bufferedWriter.newLine(); // readLine()으로 읽기 때문에 개행 추가
//					bufferedWriter.flush();
//
//					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//					// 서버부터 메시지 입력받음
//					String serverMessage = bufferedReader.readLine();
//
//					// 입력받은 내용을 서버 콘솔에 출력
//					String line = null;
//					while ((line = serverMessage) != null) {
//						System.out.println("서버가 보내온 내용 : " + line);
//					}
//
//					socket.close();// 접속 종료
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				continue;
			case 2:
				System.out.println("★특정 품목 조회★");
				continue;
			case 3:
				System.out.println("★입고 품목 등록★");
				continue;
			case 4:
				System.out.println("★뒤로 가기★");
				break;
			default:
				System.out.println("Invalid selection");
				continue; // This break is not really necessary
			}
			break;
		}
	}

	public void setSocket(String ip, int port) throws IOException {

		try {
			socket = new Socket(ip, port);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}
	// 출처: http://pulsebeat.tistory.com/24
}

class Keyin {
	// *******************************
	// support methods
	// *******************************
	// Method to display the user's prompt string
	public static void printPrompt(String prompt) {
		System.out.print(prompt + " ");
		System.out.flush();
	}

	// Method to make sure no data is available in the
	// input stream
	public static void inputFlush() {
		int dummy;
		int bAvail;

		try {
			while ((System.in.available()) != 0)
				dummy = System.in.read();
		} catch (java.io.IOException e) {
			System.out.println("Input error");
		}
	}

	// ********************************
	// data input methods for
	// string, int, char, and double
	// ********************************
	public static String inString(String prompt) {
		inputFlush();
		printPrompt(prompt);
		return inString();
	}

	public static String inString() {
		int aChar;
		String s = "";
		boolean finished = false;

		while (!finished) {
			try {
				aChar = System.in.read();
				if (aChar < 0 || (char) aChar == '\n')
					finished = true;
				else if ((char) aChar != '\r')
					s = s + (char) aChar; // Enter into string
			}

			catch (java.io.IOException e) {
				System.out.println("Input error");
				finished = true;
			}
		}
		return s;
	}

	public static int inInt(String prompt) {
		while (true) {
			inputFlush();
			printPrompt(prompt);
			try {
				return Integer.valueOf(inString().trim()).intValue();
			}

			catch (NumberFormatException e) {
				System.out.println("Invalid input. Not an integer");
			}
		}
	}

	public static char inChar(String prompt) {
		int aChar = 0;

		inputFlush();
		printPrompt(prompt);

		try {
			aChar = System.in.read();
		}

		catch (java.io.IOException e) {
			System.out.println("Input error");
		}
		inputFlush();
		return (char) aChar;
	}

	public static double inDouble(String prompt) {
		while (true) {
			inputFlush();
			printPrompt(prompt);
			try {
				return Double.valueOf(inString().trim()).doubleValue();
			}

			catch (NumberFormatException e) {
				System.out.println("Invalid input. Not a floating point number");
			}
		}
	}
}