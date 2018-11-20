package com.baoding.notebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

@SpringBootApplication
public class NotebookApplication {
	// 监听控制台,获取用户输入的内容,作为springboot启动时使用的端口号
	public static String port;
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		port = scanner.nextLine();
		new SpringApplicationBuilder(NotebookApplication.class).properties("server.port="+port).run(args);
	}

//	public static void main(String[] args) {
//		SpringApplication.run(NotebookApplication.class, args);
//	}
}
