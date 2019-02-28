package mapMethod;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class file {
	public static boolean proof(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static int convertToInt(StringBuffer str) {
		int k = 0;
		for (int i = 0; i < str.length(); i++) {
			k = k + (int) Math.pow(2, str.length() - i - 1) * (str.charAt(i) - 48);
		}
		return k;
	}

	public static StringBuffer convertToBitString(int i) {
		StringBuffer result = new StringBuffer("");
		while (i != 0) {
			result = result.append(Integer.toString(i % 2));
			i = i / 2;
		}
		if (!result.toString().equals("")) {
			return result.reverse();
		} else {
			return new StringBuffer("0");
		}
	}

	public static int numberBits(int current) {
		int t = 0;
		while (current >= Math.pow(2, t)) {
			t++;
		}
		return t;
	}

	public static StringBuffer addZero(StringBuffer current) {
		StringBuffer zeroes = new StringBuffer();
		while (current.length() + zeroes.length() < 8) {
			zeroes.append("0");
		}
		zeroes.append(current);
		return zeroes;
	}
	public static void main(String args[]) throws IOException {
		BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream("C:\\test\\random.txt"));
		//writer.write(152);
		//writer.close();
		DataInputStream reader = new DataInputStream(new FileInputStream("C:\\test\\rara.txt"));
		Byte ch;
		int c = 0;
		while (reader.available() > 0) {
			ch = reader.readByte();
			c = ch & 0xFF;
			System.out.println(c);
			writer.write(c);
		}
		reader.close();
		System.out.println(convertToInt(new StringBuffer("10011000")));
	}
}
