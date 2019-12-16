package br.com.proxy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import br.com.proxy.utils.KeyStoreUtils;

public class Bootstrap {

	public static ServerConnector buildServerConnector(Server server, int httpPort, int httpsPort)
	{
        HttpConfiguration httpConfiguration = new HttpConfiguration();
        httpConfiguration.setSecureScheme("https");
        httpConfiguration.setSecurePort(httpsPort);

        // HTTP Connector
        ServerConnector connector = new ServerConnector(server,new HttpConnectionFactory(httpConfiguration), new HTTP2CServerConnectionFactory(httpConfiguration));
        connector.setPort(httpPort);
        server.addConnector(connector);
        
        SslContextFactory sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath("src/main/resources/keystore.jks");
        sslContextFactory.setKeyStorePassword("changeit");
        sslContextFactory.setKeyManagerPassword("changeit");
        sslContextFactory.setTrustStore(KeyStoreUtils.createKeyStore("src/main/resources/truststore"));
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

        // HTTPS Configuration
        HttpConfiguration httpsConfiguration = new HttpConfiguration(httpConfiguration);
        httpsConfiguration.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpsConfiguration);

        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol(connector.getDefaultProtocol());

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector = new ServerConnector(server, ssl, alpn, h2, new HttpConnectionFactory(httpsConfiguration));
        http2Connector.setPort(httpsPort);
        
        return http2Connector;
	}
	
	public static HandlerList buildServerHandlers()
	{
        HandlerList handlers = new HandlerList();
        
        ContextHandler contextHandlerDomain1 = new ContextHandler();
        contextHandlerDomain1.addVirtualHosts(new String[] {"domain1.test"});
        contextHandlerDomain1.setHandler(Bootstrap.buildhandler("Hello from domain 1!"));
        
        ContextHandler contextHandlerDomain2 = new ContextHandler();
        contextHandlerDomain2.addVirtualHosts(new String[] {"domain2.test"});
        contextHandlerDomain2.setHandler(Bootstrap.buildhandler("Hello from domain 2!"));
        
        handlers.addHandler(contextHandlerDomain1);        
        handlers.addHandler(contextHandlerDomain2);

        return handlers;
	}
	
	private static AbstractHandler buildhandler(final String message)
	{
		return new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException {
				response.setContentType("text/html;charset=utf-8");
		        response.setStatus(HttpServletResponse.SC_OK);
		        baseRequest.setHandled(true);
		        response.getWriter().println("<h1>".concat(message).concat("</h1>"));				
			}
		};
	}
}