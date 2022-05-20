public class Node <Key extends Comparable<Key>> {

    public int m;                   // Number of keys currently
    public Key[] keys;              // For both Internal and Leaf Nodes
    public RecordPair[] records;    // For Leaf Nodes
    public Node[] children;         // For Internal Nodes
    public boolean isLeafNode;      // For both Internal and Leaf Nodes
    public boolean isOverflowBlock;
    public Node siblingPointer;     // For Leaf Nodes

    public Node(boolean isLeafNode, int order, boolean isOverflowBlock) {
        this.isLeafNode = isLeafNode;
        this.isOverflowBlock = isOverflowBlock;
        keys = (Key[]) new Comparable[order];
        children = new Node[order + 1];
        if (isLeafNode) {
            records = new RecordPair[order];
        }

    }

    public void setLeafNode(boolean isLeafNode, int order) {
        if (!isLeafNode && this.isLeafNode) {
            records = null;
            children = new Node[order + 1];
        }
    }

    public void setKey(int index, Comparable key) {
        keys[index] = (Key) key;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node node = (Node) o;
        for (int i = 0; i < node.keys.length; i++) {
            if (this.keys[i] != node.keys[i]) {
                return false;
            }
        }
        return true;
    }

}
