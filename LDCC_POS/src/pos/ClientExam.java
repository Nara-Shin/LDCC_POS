package pos;

/*------------------------------------------------------------------
[ClientExam JAVA]

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
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Handler;

public class ClientExam {
	private String html = "";
	private Handler mHandler;
	private static ClientExam Cli;

	private static Socket socket;

	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	private static String IP = "127.0.0.1"; // IP
	private static int PORT_NUM = 6060; // PORT번호

	public static void main(String[] args) {
		Cli = new ClientExam();
		Cli.draw_menu();
	}

	public void login() {

	}

	public void draw_menu() {
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
				Cli.func_sell();
				continue;
			case 2: // 환불
				Cli.func_refund();
				continue;
			case 3: // 재고관리
				Cli.func_check();
				continue;
			case 4:
				System.out.println("★프로그램 종료★");
				String[] prod = new String[1];
				try {
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

					prod[0] = "exit";

					bufferedWriter.write(prod[0]);
					bufferedWriter.newLine();

				} catch (Exception e) {
					e.printStackTrace();
				}
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
			System.out.println("\n============================");
			System.out.println("|     POS 결제 페이지      |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 바코드입력/결제|");
			System.out.println("|        2. 뒤로가기       |");
			System.out.println("============================");
			swValue = Keyin.inInt(" ▶ 메뉴 선택 : ");

			switch (swValue) {
			case 1:
				System.out.println("★바코드 입력하기★");
				String str;
				String quitWord = "n";
				String[] prod = new String[3];
				try {
					socket = new Socket(IP, PORT_NUM);
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					while(true) {
						prod[0] = "sell";
						System.out.print("바코드 번호 : ");
						prod[1] = sc.nextLine();
						System.out.print("수량 : ");
						prod[2] = sc.nextLine();

						bufferedWriter.write(prod[0]);
						bufferedWriter.newLine();
						bufferedWriter.write(prod[1]);
						bufferedWriter.newLine();
						bufferedWriter.write(prod[2]);
						bufferedWriter.newLine();
						
						System.out.print("계속 하시겠습니까?(y/n) : ");
						str = sc.nextLine();
			            if (str.equalsIgnoreCase(quitWord)) {
			            	prod[0] = "fin";
			            	bufferedWriter.write(prod[0]);
							bufferedWriter.newLine();
			            	break;
			            }
					}
					
					bufferedWriter.flush();

					// // 입력받은 내용을 서버 콘솔에 출력
					setTableForm(bufferedReader);
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			case 2:
				System.out.println("★뒤로 가기★");
				break;
			default:
				System.out.println("메뉴의 번호를 선택해주세요");
				continue; // This break is not really necessary
			}
			break;
		}

	}

	public void func_refund() {
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
				String[] prod = new String[3];
				try {
					socket = new Socket(IP, PORT_NUM);
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					prod[0] = "refund";
					System.out.print("바코드 번호 : ");
					prod[1] = sc.nextLine();
					System.out.print("수량 : ");
					prod[2] = sc.nextLine();

					bufferedWriter.write(prod[0]);
					bufferedWriter.newLine();
					bufferedWriter.write(prod[1]);
					bufferedWriter.newLine();
					bufferedWriter.write(prod[2]);
					bufferedWriter.newLine();
					bufferedWriter.flush();

					// // 서버부터 메시지 입력받음
					String serverMessage = bufferedReader.readLine();
					//
					// // 입력받은 내용을 서버 콘솔에 출력
					System.out.println("환불결과 : " + serverMessage);
					socket.close();
				} catch (Exception e) {
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
				System.out.println("메뉴의 번호를 선택해주세요");
				continue; // This break is not really necessary
			}
			break;
		}
	}

	public void func_check() {
		int swValue;
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("\n============================");
			System.out.println("|    POS 재고관리 페이지   |");
			System.out.println("============================");
			System.out.println("| Options:                 |");
			System.out.println("|        1. 전체 품목 조회 |");
			System.out.println("|        2. 특정 품목 조회 |");
			System.out.println("|        3. 뒤로가기       |");
			System.out.println("============================");
			swValue = Keyin.inInt(" ▶ 메뉴 선택 : ");

			switch (swValue) {
			case 1:
				System.out.println("★전체 품목 조회★");
				String[] prod = new String[3];
				try {
					socket = new Socket(IP, PORT_NUM);
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					prod[0] = "check";

					// 서버로 값 전송
					bufferedWriter.write(prod[0]);
					bufferedWriter.newLine();

					bufferedWriter.flush();

					setTableForm(bufferedReader);
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			case 2:
				System.out.println("★특정 품목 조회★");
				prod = new String[3];
				try {
					socket = new Socket(IP, PORT_NUM);
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					prod[0] = "bar_check";
					System.out.print("바코드 번호 : ");
					prod[1] = sc.nextLine();

					// 서버로 값 전송
					bufferedWriter.write(prod[0]);
					bufferedWriter.newLine();
					bufferedWriter.write(prod[1]);
					bufferedWriter.newLine();
					
					bufferedWriter.flush();

					setTableForm(bufferedReader);
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			case 3:
				System.out.println("★뒤로 가기★");
				break;
			default:
				System.out.println("메뉴의 번호를 선택해주세요");
				continue; // This break is not really necessary
			}
			break;
		}
	}

	public void setTableForm(BufferedReader bufferedReader) {
		// 출력 폼
		String leftAlignFormat = "| %-15s | %-15s | %-4s | %-4s | %-4s |%n";

		System.out.format("+-----------------+--------------------+------+------+------+%n");
		System.out.format("|      바코드     |        품명        | 가격 | 중량 | 수량 |%n");
		System.out.format("+-----------------+--------------------+------+------+------+%n");

		String line = "";
		String[] prod_list = new String[5];
		int i = 0;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				while (st.hasMoreTokens()) { // 더이상 문자가 없을때 까지 반복

					if (i == 0) {
						prod_list[i] = st.nextToken();
						i++;
					} else if (i == 1) {
						prod_list[i] = st.nextToken();
						i++;
					} else if (i == 2) {
						prod_list[i] = st.nextToken();
						i++;
					} else if (i == 3) {
						prod_list[i] = st.nextToken();
						i++;
					} else if (i == 4) {
						prod_list[i] = st.nextToken();
						System.out.format(leftAlignFormat, prod_list[0], prod_list[1], prod_list[2], prod_list[3],
								prod_list[4]);
						i = 0;
						break;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.format("+-----------------+--------------------+------+------+------+%n");
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