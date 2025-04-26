package rt4.paxos;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.swing.*;
import java.util.*;

public class PaxosAcceptor {
    private final List<String> targetPorts = List.of("50051", "50052", "50053");
    private Map<String, Integer> proposedValues = new HashMap<>();
    private Integer consensusValue;

    public void runConsensus() {
        // Clear previous values
        proposedValues.clear();
        consensusValue = null;

        // Étape 1 : Demande à chaque serveur sa valeur aléatoire
        for (String port : targetPorts) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", Integer.parseInt(port))
                    .usePlaintext()
                    .build();

            PaxosServiceGrpc.PaxosServiceBlockingStub stub = PaxosServiceGrpc.newBlockingStub(channel);

            try {
                ValueResponse response = stub.proposeValue(ValueRequest.newBuilder().setRequester("client").build());
                proposedValues.put(port, response.getProposedValue());
            } catch (Exception e) {
                System.err.println("Error contacting server on port " + port + ": " + e.getMessage());
            } finally {
                channel.shutdown();
            }
        }

        // Étape 2 : Choix de la valeur maximale (R) comme valeur de consensus
        if (!proposedValues.isEmpty()) {
            consensusValue = Collections.max(proposedValues.values());
            System.out.println("Valeurs proposées : " + proposedValues);
            System.out.println("=> Valeur choisie par consensus : " + consensusValue);

            // Étape 3 : Envoi de cette valeur R à tous les serveurs
            for (String port : targetPorts) {
                ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", Integer.parseInt(port))
                        .usePlaintext()
                        .build();

                PaxosServiceGrpc.PaxosServiceBlockingStub stub = PaxosServiceGrpc.newBlockingStub(channel);
                try {
                    stub.storeConsensusValue(ConsensusValue.newBuilder().setValue(consensusValue).build());
                } catch (Exception e) {
                    System.err.println("Error sending consensus to server on port " + port + ": " + e.getMessage());
                } finally {
                    channel.shutdown();
                }
            }
            System.out.println("=> La valeur de consensus a été envoyée à tous les serveurs.");
        } else {
            System.err.println("No values received from servers.");
        }
    }

    public Map<String, Integer> getProposedValues() {
        return new HashMap<>(proposedValues);
    }

    public Integer getConsensusValue() {
        return consensusValue;
    }

    public List<String> getTargetPorts() {
        return targetPorts;
    }

    public static void main(String[] args) {
        PaxosAcceptor acceptor = new PaxosAcceptor();
        acceptor.runConsensus();

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            PaxosVisualizer visualizer = new PaxosVisualizer(acceptor);
            visualizer.setVisible(true);
        });
    }
}