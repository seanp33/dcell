include Makefile.config

all:
	$(THRIFT) --gen java core.thrift
	$(THRIFT) --gen cpp core.thrift
	$(THRIFT) --gen java agent.thrift
	$(THRIFT) --gen cpp agent.thrift

clean:
	rm -rf gen-cpp
	rm -rf gen-java
