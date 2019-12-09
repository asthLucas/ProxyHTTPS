package br.com.proxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.xnio.OptionMap;
import org.xnio.Xnio;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.protocols.ssl.SNISSLContext;
import io.undertow.protocols.ssl.UndertowXnioSsl;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;
import io.undertow.util.Headers;

public class Bootstrap {

	public static Undertow bootstrapServer(int port, String host, Map<String, SNISSLContext> domainSSLContext) throws Exception
	{
		Undertow server = Undertow
							.builder()
							.addHttpsListener(port, host, domainSSLContext.get(host), Bootstrap.buildServerHandler())
							.build();

		server.start();

		return server;
	}
	
	public static Undertow bootstrapProxy(int port, String host, List<String> servers, Map<String, SNISSLContext> domainSSLContext) throws Exception
	{
		Undertow proxy = Undertow
							.builder()
							.addHttpsListener(port, host, domainSSLContext.get(host))
							.setHandler(buildProxyHandler(servers, domainSSLContext))
							.build();
		proxy.start();
		
		return proxy;
	}
	
	public static LoadBalancingProxyClient bootstrapLoadBalancer(List<String> hosts, Map<String, SNISSLContext> domainSSLContext) throws URISyntaxException
	{
		LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient();
		
		for(String host : hosts)
			loadBalancer.addHost(new URI(host), new UndertowXnioSsl(Xnio.getInstance(), OptionMap.EMPTY, domainSSLContext.get(host)));
				
		loadBalancer.setConnectionsPerThread(20);
		
		return loadBalancer;
	}
	
	public static HttpHandler buildProxyHandler(List<String> servers, Map<String, SNISSLContext> domainSSLContext) throws URISyntaxException
	{
		HttpHandler handler = ProxyHandler.builder()
				.setProxyClient(bootstrapLoadBalancer(servers, domainSSLContext))
				.build();
		
		return handler;
	}
	
	public static HttpHandler buildServerHandler()
	{
		return Handlers.virtualHost()
				.addHost("domain1.localhost", new HttpHandler() {
					public void handleRequest(HttpServerExchange exchange) throws Exception {
						exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
						exchange.getResponseSender().send("Hello from domain1!");
					}
				}).addHost("domain2.localhost", new HttpHandler() {
					public void handleRequest(HttpServerExchange exchange) throws Exception {
						exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
						exchange.getResponseSender().send("Hello from domain2!");
				}
		});
	}
}