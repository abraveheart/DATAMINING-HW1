package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class Utils {
	public static BufferedReader getReader(String filename) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return reader;
	}
	
	public static BufferedWriter getWriter(String filename) {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer;
	}
}
