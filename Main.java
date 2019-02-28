package mapMethod;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
		int choose = 0;
		while (choose != 3) {
			System.out.println(
					"please enter 1 if you want zip and enter 2 if you want unzip or enter 3 if you want exit");
			choose = sc.nextInt();
			if (choose == 1) {
				ArrayList<Pair<Integer, String>> pairs = new ArrayList<Pair<Integer, String>>();
				ArrayList<Integer> length = new ArrayList<Integer>();
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				System.out.println("please enter path to input file for zipping");
				String pathRead = sc.next();
				while (!proof(pathRead)) {
					System.out.println("please enter correct name for file");
					pathRead = sc.next();
				}
				DataInputStream reader = new DataInputStream(new FileInputStream(pathRead));
				int current;
				String temp = "";
				System.out.println("please enter path to outputfile");
				String pathWrite = sc.next();
				Path path = Paths.get(pathWrite);
				while (!Files.exists(path)) {
					pathWrite = sc.next();
				}
				Byte cha;
				BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(pathWrite));
				long start = System.currentTimeMillis();
				while (reader.available() > 0) {
					cha = reader.readByte();
					int t = cha & 0xFF;
					temp = temp + Character.toString((char) t);
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
				reader.close();
				if (map.get(temp) != null && !temp.equals("")) {
					pairs.add(new Pair<Integer, String>(map.get(temp), "end"));
					int k = map.get(temp);
					length.add(numberBits(k));
				} else {

				}
				temp = "";
				StringBuffer res = new StringBuffer();
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
				StringBuffer cur = new StringBuffer();
				int counter = 0;
				for (int i = 0; i < res.length(); i++) {
					cur.append(res.charAt(i));
					counter++;
					if (counter == 8 || ((i == res.length() - 1) && (cur.length() != 0))) {
						counter = 0;
						while (cur.length() != 8) {
							cur.append("0");
						}
						writer.write(convertToInt(cur));
						cur = new StringBuffer();
					}
				}
				writer.close();
				long finish = System.currentTimeMillis() - start;
				double time = (double) finish / 1000;
				long sizeInput = new File(pathRead).length();
				System.out.println("time execute for zipping=" + time + "seconds");
				System.out.println("size input file = " + new File(pathRead).length() + "bytes");
				System.out.println("size output file = " + new File(pathWrite).length() + "bytes");
				System.out.println("speed coding bytes in second = " + sizeInput / time);
			} else if (choose == 2) {
				ArrayList<Pair<Integer, String>> pairs = new ArrayList<Pair<Integer, String>>();
				LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
				System.out.println("please enter path to input file for unzipping");
				String pathRead = sc.next();
				while (!proof(pathRead)) {
					System.out.println("please enter correct name for file");
					pathRead = sc.next();
				}
				DataInputStream reader = new DataInputStream(new FileInputStream(pathRead));
				Byte ch;
				int c;
				StringBuffer temp = new StringBuffer();
				System.out.println("please enter output file");
				String pathWrite = sc.next();
				Path path = Paths.get(pathWrite);
				while (!Files.exists(path)) {
					pathWrite = sc.next();
				}
				BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(pathWrite));
				long start = System.currentTimeMillis();
				while (reader.available() > 0) {
					ch = reader.readByte();
					c = ch & 0xFF;
					temp.append(addZero(convertToBitString(c)));
				}
				reader.close();
				int order = 0;
				int counter = 0;
				int numberInMap = -100;
				int first = -100;
				int second = 0;
				StringBuffer current = new StringBuffer();
				for (int i = 0; i < temp.length(); i++) {
					current.append(temp.charAt(i));
					counter++;
					if (order == 0 && counter == 8) {
						order++;
						counter = 0;
						numberInMap = convertToInt(current);
						current = new StringBuffer();
					} else if (order == 1 && counter == numberInMap) {
						order++;
						counter = 0;
						numberInMap = -100;
						first = convertToInt(addZero(current));
						current = new StringBuffer();
					} else if (order == 2 && counter == 8) {
						order = 0;
						counter = 0;
						second = convertToInt(current);
						pairs.add(new Pair<Integer, String>(first, Character.toString((char) second)));
						first = -100;
						current = new StringBuffer();
					}
				}
				counter = 1;
				for (int i = 0; i < pairs.size(); i++) {
					if (pairs.get(i).getFirst() == 0) {
						map.put(counter, pairs.get(i).getSecond());
						counter++;
						writer.write(pairs.get(i).getSecond().charAt(0));
					} else {
						map.put(counter, map.get(pairs.get(i).getFirst()) + pairs.get(i).getSecond());
						counter++;
						String cur = map.get(pairs.get(i).getFirst()) + pairs.get(i).getSecond();
						for (int j = 0; j < cur.length(); j++) {
							writer.write(cur.charAt(j));
						}
					}
				}
				if (first != -100) {
					String cur = map.get(first);
					for (int j = 0; j < cur.length(); j++) {
						writer.write(cur.charAt(j));
					}
				}
				writer.close();
				long finish = System.currentTimeMillis() - start;
				double time = (double) finish / 1000;
				long sizeInput = new File(pathRead).length();
				System.out.println("time execute for zipping=" + time + "seconds");
				System.out.println("size input file = " + new File(pathRead).length() + "bytes");
				System.out.println("size output file = " + new File(pathWrite).length() + "bytes");
				System.out.println("speed coding bytes in second = " + sizeInput / time);
			} else if (choose == 3) {
				System.out.println("exit success execute");
				sc.close();
			} else {
				System.out.println("please enter corret argument");
			}
		}
	}
}
