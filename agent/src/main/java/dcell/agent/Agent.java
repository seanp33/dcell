package dcell.agent;

import dcell.agent.exception.AgentException;

public interface Agent {

    void init() throws AgentException;

    void start() throws AgentException;

    Object status();

    void stop() throws AgentException;
}
