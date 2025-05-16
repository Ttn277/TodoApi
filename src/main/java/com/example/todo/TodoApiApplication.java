package com.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApiApplication.class, args);
	}

}


// SpringApplication.run() sẽ:
// Khởi tạo Spring context
// Tạo các bean
// Cấu hình web server (Mặc định là Tomcat)
// Bắt đầu lắng nghe request HTTP trên cổng mạc định (8080)


