package io.github.zukkari.data;

import java.io.Serializable;

public class Line implements Serializable {
	private int number;
	private String text;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

    @Override
    public String toString() {
        return "Line{" +
                "number=" + number +
                ", text='" + text + '\'' +
                '}';
    }
}
