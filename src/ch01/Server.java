package ch01;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {

		// 서버측 소켓 통신을 만들기 위한 준비물
		// 1. ServerSocket (클라이언트 측 소켓과 연결만 시켜준다)
		// 2. 클라이언트와 연결되는 소켓을 들고 있어야한다.

		// 로컬 컴퓨터에는 정해진, 사용할 수 있는
		// 포트 번호 개수가 할당되어있다.
		// 1~1024 포트 번호는 잘 알려진 포트번호이며,
		// 시스템이 선점하고 있는 번호들임

		try (ServerSocket serverSocket = new ServerSocket(5000)) {
			System.out.println("서버포트번호 : 5000 으로 생성");

			// 내부 메서드 안에서 while 문을 돌면서
			// 클라이언트측에 연락을 기다리고있다.
			Socket socket = serverSocket.accept();
			// 여기 아래는 클라이언측과 양 끝단에 소켓이
			// 서로 연결되어야 실행흐름이 내려온다.
			System.out.println("Client connected ...");
			// 대상 - 소켓 -> 입력 스트림을 가져온다.
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String message = reader.readLine(); // 한줄단위로 데이터를 읽어라 (문자기반)
			System.out.println("Received : " + message);

			// 기본 소켓은 클라이언트가 연결되어야 생성된다.
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
