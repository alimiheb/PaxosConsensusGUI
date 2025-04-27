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

## Troubleshooting

### Issue: "Cannot Resolve Symbol" Errors for gRPC Classes
After cloning the repository, you may encounter errors in IntelliJ IDEA such as `Cannot resolve symbol 'PaxosServiceGrpc'`, `Cannot resolve symbol 'ValueRequest'`, etc. This occurs because the gRPC-generated classes are not automatically recognized by IntelliJ IDEA.

#### Solution
1. **Re-import the Maven Project**:
   - In IntelliJ IDEA, open the Maven tool window (right sidebar).
   - Click the **Reimport All Maven Projects** button (circular arrow icon).
   - Alternatively, run the following command in the terminal:
     ```bash
     mvn idea:idea



## Contact
For issues or feedback, open a GitHub issue or contact [iheb66alimi@gmail.com].

