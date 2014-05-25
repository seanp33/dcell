namespace java dcell.agent.thrift
namespace cpp dcell.agent.thrift

include "core.thrift"

typedef string AgentID

typedef map<byte,i32> VClock

enum OPERATION{
    PING = 1, // just a ping
    GET = 2, // gimmie
    SET = 3, // set it
    DEL = 4, // del it
    SYS = 5 // some system op
}

struct Message{
    1: AgentID agentID,
    2: binary data,
    3: OPERATION operation,
    4: optional i32 id,
    5: optional i32 ts,
    6: optional VClock vclock
}

service AgentService {
   Message ping(1:Message message),
   oneway void beat()
}