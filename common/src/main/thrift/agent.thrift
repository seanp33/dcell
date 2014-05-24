namespace java dcell.agent.thrift
namespace cpp dcell.agent.thrift

include "core.thrift"


typedef i64 AgentID

// a message request
struct Message{
    1: AgentID agentID,
    2: byte data,
    3: OPERATION operation,
    4: optional i32 id,
    5: optional i32 ts,
    6: optional VClock vclock
}

// ping service
service TestService {
   Message ping(1:Message message),
}

enum OPERATION{
    PING = 1,
    GET = 2,
    SET = 3,
    DEL = 4
}
