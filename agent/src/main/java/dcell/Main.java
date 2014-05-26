package dcell;

import dcell.agent.DefaultAgent;
import dcell.agent.conf.AgentConfiguration;
import dcell.agent.exception.AgentException;

public class Main {

    public Main(AgentConfiguration conf) throws AgentException {
        DefaultAgent agent = new DefaultAgent();
        agent.init(conf);
        agent.start();
        agent.stop();
    }

    public static void main(String[] args) throws AgentException {
        AgentConfiguration conf = new AgentConfiguration();
        conf.setPort(Integer.parseInt(args[1]));
        conf.setAgentID(args[0]);
        new Main(conf);
    }
}
