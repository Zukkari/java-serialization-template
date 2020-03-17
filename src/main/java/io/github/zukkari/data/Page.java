package io.github.zukkari.data;

import java.util.List;

public class Page {
	private int number;
	private List<Line> lines;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
}
