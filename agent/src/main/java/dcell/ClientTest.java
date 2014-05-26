package dcell;

import dcell.agent.thrift.AgentService;
import dcell.agent.thrift.Message;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.*;

import java.io.IOException;

public class ClientTest {

    private String agentID;

    private String host;

    private int port;

    private TNonblockingTransport asyncTransport;

    private AgentService.AsyncClient asyncClient;

    public ClientTest(String agentID, String host, int port) throws IOException {
        this.agentID = agentID;
        this.host = host;
        this.port = port;
        this.asyncTransport = new TNonblockingSocket(this.host, this.port);
        this.asyncClient = new AgentService.AsyncClient(new TCompactProtocol.Factory(), new TAsyncClientManager(), asyncTransport);
    }

    public void syncPing() throws TException {
        final TSocket socket = new TSocket(this.host, this.port);
        socket.setTimeout(1000);
        final TTransport transport = new TFramedTransport(socket);
        final TProtocol protocol = new TCompactProtocol(transport);
        final AgentService.Client client = new AgentService.Client(protocol);

        transport.open();

        Message req = new Message();
        req.setAgentID(this.agentID);
        req.setData("Hello there".getBytes());
        Message resp = client.ping(req);
        System.out.println("Received " + resp);
        transport.close();
    }

    public void asyncPing() throws TException, IOException {
        Message req = new Message();
        req.setAgentID(this.agentID);
        req.setData("Hello there".getBytes());
        asyncClient.ping(req, new AsyncMethodCallback() {
            @Override
            public void onComplete(Object response) {
                System.out.println("Received " + (Message) response);
                asyncTransport.close();
            }

            @Override
            public void onError(Exception exception) {
                System.err.println("Received " + exception);
                asyncTransport.close();
            }
        });
    }

    public void close() {
        if (this.asyncTransport.isOpen()) {
            this.asyncTransport.close();
        }
    }

    public static void main(String[] args) throws TException, IOException, InterruptedException {
        final ClientTest clientTest = new ClientTest(args[0], args[1], Integer.parseInt(args[2]));
        clientTest.syncPing();
        clientTest.asyncPing();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Good bye");
                clientTest.close();
            }
        });


        while (true) {
        }
    }
}
