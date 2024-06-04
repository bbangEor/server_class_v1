package ch07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MultiClientServer {

	private static final int PORT = 5000; // 포트번호 : 5000

	private static Vector<PrintWriter> clientWriters = new Vector<>();

	public static void main(String[] args) {
		System.out.println("Server stared ...");

		try (ServerSocket serverSocket = new ServerSocket(PORT)) {

			while (true) {
				Socket socket = serverSocket.accept();
				new ClientHandler(socket).start();
			}
		} catch (Exception e) {

		}

	} // end of main

	// 정적 내부 클래스 설계
	private static class ClientHandler extends Thread {

		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		// Thread start 호출 시 동작되는 메서드
		@Override
		public void run() {

			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				// 코드 추가
				// 클라이언트로부터 이름 받기 (약속되어있다.)
				String nameMessage = in.readLine();
				if (nameMessage != null && nameMessage.startsWith("NAME : ")) {
					String clientName = nameMessage.substring(5); // substring -> 문자열을 잘라내는 기능
					// 5 다음에 들어오는 데이터가 담기게됨
					broadcastMessage("서버에  " + clientName + " 님 입장");
				} else {
					// 약속과 다르게 접근했다면 , 종료 처리
					socket.close();
					return;
				}

				// @@ ! 중요 ! @@ - 서버가 관리하는 자료구조에 자원(클라이언트와 연결된 소켓안의 OutputStream) 저장
				clientWriters.add(out);
				String message;
				while ((message = in.readLine()) != null) {
					System.out.println("Received : " + message);

					// 클라이언트와 서버의 약속 !
					// ' : ' 기준 으로 처리 or ' / ' 기준
					// 클라이언트 msg : 안녕\n
					String[] parts = message.split(":", 2); // 자르다
					System.out.println(" parts 인덱스 갯수 : " + parts.length);
					// 명령 부분을 분리
					String command = parts[0];
					// 데이터 부분을 분리
					String data = parts.length > 1 ? parts[1] : ""; // true = 1번째 데이터 값이 data에 들어감

					if (command.equals("MSG")) {
						System.out.println("연결된 전체 사용자에게  MSG 방송");
						broadcastMessage(message);
					} else if (command.equals("BYE")) {
						System.out.println("Client disconnected...");
						break; // while 구문종료
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
					// 서버 측에서 관리하고 있는 P.W 를 제거해야한다.
					// index 번호가 필요하다 .
					// clientWriters.add() 할때 , 지정된 나의 인덱스 번호가 필요하다.
					// clientWriters.remove();
					System.out.println(" ...... 클라이언트 연결 해제 ...... ");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	} // end of ClientHandler

	// 모든 클라이언트에게 메세지 보내기 - 브로드캐스트
	private static void broadcastMessage(String message) {
		for (PrintWriter writer : clientWriters) {
			writer.println(message);
		}
	}

} // end of class
