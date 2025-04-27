# Paxos Consensus Implementation

## Overview
This project implements the **Paxos consensus algorithm** using Java, gRPC, and Java Swing. Multiple Proposers propose random values, and an Acceptor determines and distributes the consensus value (maximum). Originally designed for a web interface, it was adapted to Swing for simplicity.

## Features
- Paxos consensus protocol
- gRPC-based server communication
- Real-time GUI visualization (Java Swing)

## Prerequisites
- Java JDK 22+ (and java language 1.7)
- Maven
- IntelliJ IDEA (recommended)
- gRPC dependencies (managed via Maven)

## Setup
```bash
git clone https://github.com/alimiheb/PaxosConsensusGUI.git
cd paxos-consensus
mvn clean install
```

## Running
1. **Start Proposers** (in three terminals):
```bash
cd target
java -jar paxos-1.0-SNAPSHOT.jar 50051
java -jar paxos-1.0-SNAPSHOT.jar 50052
java -jar paxos-1.0-SNAPSHOT.jar 50053
```

2. **Run Acceptor GUI** in IntelliJ:
- Right-click `PaxosAcceptor.java` > Run 'PaxosAcceptor.main()'

## Project Structure
```
src/main/java/rt4/paxos/
  - PaxosAcceptor.java
  - PaxosProposer.java
  - PaxosServiceImpl.java
  - PaxosVisualizer.java
src/main/proto/
  - paxos.proto
src/main/resources/
target/
pom.xml
```



## Contact
For issues or feedback, open a GitHub issue or contact [iheb66alimi@gmail.com].

