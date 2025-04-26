package rt4.paxos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PaxosVisualizer extends JFrame {
    private final PaxosAcceptor acceptor;
    private final JLabel[] valueLabels;
    private final JLabel[] statusDots; // For green dot on highest value
    private final JLabel consensusLabel;
    private final JLabel errorLabel;
    private final JLabel statusLabel;
    private final JLabel refreshCountLabel; // For refresh counter
    private int refreshCount = 0; // Counter for refreshes
    private int previousConsensusValue = -1; // Track previous consensus for animation

    public PaxosVisualizer(PaxosAcceptor acceptor) {
        this.acceptor = acceptor;
        setTitle("Paxos Consensus Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Main panel with dark background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(30, 45, 65));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Paxos Consensus Visualizer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for server values and consensus
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        // Server values panel (horizontal layout)
        JPanel serversPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        serversPanel.setOpaque(false);
        valueLabels = new JLabel[3];
        statusDots = new JLabel[3]; // Initialize status dots
        for (int i = 0; i < 3; i++) {
            JPanel serverCard = new JPanel(new BorderLayout(5, 5));
            serverCard.setBackground(new Color(50, 70, 90));
            serverCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(80, 100, 120), 1, true),
                    new EmptyBorder(10, 10, 10, 10)
            ));

            // Server label and status dot panel
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);

            JLabel serverLabel = new JLabel("Server " + acceptor.getTargetPorts().get(i), JLabel.CENTER);
            serverLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            serverLabel.setForeground(new Color(180, 200, 220));
            topPanel.add(serverLabel, BorderLayout.CENTER);

            statusDots[i] = new JLabel("●");
            statusDots[i].setForeground(Color.GRAY);
            statusDots[i].setFont(new Font("Arial", Font.PLAIN, 12));
            topPanel.add(statusDots[i], BorderLayout.EAST);

            serverCard.add(topPanel, BorderLayout.NORTH);

            valueLabels[i] = new JLabel("N/A", JLabel.CENTER);
            valueLabels[i].setFont(new Font("Arial", Font.BOLD, 20));
            valueLabels[i].setForeground(Color.WHITE);
            serverCard.add(valueLabels[i], BorderLayout.CENTER);

            serversPanel.add(serverCard);
        }
        centerPanel.add(serversPanel, BorderLayout.CENTER);

        // Consensus value panel
        JPanel consensusPanel = new JPanel(new BorderLayout());
        consensusPanel.setOpaque(false);
        consensusPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel consensusTitle = new JLabel("Consensus Value", JLabel.CENTER);
        consensusTitle.setFont(new Font("Arial", Font.BOLD, 16));
        consensusTitle.setForeground(new Color(180, 200, 220));
        consensusPanel.add(consensusTitle, BorderLayout.NORTH);

        consensusLabel = new JLabel("N/A", JLabel.CENTER);
        consensusLabel.setFont(new Font("Arial", Font.BOLD, 28));
        consensusLabel.setForeground(Color.WHITE);
        consensusLabel.setBackground(new Color(40, 150, 130));
        consensusLabel.setOpaque(true);
        consensusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 200, 180), 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        consensusPanel.add(consensusLabel, BorderLayout.CENTER);

        centerPanel.add(consensusPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // South panel for refresh button, status, refresh count, and error
        JPanel southPanel = new JPanel(new BorderLayout(10, 5));
        southPanel.setOpaque(false);

        // Status label (last refresh time)
        statusLabel = new JLabel("Last Refresh: Never", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(150, 170, 190));
        southPanel.add(statusLabel, BorderLayout.NORTH);

        // Refresh count label
        refreshCountLabel = new JLabel("Refresh Count: 0", JLabel.CENTER);
        refreshCountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshCountLabel.setForeground(new Color(150, 170, 190));
        southPanel.add(refreshCountLabel, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh Values") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 150, 130), w, h, new Color(30, 120, 100));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.addActionListener(e -> updateValues());
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(new Color(50, 170, 150));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(new Color(40, 150, 130));
            }
        });
        southPanel.add(refreshButton, BorderLayout.SOUTH);

        // Error label
        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(255, 100, 100));
        southPanel.add(errorLabel, BorderLayout.WEST);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        // Initial update
        updateValues();
        previousConsensusValue = parseConsensusValue(consensusLabel.getText());

        setContentPane(mainPanel);
    }

    private void updateValues() {
        // Increment refresh counter
        refreshCount++;
        refreshCountLabel.setText("Refresh Count: " + refreshCount);

        errorLabel.setText("");
        acceptor.runConsensus();
        Map<String, Integer> values = acceptor.getProposedValues();
        if (values.isEmpty()) {
            errorLabel.setText("No values received from servers.");
            for (JLabel valueLabel : valueLabels) {
                valueLabel.setText("N/A");
            }
            for (JLabel dot : statusDots) {
                dot.setForeground(Color.GRAY);
            }
            consensusLabel.setText("N/A");
        } else {
            // Update server values
            for (int i = 0; i < 3; i++) {
                String port = acceptor.getTargetPorts().get(i);
                valueLabels[i].setText(values.getOrDefault(port, 0).toString());
                statusDots[i].setForeground(Color.GRAY); // Reset dots
            }

            // Find the server with the highest value and mark with green dot
            int maxValue = Integer.MIN_VALUE;
            int maxIndex = -1;
            for (int i = 0; i < 3; i++) {
                String port = acceptor.getTargetPorts().get(i);
                int value = values.getOrDefault(port, Integer.MIN_VALUE);
                if (value > maxValue) {
                    maxValue = value;
                    maxIndex = i;
                }
            }
            if (maxIndex >= 0) {
                statusDots[maxIndex].setForeground(Color.GREEN);
            }

            // Update consensus value and animate if changed
            Integer consensus = acceptor.getConsensusValue();
            String consensusText = consensus != null ? consensus.toString() : "N/A";
            consensusLabel.setText(consensusText);

            // Animate consensus value if it has changed
            int currentConsensusValue = parseConsensusValue(consensusText);
            if (currentConsensusValue != previousConsensusValue && currentConsensusValue != -1) {
                animateConsensusValue();
            }
            previousConsensusValue = currentConsensusValue;
        }

        // Update status with current time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        statusLabel.setText("Last Refresh: " + sdf.format(new Date()));
    }

    private int parseConsensusValue(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1; // For "N/A" or invalid values
        }
    }

    private void animateConsensusValue() {
        final Color baseColor = new Color(40, 150, 130);
        final Color highlightColor = new Color(60, 180, 160);
        final int flashCount = 2;
        final int flashDuration = 500; // 500ms per flash (total 1s per cycle)

        Timer timer = new Timer(flashDuration / 2, null);
        timer.addActionListener(e -> {
            int count = timer.getActionListeners().length;
            if (count % 2 == 0) {
                consensusLabel.setBackground(highlightColor);
            } else {
                consensusLabel.setBackground(baseColor);
            }
            if (count >= flashCount * 2) {
                timer.stop();
                consensusLabel.setBackground(baseColor);
            }
        });
        timer.setRepeats(true);
        timer.start();
    }
}