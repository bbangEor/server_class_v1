package ch02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFile {

	public static void main(String[] args) {

		// 준비물
		// 1. 서버 소켓
		// 2. 포트 번호 ( 0~65535 까지 존재 )
		// 2-1. 잘알려진 포트번호 : 주로 시스템 레벨 ( 0~ 1023)
		// 2-2. 등록 가능한 포트번호 : 1024 ~ 49151
		// 2-3. 동적 사설 포트번호 - 그외 임시 사용을 위해 할당된다.

		// 지역변수
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(5001);
			System.out.println("서버를 시작합니다.  포트번호 : 5001 ");

			Socket socket = serverSocket.accept();
			System.out.println(">>> 클라이언트가 연결하였습니다. <<<");

			// 데이터를 전달 받기 위해선 Input 스트림이 필요하다.
			InputStream input = socket.getInputStream();
			// 문자 기반 스트림
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			// 실제 데이터를 읽는 행위가 필요
			String message = reader.readLine();
			System.out.println("클라이언트 측 메세지 전달받음 : " + message );
			
			socket.close(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // return 보다 강한 반드시 수행되어야하는 구문
			
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}

	}

}