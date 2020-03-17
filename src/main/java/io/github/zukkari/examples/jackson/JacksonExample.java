package io.github.zukkari.examples.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zukkari.data.Book;
import io.github.zukkari.generator.BookGenerator;

public class JacksonExample {
	public static void main(String[] args) throws Exception {
		// Create book that we want to serialize
		final Book book = BookGenerator.generate();

		// Create an instance of class that will serialize our book
		final ObjectMapper objectMapper = new ObjectMapper();

		// Serialize the book
		final String bookAsJson = objectMapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(book);

		// Print out the resulting book in JSON format
		System.out.println(bookAsJson);
	}
}
