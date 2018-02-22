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

 1.  public void login() : 로그인 함수
 2.  public void draw_menu() : 메인메뉴 함수
 3.  public void func_sell() : 결제 페이지 함수
 4.  public void func_refund() : 환불 페이지 함수
 5.  public void func_check() : 재고관리 페이지 함수
 6.  public void setTableForm(BufferedReader bufferedReader) : 테이블 렌더링 함수
     - 결제페이지/환불페이지에서 호출
 7.  public void setBarcode(String flag, String code, String quantity) : 서버에 보낼 바코드 및 수량 write
 8.  public void open() throws Exception, IOException : Socket, bufferedWriter, bufferedReader 열기
 9.  public void close() : Socket, bufferedWriter, bufferedReader 닫기
 10. class Keyin : 숫자 input
 -------------------------------------------------------------------*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Handler;

public class ClientExam {
	// 전역변수 선언
	private String html = "";
	private Handler mHandler;
	private static ClientExam Cli;

	private static Socket socket;

	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	private static String IP = "127.0.0.1"; // IP
	private static int PORT_NUM = 6060; // PORT번호
	
	Scanner sc = new Scanner(System.in);

	//메인함수
	public static void main(String[] args) {
		Cli = new ClientExam();
		
		//로그인
		Cli.login();
	}

	/** 로그인 함수 **/
	public void login() {
		System.out.println("\n============================");
		System.out.println("|     POS 로그인 페이지    |");
		System.out.println("============================");
		String[] login = new String[3];
		String[] login_result = new String[3];
		Cli = new ClientExam();
		
		while(true) {
			try {
				open();
				
				login[0] = "login";
				
				System.out.print("ID : ");
				login[1] = sc.nextLine();
				System.out.print("PW : ");
				login[2] = sc.nextLine();

				// 서버로 값 전송
				bufferedWriter.write(login[0]);
				bufferedWriter.newLine();
				bufferedWriter.write(login[1]);
				bufferedWriter.newLine();
				bufferedWriter.write(login[2]);
				bufferedWriter.newLine();
				bufferedWriter.flush();

				String line = "";
				int i = 0;
				try {
					while ((line = bufferedReader.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(line);
						while (st.hasMoreTokens()) { // 더이상 문자가 없을때 까지 반복
							if (i == 0) { //id
								login_result[i] = st.nextToken();
								i++;
							} else{ //pw
								login_result[i] = st.nextToken();
								break;
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(login[1].equals(login_result[0])&&login[2].equals(login_result[1])) {
					System.out.println("★로그인 성공★\n");
					Cli.draw_menu();
				}else {
					System.out.println("☆로그인 실패☆\n 아이디 혹은 비밀번호를 다시 확인해주세요.\n");
				}
				
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

	/** 메인메뉴 함수 **/
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
			System.out.println("|        2. 환불 및 교환   |");
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
					open();

					prod[0] = "exit";

					bufferedWriter.write(prod[0]);
					bufferedWriter.newLine();
					bufferedWriter.flush();
					
					close();

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

	/** 결제 페이지 함수 **/
	public void func_sell() {
		int swValue;
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
				String quitWord = "p";
				String[] prod = new String[3];
				try {
					open();
					System.out.print("※결제 시 p(pay) 입력\n");
					while (true) {
						prod[0] = "sell";
						System.out.print("바코드 번호 : ");
						prod[1] = sc.nextLine();
						
						str = prod[1];
						if (str.equalsIgnoreCase(quitWord)) {
							prod[0] = "fin";
							bufferedWriter.write(prod[0]);
							bufferedWriter.newLine();
							break;
						}
						
						System.out.print("수량 : ");
						prod[2] = sc.nextLine();

						setBarcode(prod[0], prod[1], prod[2]);

						
					}

					bufferedWriter.flush();

					// // 입력받은 내용을 서버 콘솔에 출력
					setTableForm(bufferedReader);
					close();
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

	/** 환불 페이지 함수 **/
	public void func_refund() {
		int swValue;

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
					open();
					
					prod[0] = "refund";
					System.out.print("바코드 번호 : ");
					prod[1] = sc.nextLine();
					System.out.print("수량 : ");
					prod[2] = sc.nextLine();

					setBarcode(prod[0], prod[1], prod[2]);
					bufferedWriter.flush();

					// // 서버부터 메시지 입력받음
					String serverMessage = bufferedReader.readLine();
					//
					// // 입력받은 내용을 서버 콘솔에 출력
					System.out.println("환불결과 : " + serverMessage);
					close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			case 2:
				System.out.println("★교환하기★");
				String[] change_prod = new String[3];
				try {
					open();
					
					change_prod[0] = "refund";
					System.out.print("반품할 상품 바코드 번호 : ");
					change_prod[1] = sc.nextLine();
					System.out.print("수량 : ");
					change_prod[2] = sc.nextLine();

					setBarcode(change_prod[0], change_prod[1], change_prod[2]);
					bufferedWriter.flush();

					
					close();
					
					try {
						Thread.sleep(1000);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
					
					open();
					change_prod[0] = "sell";
					System.out.print("교환할 상품 바코드 번호 : ");
					change_prod[1] = sc.nextLine();
					System.out.print("수량 : ");
					change_prod[2] = sc.nextLine();

					setBarcode(change_prod[0], change_prod[1], change_prod[2]);
					change_prod[0] = "fin";
					bufferedWriter.write(change_prod[0]);
					bufferedWriter.newLine();

					bufferedWriter.flush();
					

					// // 입력받은 내용을 서버 콘솔에 출력
					setTableForm(bufferedReader);
					close();
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

	/** 재고관리 페이지 함수 **/
	public void func_check() {
		int swValue;

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
					open();
					
					prod[0] = "check";

					// 서버로 값 전송
					bufferedWriter.write(prod[0]);
					bufferedWriter.newLine();

					bufferedWriter.flush();

					setTableForm(bufferedReader);
					close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			case 2:
				System.out.println("★특정 품목 조회★");
				prod = new String[3];
				try {
					open();
					
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
					close();
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

	/** 테이블 렌더링 함수 **/
	public void setTableForm(BufferedReader bufferedReader) {
		// 출력 폼
		String leftAlignFormat = "| %-s\t | %-s\t | %-s\t | %-s\t | %-s\t |%n";

		System.out.format("------------------------------------------------------%n");
		System.out.println("바코드"+"\t\t"+"품명"+"\t\t"+"가격"+"\t"+"중량"+"\t"+"수량");
		System.out.format("------------------------------------------------------%n");


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
						System.out.println(prod_list[0]+"\t\t"+prod_list[1]+"\t\t"+prod_list[2]+"\t"+prod_list[3]+"\t"+prod_list[4]);
						i = 0;
						break;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.format("------------------------------------------------------%n");
	}
	
	/** 서버에 보낼 바코드 및 수량 write **/
	public void setBarcode(String flag, String code, String quantity) {
		try {
			bufferedWriter.write(flag);
			bufferedWriter.newLine();
			bufferedWriter.write(code);
			bufferedWriter.newLine();
			bufferedWriter.write(quantity);
			bufferedWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Socket, bufferedWriter, bufferedReader 열기 **/
	public void open() throws Exception, IOException {
		socket = new Socket(IP, PORT_NUM);
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	/** Socket, bufferedWriter, bufferedReader 닫기 **/
	public void close() {
		try {
			bufferedWriter.close();
			bufferedReader.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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