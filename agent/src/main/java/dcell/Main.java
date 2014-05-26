package dcell;

import dcell.agent.Agent;
import dcell.agent.DefaultAgent;
import dcell.agent.conf.AgentConfiguration;
import dcell.agent.exception.AgentException;

public class Main {

    Agent agent = new DefaultAgent();

    public Main(AgentConfiguration conf) throws AgentException {
        DefaultAgent agent = new DefaultAgent();
        agent.init(conf);
        agent.start();

    }

    public void close() throws AgentException {
        agent.stop();
    }

    public static void main(String[] args) throws AgentException {
        AgentConfiguration conf = new AgentConfiguration();
        conf.setPort(Integer.parseInt(args[1]));
        conf.setAgentID(args[0]);
        final Main server = new Main(conf);

        // TODO: implement the thrift way of doing this
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    server.close();
                    System.out.println("Good bye");
                } catch (AgentException e) {
                    System.err.println("Could not gracefully stop agent. Good bye");
                    e.printStackTrace();
                }
            }
        });
    }
}
