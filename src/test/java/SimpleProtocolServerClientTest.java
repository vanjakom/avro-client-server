import com.mungolab.protocol.ResponseStatus;
import com.mungolab.protocol.SimpleProtocol;
import com.mungolab.rpc.client.SimpleProtocolClient;
import com.mungolab.rpc.server.SimpleProtocolServer;
import org.apache.avro.AvroRemoteException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Vanja Komadinovic ( vanjakom@gmail.com )
 */
public class SimpleProtocolServerClientTest {
    private SimpleProtocolServer server;

    @BeforeTest
    public void setupServer() {
        server = new SimpleProtocolServer();
        server.startServer(6666, new SimpleProtocol() {
            @Override
            public ResponseStatus process(CharSequence operation, CharSequence data) throws AvroRemoteException {
                if ("add".equals(operation)) {
                    return ResponseStatus.OK;
                } else {
                    return ResponseStatus.FAIL;
                }

            }

            @Override
            public ResponseStatus status(CharSequence operation) throws AvroRemoteException {
                if ("add".equals(operation)) {
                    return ResponseStatus.OK;
                } else {
                    return ResponseStatus.FAIL;
                }
            }
        });
    }

    @Test
    public void testClient() {
        SimpleProtocolClient client = new SimpleProtocolClient();
        client = client.connect("localhost", 6666);
        if (client != null) {
            try {
                Assert.assertEquals(client.process("add", "data"), ResponseStatus.OK);
                Assert.assertEquals(client.status("add"), ResponseStatus.OK);

                Assert.assertEquals(client.process("delete", "data"), ResponseStatus.FAIL);
                Assert.assertEquals(client.status("delete"), ResponseStatus.FAIL);
            } catch (Exception e) {
                Assert.fail("Unable to get response from server", e);
            }

            client.close();
        } else {
            Assert.fail("Unable to setup client");
        }
    }

    @AfterTest
    public void closeServer() {
        server.stopServer();
    }
}
