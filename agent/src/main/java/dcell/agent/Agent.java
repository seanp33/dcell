package dcell.agent;

import dcell.agent.conf.AgentConfiguration;
import dcell.agent.exception.AgentException;

public interface Agent {

    void init(AgentConfiguration config) throws AgentException;

    void start() throws AgentException;

    Object status();

    void stop() throws AgentException;
}
