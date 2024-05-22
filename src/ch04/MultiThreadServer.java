package ch04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {
	// 메인함수
	public static void main(String[] args) {

		System.out.println("==== 서버 실행 ==== ");

		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(5001);
			socket = serverSocket.accept();
			System.out.println("포트번호 - 5001 할당완료");

			// 클라이언트의 데이터를 받을 입력 스트림 필요
			// 클라이언트에 데이터를 보낼 출력 스트림 필요
			// 서버측 - 키보드 입력을 받기 위한 입력 스트림 필요

			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			// 멀티스레딩 개념의 확장
			// 클라이언트로 부터 데이터를 읽는 스레드
			Thread readThread = new Thread(() -> {

				try {
					String clientMessage;
					while ((clientMessage = socketReader.readLine()) != null) {
						System.out.println("서버측 콘솔 : " + clientMessage);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			});

			// 클라이언트에게 데이터를 보내는 스레드
			Thread writeThread = new Thread(() -> {
				try {
					String serverMessage;
					while ((serverMessage = keyboardReader.readLine()) != null) {
						// 1. 키보드를 통해서 데이터를 읽음
						// 2. 출력스트림을 활용하여 데이터를 보낸다.
						socketWriter.println(serverMessage);
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			});

			// 스레드 동작 -> start() 호출
			readThread.start();
			writeThread.start();
			
			readThread.join();
			writeThread.join();

			System.out.println(" --- 서버프로그램 종료 ---");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
