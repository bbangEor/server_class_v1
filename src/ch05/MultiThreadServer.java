package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {
	// 메인함수
	public static void main(String[] args) {

		System.out.println(" ===== 서버 실행 ===== ");

		// 서버측 소켓을 만들기 위한 준비물
		// 서버소켓 , 포트번호

		try (ServerSocket serverSocket = new ServerSocket(5000)) {

			Socket socket = serverSocket.accept();
			// 클라이언트 대기 -- > 연결 요청이 오게되면
			// -- > 소켓객체를 생성 (클라이언트와 연결된상태)
			System.out.println(" ------ client connected ------ ");

			// 클라이언트와 통신을 위한 스트림을 설정해야함 ( 대상소켓을 얻은 후 )
			BufferedReader readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			PrintWriter writerStream = new PrintWriter(socket.getOutputStream(), true);// AUTO FLUSH

			// 키보드스트림 준비
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			// 스레드를 시작합니다.
			startReadThread(readerStream);
			startWriteThread(writerStream, keyboardReader);
			
			// 안정적인 종료를 위해서 굳이 JOIN 안써도됨
			System.out.println(" MainThread 작업 완료 ...");

		} catch (Exception e) {
			// TODO: handle exception
		}

	} // end of main
		///////////////////////////////////

	// 클라이언트로 부터 데이터를 읽는 스레드 분리
	private static void startReadThread(BufferedReader bufferedReader) {
		Thread readThread = new Thread(() -> {
			try {

				String clientMessage;
				while ((clientMessage = bufferedReader.readLine()) != null)
					;
				// 서버측 콘솔에 클라이언트가 보낸 문자 데이터출
				System.out.println("클라이언트 에게서 온 메세지 : " + clientMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		// 메인 스레드 대기처리 -> join() 메서드 실행 /반복 2번 예상
		readThread.start(); // 스레드 실행 -> run() 메서드 진행
		//waitForThreadToEnd(readThread); // readThread 가 종료될때까지 메인스레드를 대기
	}

	// 서버 측 -> 클라이언트 방향으로 데이터를 보내는 기능
	private static void startWriteThread(PrintWriter printWriter, BufferedReader keyboardReader) {

		Thread writeThread = new Thread(() -> {

			try {
				String serverMessage;
				while ((serverMessage = keyboardReader.readLine()) != null) {
					printWriter.println(serverMessage);
					printWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		// 메인스레드 대기
		writeThread.start();
		//waitForThreadToEnd(writeThread);

	}

	// 워커스레드가 종료 될때 까지 대기하는 메서드
	private static void waitForThreadToEnd(Thread thread) {

		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

} // end of class
