package ch05;

import java.io.IOException;
import java.net.ServerSocket;

public class MyThreadServer extends AbstractServer{

	@Override
	protected void setupServer() throws IOException {
		// 추상클래스 -> 부모의클래스 -> 자식은 부모기능에 확장 or 사용 가능
		// 서버측 소켓 통신을 하기위해선 서버 소켓이 필요하다.
		super.setServerSocket(new ServerSocket(5000));
		System.out.println(">>> Server started on port 5000 <<<");
	}

	@Override
	protected void connection() throws IOException {
		// 서버소켓.accept() 호출
		super.setSocket(super.getServerSocket().accept());
	}
	
	public static void main(String[] args) {
		MyThreadServer myThreadServer = new MyThreadServer();
		myThreadServer.run();
	}

}
