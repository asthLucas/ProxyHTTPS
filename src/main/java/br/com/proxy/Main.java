package br.com.proxy;

import java.util.Arrays;

public class Main {
	
	public static void main(String[] args) throws Exception
	{
		Bootstrap.bootstrapServer(8081, "localhost");
		Bootstrap.bootstrapProxy(8080, "localhost", Arrays.asList("http://localhost:8081"));
	}
}