package br.com.proxy;

import org.eclipse.jetty.server.Server;

public class HttpServer
{
    Server server;
	
    public HttpServer(int httpPort, int httpsPort) throws Exception
    {
        this.server = new Server();
        server.setHandler(Bootstrap.buildServerHandlers());
        server.addConnector(Bootstrap.buildServerConnector(server, httpPort, httpsPort));
        server.start();
        server.join();
    }
}
