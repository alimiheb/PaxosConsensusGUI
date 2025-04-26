Paxos Consensus Implementation
Overview
This project implements the Paxos consensus algorithm, a fundamental protocol for achieving distributed consensus in a network of unreliable processors. The system consists of multiple Proposer servers (running on ports 50051, 50052, and 50053) that propose random values, and an Acceptor that collects these values, determines a consensus value (the maximum), and distributes it back to the Proposers. The implementation is built in Java, using gRPC for communication between components and Java Swing for a graphical user interface (GUI) to visualize the consensus process in real-time.
The project was initially designed to integrate a web-based interface but was modified to use Java Swing for simplicity and seamless integration within the Java/Maven ecosystem. This repository provides a complete solution, including the core Paxos implementation, a GUI visualizer, and unit tests to ensure reliability.
Features

Distributed Consensus with Paxos: Implements the Paxos algorithm with multiple Proposers and a single Acceptor.
gRPC Communication: Uses gRPC for efficient, type-safe communication between the Acceptor and Proposers.
Real-Time Visualization: A Java Swing-based GUI (PaxosVisualizer) displays proposed values and the consensus value, with interactive refresh functionality.
Enhanced GUI Features:
Refresh Counter: Tracks the number of times the "Refresh Values" button is clicked.
Highest Value Indicator: Marks the server with the highest proposed value using a green dot.
Consensus Animation: Highlights changes in the consensus value with a flashing animation.


Unit Testing: Includes JUnit tests (PaxosVisualizerTest) to verify the GUI’s functionality.
Error Handling: Displays error messages if servers are unreachable or fail to respond.

Prerequisites

Java: JDK 11 or higher.
Maven: For dependency management and building the project.
IntelliJ IDEA (recommended): For running and debugging the application.
gRPC: Dependencies are managed via Maven (see pom.xml).
Operating System: Tested on Ubuntu, but should work on any OS supporting Java.

Setup Instructions

Clone the Repository:
git clone https://github.com/yourusername/paxos-consensus.git
cd paxos-consensus


Build the Project:Use Maven to compile and package the project:
mvn clean install


Verify Dependencies:Ensure all dependencies (gRPC, JUnit, etc.) are downloaded by Maven. Check the pom.xml file for details.


Running the Application

Start the Proposer Servers:Open three separate terminal windows and run each Proposer on its respective port:
cd target
java -jar paxos-1.0-SNAPSHOT.jar 50051
java -jar paxos-1.0-SNAPSHOT.jar 50052
java -jar paxos-1.0-SNAPSHOT.jar 50053


Run the Acceptor with GUI:In IntelliJ IDEA, open the project and run the PaxosAcceptor class:

Right-click on PaxosAcceptor.java and select Run 'PaxosAcceptor.main()'.
The Acceptor will communicate with the Proposers via gRPC, calculate the consensus, and launch the GUI.


Interact with the GUI:

The GUI (PaxosVisualizer) will display the proposed values (e.g., 39, 45, 92) for each server and the consensus value (e.g., 92).
Click the "Refresh Values" button to fetch new values and update the display.
Observe the refresh counter, green dot for the highest value, and animation on consensus value changes.



Project Structure

src/main/java/rt4/paxos/:
PaxosAcceptor.java: Core logic for fetching proposed values, calculating consensus, and distributing the result.
PaxosVisualizer.java: Swing-based GUI for visualizing the consensus process.


src/test/java/rt4/paxos/:
PaxosVisualizerTest.java: JUnit tests for the GUI functionality.


target/: Contains the compiled JAR file (paxos-1.0-SNAPSHOT.jar).
pom.xml: Maven configuration with dependencies (gRPC, JUnit).

Testing
The project includes JUnit tests to ensure the GUI works as expected. To run the tests:

Execute Tests:
mvn test

Or, in IntelliJ IDEA, right-click on PaxosVisualizerTest.java and select Run 'PaxosVisualizerTest'.

Test Cases:

testInitialValuesDisplayed: Verifies that initial values are displayed correctly in the GUI.
testRefreshUpdatesValues: Simulates a refresh and checks if the values update as expected.



Screenshots
Below is a description of the GUI:

Main Window: A dark blue interface (800x500 pixels) with a title "Paxos Consensus Visualizer".
Server Cards: Three horizontal cards showing proposed values (e.g., Server 50051: 39, Server 50052: 45, Server 50053: 92). A green dot marks the server with the highest value.
Consensus Panel: Displays the consensus value (e.g., 92) in a teal panel, with a flashing animation on changes.
Refresh Section: Includes a "Refresh Values" button, a "Last Refresh" timestamp, a "Refresh Count" (e.g., Refresh Count: 3), and an error message area.

Future Improvements

Historical Data: Add a table to log past consensus values and timestamps.
Auto-Refresh: Implement a timer for periodic updates without manual clicks.
Advanced Visualizations: Integrate charts (e.g., using JFreeChart) to plot value trends over time.
Retry Mechanism: Add automatic retries for failed gRPC calls to improve reliability.

Contributing
Contributions are welcome! To contribute:

Fork the repository.
Create a new branch (git checkout -b feature/your-feature).
Make your changes and commit (git commit -m "Add your feature").
Push to your branch (git push origin feature/your-feature).
Open a pull request with a detailed description of your changes.

License
This project is licensed under the MIT License. See the LICENSE file for details.
Contact
For questions or feedback, please open an issue on GitHub or contact the project maintainer at [your-email@example.com].
