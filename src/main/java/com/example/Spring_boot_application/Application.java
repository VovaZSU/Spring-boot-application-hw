package com.example.Spring_boot_application;

import com.example.Spring_boot_application.model.Book;
import com.example.Spring_boot_application.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class Application {

	@Autowired
	private BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				Book book = new Book();
				book.setTitle("My way to Java");
				book.setAuthor("Volodymyr Havrylenko");
				book.setIsbn("01");
				book.setPrice(BigDecimal.valueOf(1000000));
				book.setDescription("Very interesting story");
				book.setCoverImage("War and java");
				bookService.save(book);
				System.out.println(bookService.findAll());
			}
		};
	}
}
