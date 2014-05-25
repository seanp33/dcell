package dcell.agent;

import dcell.agent.exception.AgentException;
import dcell.agent.thrift.AgentService;
import dcell.agent.thrift.Message;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAgent implements Agent {

    protected final static Logger LOGGER = LoggerFactory.getLogger(DefaultAgent.class);

    public static final int PORT = 10000;

    private TServer server;

    @Override
    public void init() throws AgentException {

        try {
            AgentService.AsyncProcessor processor = new AgentService.AsyncProcessor(new Handler());

            TNonblockingServerSocket socket = new TNonblockingServerSocket(PORT);

            TNonblockingServer.Args args = new TNonblockingServer.Args(socket);
            args.processor(processor);
            args.transportFactory(new TFramedTransport.Factory());
            args.protocolFactory(new TCompactProtocol.Factory());

            server = new TNonblockingServer(args);

        } catch (TTransportException e) {
            LOGGER.error(e.getMessage());
            throw new AgentException("Could not initialize Agent TServerTransport", e);
        }
    }

    @Override
    public void start() throws AgentException {
        if (server == null) {
            throw new AgentException("Agent server not initialized. Did you call init?");
        }

        if (!server.isServing()) {
            LOGGER.info("Agent is now serving on {}", PORT);
            server.serve();
        }
    }

    @Override
    public Object status() {
        // TODO: implement an AgentStatus
        return "status";
    }

    @Override
    public void stop() {
        if (server == null) {
            LOGGER.info("Agent server was never initialized. Nothing to do");
        }

        if (server.isServing()) {
            server.stop();
        }

    }

    private static class Handler implements AgentService.AsyncIface {

        @Override
        public void ping(Message message, AsyncMethodCallback resultHandler) throws TException {
            LOGGER.debug("Ping received");
            String data = "PING::REPLY::" + System.currentTimeMillis();
            Message reply = new Message(message);
            reply.setData(data.getBytes());
            resultHandler.onComplete(reply);
        }

        @Override
        public void beat(AsyncMethodCallback resultHandler) throws TException {
            LOGGER.debug("Beat received");
            resultHandler.onComplete("Beating");
        }
    }
}