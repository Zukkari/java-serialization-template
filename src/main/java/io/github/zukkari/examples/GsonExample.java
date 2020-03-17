package io.github.zukkari.examples;

import com.google.gson.Gson;
import io.github.zukkari.data.Book;
import io.github.zukkari.generator.BookGenerator;

public class GsonExample {
	public static void main(String[] args) {
		// Create book that we want to serialize
		final Book book = BookGenerator.generate();

		// Create an instance of class that will serialize our book
		final Gson gson = new Gson();

		// Serialize the book
		final String bookAsJson = gson.toJson(book);

		// Print out the resulting book in JSON format
		System.out.println(bookAsJson);
	}
}
