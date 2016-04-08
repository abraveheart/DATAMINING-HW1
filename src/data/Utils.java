package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
	public static BufferedReader getReader(String filename) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return reader;
	}
	
	public static BufferedWriter getWriter(String filename) {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer;
	}
}
