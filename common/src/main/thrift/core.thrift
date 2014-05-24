namespace java dcell.core.thrift
namespace cpp dcell.core.thrift

enum EXTYPE {
    GENERAL_FAULT = 1,
    TIMEOUT = 2,
    SECURITY = 3,
    STALENESS = 4,
    BUSY = 5,
    UNKNOWN = 6
}

exception RemoteException{
    1: EXTYPE type,
    2: optional string msg
}