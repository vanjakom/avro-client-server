package com.mungolab.rpc.server;

import com.mungolab.protocol.SimpleProtocol;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Vanja Komadinovic ( vanjakom@gmail.com )
 */
public class SimpleProtocolServer {
    private static final Logger logger = LoggerFactory.getLogger(SimpleProtocolServer.class);

    private Server server;

    public void startServer(int port, SimpleProtocol streamingProtocolImpl) {
        server = new NettyServer(new SpecificResponder(SimpleProtocol.class, streamingProtocolImpl),
                new InetSocketAddress(port));

        server.start();

        logger.info("Started Avro server on port: " + port);
    }

    public void stopServer() {
        server.close();

        logger.info("Closed Avro server");
    }
}
