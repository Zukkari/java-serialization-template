package io.github.zukkari.examples.object;

import io.github.zukkari.data.Book;
import io.github.zukkari.generator.BookGenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ObjectStreamExample {
    public static void main(String[] args) throws Exception {
        Path tempFile = Files.createTempFile("java_object", null);
        System.out.printf("Writing to file: %s\n", tempFile.toAbsolutePath());

        Book book = BookGenerator.generate();

        // Write the object to file
        try (var stream = new ObjectOutputStream(new FileOutputStream(tempFile.toFile()))) {
            stream.writeObject(book);
        }

        try (var stream = new ObjectInputStream(new FileInputStream(tempFile.toFile()))) {
            // Read the object back from the stream
            // and cast the object to the book instance
            final Book inputBook = (Book) stream.readObject();

            System.out.println(inputBook);
        }
    }
}
