package data;

public class Person {
	private int year;
	private int papers;
	private String name;
	
	public Person(String name) {
		this.name = name;
		papers = 0;
		setYear(2016);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getPapers() {
		return papers;
	}

	public void incPapers() {
		this.papers++;
	}
	
	public void setPapers(int paper) {
		this.papers = paper;
	}

	public String getName() {
		return name;
	}
}
