package com.mungolab.rpc.client;

import com.mungolab.protocol.ResponseStatus;
import com.mungolab.protocol.SimpleProtocol;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Vanja Komadinovic ( vanjakom@gmail.com )
 */
public class SimpleProtocolClient implements SimpleProtocol {
    private static Logger logger = LoggerFactory.getLogger(SimpleProtocolClient.class);

    private NettyTransceiver client;
    private SimpleProtocol proxy;

    public SimpleProtocolClient connect(String host, int port) {
        try {
            client = new NettyTransceiver(new InetSocketAddress(host, port));
            proxy = (SimpleProtocol)SpecificRequestor.getClient(SimpleProtocol.class, client);

            return this;
        } catch (Exception e) {
            logger.error("Unable to connect to server", e);
            return null;
        }
    }

    public void close() {
        client.close();
    }

    @Override
    public ResponseStatus process(CharSequence operation, CharSequence data) throws AvroRemoteException {
        if (proxy != null) {
            return proxy.process(operation, data);
        } else {
            return null;
        }
    }

    @Override
    public ResponseStatus status(CharSequence operation) throws AvroRemoteException {
        if (proxy != null) {
            return proxy.status(operation);
        } else {
            return null;
        }
    }
}
