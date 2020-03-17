package io.github.zukkari.examples.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.zukkari.data.Book;
import io.github.zukkari.generator.BookGenerator;

public class JacksonXMLExample {
    public static void main(String[] args) throws Exception {
        // Create book that we want to serialize
        final Book book = BookGenerator.generate();

        // Create an instance of class that will serialize our book
        // Note that we now use XMLMapper instead of ObjectMapper
        final ObjectMapper objectMapper = new XmlMapper();

        // Serialize the book
        final String bookAsJson = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(book);

        // Print out the resulting book in JSON format
        System.out.println(bookAsJson);
    }
}
