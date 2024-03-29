package info.developia.lib.graph;

public class Edge {
    private final Node source;
    private final Node destination;

    public Edge(Node source, Node destination) {
        this.source = source;
        this.destination = destination;
    }

    public Node getDestination() {
        return destination;
    }
}
