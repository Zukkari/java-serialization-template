package io.github.zukkari.generator;

import io.github.zukkari.data.Book;
import io.github.zukkari.data.Line;
import io.github.zukkari.data.Page;

import java.util.ArrayList;
import java.util.UUID;

public class BookGenerator {

	/**
	 * Generates us a {@link Book} that we can use for the test purpose
	 *
	 * @return instance of {@link Book} with pages and lines filled.
	 */
	public static Book generate() {
		final Book book = new Book();

		book.setAuthor("Book author");
		book.setTitle("Book title");

		var pages = new ArrayList<Page>();
		for (int i = 0; i < 2; i++) {
			final Page page = new Page();
			page.setNumber(i);

			var lines = new ArrayList<Line>();
			for (int j = 0; j < 2; j++) {
				final Line line = new Line();
				line.setNumber(j);
				line.setText(UUID.randomUUID().toString());

				lines.add(line);
			}

			page.setLines(lines);
			pages.add(page);
		}

		book.setPages(pages);
		return book;
	}

}
