package br.com.proxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;
import io.undertow.util.Headers;

public class Bootstrap {

	public static Undertow bootstrapServer(int port, String host) throws Exception
	{
		Undertow server = Undertow
							.builder()
							.addHttpListener(port, host)
							.setHandler(Bootstrap.buildServerHandler())
							.build();
		server.start();
		
		return server;
	}
	
	public static Undertow bootstrapProxy(int port, String host, List<String> servers) throws Exception
	{
		LoadBalancingProxyClient loadBalancer = bootstrapLoadBalancer(servers, 20);
		Undertow proxy = Undertow
							.builder()
							.addHttpListener(port, host)
							.setHandler(ProxyHandler.builder()
													.setProxyClient(loadBalancer)
													.setMaxRequestTime(30000)
													.build())
							.build();
		proxy.start();
		
		return proxy;
	}
	
	public static LoadBalancingProxyClient bootstrapLoadBalancer(List<String> hosts, int connectionsPerThread) throws URISyntaxException
	{
		LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient();

		for(String host : hosts)
			loadBalancer.addHost(new URI(host));

		loadBalancer.setConnectionsPerThread(connectionsPerThread);
		
		return loadBalancer;
	}
	
	public static HttpHandler buildServerHandler()
	{
		return new HttpHandler() {
			public void handleRequest(HttpServerExchange exchange) throws Exception {
				exchange.getResponseHeaders().put
				(Headers.CONTENT_TYPE, "text/plain");
				exchange.getResponseSender().send("Hello World");
			}
		};
	}
}