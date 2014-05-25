package dcell.agent;

import dcell.agent.conf.AgentConfiguration;
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

    private TServer server;

    private AgentConfiguration config;

    @Override
    public void init(AgentConfiguration config) throws AgentException {
        this.config = config;

        try {
            AgentService.AsyncProcessor processor = new AgentService.AsyncProcessor(new Handler(config.getAgentID()));

            TNonblockingServerSocket socket = new TNonblockingServerSocket(config.getPort());

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
            LOGGER.info("Agent is now serving on {}", config.getPort());
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

        private String agentID;

        private Handler(String agentID) {
            this.agentID = agentID;
        }

        @Override
        public void ping(Message message, AsyncMethodCallback resultHandler) throws TException {
            LOGGER.debug("Ping received by Agent " + agentID);
            String data = "PING::REPLY::" + System.currentTimeMillis();
            Message reply = new Message(message);
            reply.setAgentID(agentID);
            reply.setData(data.getBytes());
            resultHandler.onComplete(reply);
        }

        @Override
        public void beat(AsyncMethodCallback resultHandler) throws TException {
            LOGGER.debug("Beat received by Agent " + agentID);
            resultHandler.onComplete("Beating");
        }
    }
}