package br.com.proxy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

	public static void execute(String command, boolean shouldPrint) 
	{
		Runtime runtime = Runtime.getRuntime();

		Process process = null;

		try {
			process = runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

		if (shouldPrint) {
			String s = null;

			try {
				while ((s = stdInput.readLine()) != null)
					System.out.println(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
