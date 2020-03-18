package io.github.zukkari.data;

import java.io.Serializable;
import java.util.List;

public class Page implements Serializable {
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

    @Override
    public String toString() {
        return "Page{" +
                "number=" + number +
                ", lines=" + lines +
                '}';
    }
}
