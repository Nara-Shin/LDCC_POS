package pos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientExam {
	static final int PORT_NUM = 6060;

	public static void main(String[] args) {
		ClientExam Cli = new ClientExam();
		Cli.draw_menu();
	}

	public void draw_menu() {
		int swValue;
		ClientExam Cli = new ClientExam();

		while (true) {
			// Display menu graphics
			System.out.println("============================");
			System.out.println("|      POS 메인 페이지     |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 결제           |");
			System.out.println("|        2. 환불           |");
			System.out.println("|        3. 재고관리       |");
			System.out.println("|        4. Exit           |");
			System.out.println("============================");
			swValue = Keyin.inInt(" Select option: ");

			// Switch construct
			switch (swValue) {
			case 1: // 결제
				Cli.func_sell();
				continue;
			case 2: // 환불
				Cli.func_refund();
				continue;
			case 3: // 재고관리
				Cli.func_check();
				continue;
			case 4:
				System.out.println("프로그램 종료");
				System.exit(0); // 프로그램 종료
				break;
			default:
				System.out.println("메뉴의 번호를 선택해주세요");
				continue; // This break is not really necessary
			}
			break;
		}
	}

	public void func_sell() {
		int swValue;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("============================");
			System.out.println("|     POS 결제 페이지      |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 바코드 입력    |");
			System.out.println("|        2. 수량조절       |");
			System.out.println("|        3. 뒤로가기       |");
			System.out.println("|        4. Exit           |");
			System.out.println("============================");
			swValue = Keyin.inInt(" Select option: ");

			switch (swValue) {
			case 1:
				System.out.println("★바코드 입력하기★");
				try {
					Socket socket = new Socket("127.0.0.1", PORT_NUM);

					// 서버에게 보내기 위한 준비
					BufferedWriter bufferedWriter = new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream()));

					System.out.print("바코드 번호 : ");
					String code = sc.nextLine();

					bufferedWriter.write(code);
					bufferedWriter.newLine(); // readLine()으로 읽기 때문에 개행 추가
					bufferedWriter.flush();

					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					// 서버부터 메시지 입력받음
					String serverMessage = bufferedReader.readLine();

					// 입력받은 내용을 서버 콘솔에 출력
					System.out.println("서버가 보내온 내용 : " + serverMessage);

					socket.close();// 접속 종료

				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			case 2:
				System.out.println("★수량 조절하기★");
				continue;
			case 3:
				System.out.println("▶뒤로 가기◀");
				break;
			case 4:
				System.out.println("★종료★");
				System.exit(0); // 프로그램 종료
			default:
				System.out.println("Invalid selection");
				continue; // This break is not really necessary
			}
			break;
		}

	}

	public void func_refund() {
		int swValue;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("============================");
			System.out.println("|     POS 환불 페이지      |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 바코드 입력    |");
			System.out.println("|        2. 교환하기       |");
			System.out.println("|        3. 뒤로가기       |");
			System.out.println("|        4. Exit           |");
			System.out.println("============================");
			swValue = Keyin.inInt(" Select option: ");

			switch (swValue) {
			case 1:
				System.out.println("★바코드 입력하기★");
				continue;
			case 2:
				System.out.println("★교환하기★");
				continue;
			case 3:
				System.out.println("▶뒤로 가기◀");
				break;
			case 4:
				System.out.println("★종료★");
				System.exit(0); // 프로그램 종료
				break;
			default:
				System.out.println("Invalid selection");
				continue; // This break is not really necessary
			}
			break;
		}
	}

	public void func_check() {
		int swValue;
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("============================");
			System.out.println("|    POS 재고관리 페이지   |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 전체 품목 조회 |");
			System.out.println("|        2. 특정 품목 조회 |");
			System.out.println("|        3. 재고 추가      |");
			System.out.println("|        4. 뒤로가기       |");
			System.out.println("|        5. Exit           |");
			System.out.println("============================");
			swValue = Keyin.inInt(" Select option: ");

			switch (swValue) {
			case 1:
				System.out.println("★전체 품목 조회★");
				try {
					Socket socket = new Socket("127.0.0.1", PORT_NUM);

					// 서버에게 보내기 위한 준비
					BufferedWriter bufferedWriter = new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream()));

					String code = "all";

					bufferedWriter.write(code);
					bufferedWriter.newLine(); // readLine()으로 읽기 때문에 개행 추가
					bufferedWriter.flush();

					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					// 서버부터 메시지 입력받음
					String serverMessage = bufferedReader.readLine();

					// 입력받은 내용을 서버 콘솔에 출력
					String line = null;
					while ((line = serverMessage) != null) {
						System.out.println("서버가 보내온 내용 : " + line);
					}

					socket.close();// 접속 종료

				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			case 2:
				System.out.println("★특정 품목 조회★");
				continue;
			case 3:
				System.out.println("★재고 추가하기★");
				continue;
			case 4:
				System.out.println("▶뒤로 가기◀");
				break;
			case 5:
				System.out.println("★종료★");
				System.exit(0); // 프로그램 종료
				break;
			default:
				System.out.println("Invalid selection");
				continue; // This break is not really necessary
			}
			break;
		}
	}
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