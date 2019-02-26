package mapMethod;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class Main {
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
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter 1 if you want zip and enter 2 if you want unzip");
		int choose = sc.nextInt();
		if (choose == 1) {
			ArrayList<Pair<Integer, String>> pairs = new ArrayList<Pair<Integer, String>>();
			ArrayList<Integer> length = new ArrayList<Integer>();
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
			System.out.println("please enter path to input file for zipping");
			String pathRead = sc.next();
			while (!proof(pathRead)) {
				System.out.println("please enter correct name for file");
				pathRead = sc.next();
			}
			FileReader reader = new FileReader(pathRead);
			int current;
			String temp = "";
			while ((current = reader.read()) != -1) {
				temp = temp + Character.toString((char) current);
				if (map.get(temp) == null && temp.equals("") == false) {
					map.put(temp, map.size() + 1);
					if (temp.length() == 1) {
						pairs.add(new Pair<Integer, String>(0, Character.toString(temp.charAt(0))));
						length.add(1);
					} else if (temp.length() > 1) {
						int k = map.get(temp.substring(0, temp.length() - 1));
						pairs.add(new Pair<Integer, String>(map.get(temp.substring(0, temp.length() - 1)),
								Character.toString(temp.charAt(temp.length() - 1))));
						length.add(numberBits(k));
					}
					temp = "";
				}
			}
			if (map.containsKey(temp) && temp != "") {
				pairs.add(new Pair<Integer, String>(map.get(temp), "end"));
				int k = map.get(temp);
				length.add(numberBits(k));
			}
			System.out.println(map.toString());
			for (int i = 0; i < pairs.size(); i++) {
				System.out.println(pairs.get(i).getFirst() + " " + pairs.get(i).getSecond());
			}
			System.out.println("please enter path to outputfile");
			temp = "";
			String pathWrite = sc.next();
			Path path = Paths.get(pathWrite);
			while (!Files.exists(path)) {
				pathWrite = sc.next();
			}
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(pathWrite));
			StringBuffer res = new StringBuffer();
			StringBuffer proof = new StringBuffer();
			for (int i = 0; i < length.size(); i++) {
				res = res.append(addZero(convertToBitString(length.get(i))));
				res = res.append(convertToBitString(pairs.get(i).getFirst()));
				StringBuffer ch = new StringBuffer(pairs.get(i).getSecond());
				if (!ch.toString().equals("end")) {
					int c = (int) ch.charAt(0);
					ch = addZero(convertToBitString(c));
					res.append(ch);
				}
			}
			int p = 0;
			int m = 8 - res.length() % 8;
			StringBuffer cur = new StringBuffer();
			writer.write(m);
			int counter = 0;
			System.out.println(8 - res.length() % 8);
			for (int i = 0; i < res.length(); i++) {
				cur.append(res.charAt(i));
				counter++;
				if(counter == 8 || ((i == res.length() - 1) && (cur.length()!=0))) {
					counter = 0;
					System.out.println(cur);
					while(cur.length() != 8) {
						cur.append("0");
					}
					writer.write(convertToInt(cur));
					cur = new StringBuffer();
				}
			}
			writer.close();
			System.out.println(res.substring(p, res.length()));
			for (int i = 0; i < pairs.size(); i++) {
				 System.out.println(pairs.get(i).getFirst() + " " + pairs.get(i).getSecond());
			 }
		} else if (choose == 2) {
			ArrayList<Pair<Integer, String>> pairs = new ArrayList<Pair<Integer, String>>();
			ArrayList<Integer> length = new ArrayList<Integer>();
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
			System.out.println("please enter path to input file for zipping");
			String pathRead = sc.next();
			while (!proof(pathRead)) {
				System.out.println("please enter correct name for file");
				pathRead = sc.next();
			}
			DataInputStream reader = new DataInputStream(new FileInputStream(pathRead));
			Byte ch;
			int c;
			int useless = 0;
			ch = reader.readByte();
			useless = ch & 0xFF;
			StringBuffer temp = new StringBuffer();
			while (reader.available() > 0) {
				ch = reader.readByte();
				c = ch & 0xFF;
				temp.append(addZero(convertToBitString(c)));
			}
			System.out.println(useless);
			System.out.println(temp);
			System.out.println(temp);
			int order = 0;
			int counter = 0;
			int numberInMap = -100;
			int first = 0;
			int second = 0;
			StringBuffer current = new StringBuffer();
			/*
			 * String pathWrite = sc.next(); Path path = Paths.get(pathWrite); while
			 * (!Files.exists(path)) { pathWrite = sc.next(); } BufferedOutputStream writer
			 * = new BufferedOutputStream(new FileOutputStream(pathWrite));
			 */
			System.out.println(temp.length());
			for (int i = 0; i < temp.length(); i++) {
				current.append(temp.charAt(i));
				counter++;
				if (order == 0 && counter == 8) {
					order++;
					counter = 0;
					numberInMap = convertToInt(current);
					System.out.println("counter" + current);
					current = new StringBuffer();
				} 
				else if (order == 1 && counter == numberInMap) {
					order++;
					counter = 0;
					numberInMap = -100;
					first = convertToInt(addZero(current));
					System.out.println("counter" + current);
					current = new StringBuffer();
				}
				else if (order == 2 && counter == 8) {
					order = 0;
					counter = 0;
					second = convertToInt(current);
					pairs.add(new Pair<Integer, String>(first, Character.toString((char) second)));
					System.out.println("counter" + current);
					current = new StringBuffer();
				}
			}
			 for (int i = 0; i < pairs.size(); i++) {
				 System.out.println(pairs.get(i).getFirst() + " " + pairs.get(i).getSecond());
			 }
		} else {

		}
	}
}
