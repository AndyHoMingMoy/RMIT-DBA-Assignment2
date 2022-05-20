@SuppressWarnings("rawtypes")
public class BTree {

    public Node root;
    private final int order;

    public BTree(int order) {
        this.order = order;
    }

    public void search(Comparable key, Object value) {
        Node cursor = root;
        boolean isLarger = true;
        while (!cursor.isLeafNode) {
            for (int i = 0; i < cursor.m; i++) {
                if (key.compareTo(cursor.keys[i]) < 0) {
                    cursor = cursor.children[i];
                    isLarger = false;
                    break;
                }
            }
            if (isLarger) {
                cursor = cursor.children[cursor.m];
            }
        }
        for (int i = 0; i < cursor.m; i++) {
            if (key.equals(cursor.keys[i])) {
                System.out.println("Element found");
                return;
            }
        }
        System.out.println("Element not found");

    }

    public void insert(Comparable key, Object value) {
        if (root == null) {
            root = new Node(true, order, false);
            root.setKey(0, key);
            root.records[0] = new RecordPair(key, value);
            root.m++;
        } else {
            // Traverse to the node
            Node cursor = root;
            Node parent = null;
            int height = -1;
            boolean isLarger = true;
            while (!cursor.isLeafNode) {
                parent = cursor;
                for (int i = 0; i < cursor.m; i++) {
                    if (key.compareTo(cursor.keys[i]) < 0 || key.compareTo(cursor.keys[i]) == 0) {
                        cursor = cursor.children[i];
                        isLarger = false;
                        break;
                    }
                }
                if (isLarger) {
                    cursor = cursor.children[cursor.m];
                }
                height++;
            }

            // If bucket is full
            if (cursor.m == order) {
                boolean isExist = false;
                System.out.println(cursor.keys[0]);
                for (int i = 0; i < cursor.m; i++) {
                    System.out.println("A");
                    if (cursor.keys[i].equals(key)) {
                        System.out.println("B");
                        isExist = true;
                        break;
                    }
                }
                if (isExist == false) {
                    // Copy old cursor to new overflow cursor (m + 1)
                    Node tempCursor = new Node(true, order + 1, false);
                    int i;
                    for (i = 0; i < cursor.m; i++) {
                        tempCursor.keys[i] = cursor.keys[i];
                        tempCursor.children[i] = cursor.children[i];
                        if (tempCursor.isLeafNode) {
                            tempCursor.records[i] = cursor.records[i];
                        }
                        tempCursor.m++;
                    }
                    tempCursor.keys[i] = key;
                    tempCursor.records[i] = new RecordPair(key, value);
                    tempCursor.children[i] = cursor.children[i];
                    tempCursor.m++;
                    sortKeys(tempCursor.keys, tempCursor.m);
                    sortChildren(tempCursor.children, tempCursor.children.length);
                    sortPairs(tempCursor.records, tempCursor.records.length);

                    // Get index of cursor from parent children list if node is not root
                    int index;
                    if (cursor.isLeafNode && !(cursor == root)) {
                        for (index = 0; index < parent.m; index++) {
                            if (cursor.equals(parent.children[index])) {
                                break;
                            }
                        }
                        parent.children[index] = tempCursor;
                    }
                    split(tempCursor, height, parent);
                } else {
                    Node overflowBlock;
                    while (cursor.m == order) {
                        System.out.println("A");
                        cursor = cursor.siblingPointer;
                    }
                    if (!cursor.siblingPointer.isOverflowBlock) {
                        overflowBlock = new Node(true, order, true);
                        overflowBlock.keys[0] = key;
                        overflowBlock.records[0] = new RecordPair(key, value);
                        overflowBlock.m++;
                        overflowBlock.siblingPointer = cursor.siblingPointer;
                        cursor.siblingPointer = overflowBlock;
                    } else {
                        overflowBlock = cursor.siblingPointer;
                        overflowBlock.keys[overflowBlock.m] = key;
                        overflowBlock.records[overflowBlock.m] = new RecordPair(key, value);
                        overflowBlock.m++;
                    }
                }

            } else {
                cursor.setKey(cursor.m, key);
                if (cursor.isLeafNode) {
                    cursor.records[cursor.m] = new RecordPair(key, value);
                }
                cursor.m++;
            }

            // Sort all keys and children of modified node and parent
            sortKeys(cursor.keys, cursor.m);
            sortPairs(cursor.records, cursor.records.length);
            if (parent != null) {
                sortKeys(parent.keys, parent.m);
                sortChildren(parent.children, parent.children.length);
            }
        }

    }

    private void split(Node overflowNode, int height, Node parent) {

        // Create two child nodes to split the parent node
        Node childLeft = new Node(true, order,false);
        Node childRight = new Node(true, order, false);

        // Set left child sibling to right child
        childLeft.siblingPointer = childRight;

        // Allocate two halves of the original bucket to two new nodes
        for (int i = 0; i < overflowNode.keys.length/2; i++) {
            childLeft.keys[i] = overflowNode.keys[i];
            if (overflowNode.isLeafNode) {
                childLeft.records[i] = overflowNode.records[i];
            }
            childLeft.m++;
        }

        for (int i = 0; i < overflowNode.keys.length / 2 + 1; i++) {
            childRight.keys[i] = overflowNode.keys[overflowNode.keys.length/2 + i];
            if (overflowNode.isLeafNode) {
                childRight.records[i] = overflowNode.records[overflowNode.records.length/2 + i];
            }
            childRight.m++;
        }

        // Allocate children to two new nodes
        if (!overflowNode.isLeafNode) {
            int childLeftSize = 0;
            int childRightSize = 0;
            for (int i = 0; i < overflowNode.children.length; i++) {
                if (overflowNode.children[i].keys[0].compareTo(overflowNode.keys[overflowNode.m/2]) < 0) {
                    childLeft.children[childLeftSize] = overflowNode.children[i];
                    childLeftSize++;
                } else {
                    childRight.children[childRightSize] = overflowNode.children[i];
                    childRightSize++;
                }
            }
        }

        // Set children to internal node if children exist for either node
        if (childLeft.children[0] != null || childRight.children[0] != null) {
            childLeft.isLeafNode = false;
            childRight.isLeafNode = false;
        }

        // If root is split
        if (height == -1) {
            Node newRoot = new Node(false, order, false);
            // Set the key of the new root to the first value of the right node
            newRoot.keys[0] = childRight.keys[0];
            newRoot.m++;
            root = newRoot;
            root.children[0] = childLeft;
            root.children[1] = childRight;
            // If the right child is not a leaf node, remove the raised key from the node
            if (!childRight.isLeafNode) {
                System.arraycopy(childRight.keys, 1, childRight.keys, 0, childRight.keys.length - 1);
            }
        } else {
            if (parent.m + 1 > order) {
                Node tempParent = new Node(false, order + 1, false);
                int i;
                for (i = 0; i < parent.m; i++) {
                    tempParent.keys[i] = parent.keys[i];
                    tempParent.children[i] = parent.children[i];
                    tempParent.m++;
                }
                tempParent.keys[i] = childRight.keys[0];
                tempParent.children[i] = parent.children[i];
                tempParent.m++;
                sortKeys(tempParent.keys, tempParent.m);
                sortChildren(tempParent.children, tempParent.children.length);

                parent = tempParent;
            } else {
                parent.keys[parent.m] = childRight.keys[0];
                parent.m++;

                AllocateChildrenToParent(parent, childLeft, childRight, overflowNode, height);
                return;
            }

            // If parent's children contains childLess or childMore
            AllocateChildrenToParent(parent, childLeft, childRight, overflowNode, height);

            // Get parent of the parent node
            Node cursor = root;
            Node tempNodeParent = null;
            boolean isLarger = true;
            while (!cursor.isLeafNode) {
                tempNodeParent = cursor;
                for (int j = 0; j < cursor.m; j++) {
                    if (childRight.keys[0].compareTo(cursor.keys[j]) < 0) {
                        cursor = cursor.children[j];
                        isLarger = false;
                        break;
                    }
                }
                if (isLarger) {
                    cursor = cursor.children[cursor.m];
                }
            }
            split(parent, height - 1, tempNodeParent);
        }

    }

    private void AllocateChildrenToParent(Node parent, Node childLeft, Node childRight, Node overflowNode, int height) {
        for (int i = 0; i < parent.m; i++) {
            if (parent.children[i].keys[0] == childLeft.keys[0]) {
                parent.children[i] = childLeft;
                parent.children[parent.m] = childRight;
                break;
            }
            if (parent.children[i].keys[0] == childRight.keys[0]) {
                parent.children[i] = childRight;
                parent.children[parent.m] = childLeft;
                break;
            }
        }

        sortChildren(parent.children, parent.children.length);

        if (overflowNode.isLeafNode && height != -1) {
            // Update right node pointer to the next leaf node
            int index;
            for (index = 0; index < parent.children.length; index++) {
                if (childRight.equals(parent.children[index])) {
                    break;
                }
            }
            if (index + 1 < parent.children.length && parent.children[index + 1] != null) {
                childRight.siblingPointer = parent.children[index + 1];
            }

            // Update previous leaf node pointer to left child
            for (index = 0; index < parent.children.length; index++) {
                if (childLeft.equals(parent.children[index])) {
                    break;
                }
            }
            if (index - 1 >= 0) {
                parent.children[index - 1].siblingPointer = childLeft;
            }
        }
    }

    // Insertion sort for children
    private void sortChildren (Node[] children, int length) {
        int size = 0;
        for (int i = 0; i < length; i++) {
            if (children[i] != null) {
                size++;
            }
        }
        for (int i = 1; i < size; i++) {
            Node current = children[i];
            int j = i - 1;
            while (j > -1) {
                assert children[j] != null;
                if (!(children[j].keys[0].compareTo(current.keys[0]) > 0)) break;
                children[j + 1] = children[j];
                j--;
            }
            children[j + 1] = current;
        }
    }

    // Insertion sort for keys
    private void sortKeys (Comparable[] keys, int length) {
        for (int i = 1; i < length; i++) {
            Comparable current = keys[i];
            int j = i - 1;
            while (j > -1 && keys[j].compareTo(current) > 0) {
                keys[j + 1] = keys[j];
                j--;
            }
            keys[j + 1] = current;
        }
    }

    // Insertion sort for recordPairs
    private void sortPairs (RecordPair[] pairs, int length) {
        int size = 0;
        for (int i = 0; i < length; i++) {
            if (pairs[i] != null) {
                size++;
            }
        }
        for (int i = 1; i < size; i++) {
            RecordPair current = pairs[i];
            int j = i - 1;
            while (j > -1 && pairs[j].key.compareTo(current.key) > 0) {
                pairs[j + 1] = pairs[j];
                j--;
            }
            pairs[j + 1] = current;
        }
    }

    @SuppressWarnings("unused")
    public void recursivePrintTree(Node tempDown, int height) {
        if (tempDown == root) {
            System.out.println("=================================================");
            System.out.println("Root [Height 0]");
            System.out.println("=================================================");
            for (int i = 0; i < tempDown.keys.length; i++) {
                System.out.print(tempDown.keys[i] + " ");
            }
            System.out.println("\n" + "Number of children: " + tempDown.m);
            System.out.println("IsLeafNode: " + tempDown.isLeafNode);
            System.out.println("=================================================");
        } else {
            System.out.println("=================================================");
            System.out.println("Children [Height " + height + "]");
            System.out.println("=================================================");
            Node heightStartNode = tempDown;
            while (heightStartNode != null) {
                for (int i = 0; i < heightStartNode.keys.length; i++) {
                    System.out.print(heightStartNode.keys[i] + " ");
                }
                System.out.println("\n" + "Number of children: " + heightStartNode.m);
                System.out.println("IsLeafNode: " + heightStartNode.isLeafNode);
                if (heightStartNode.isLeafNode) {
                    System.out.println("Values: ");
                    for (int i = 0; i < heightStartNode.records.length; i++) {
                        System.out.println("\t" + heightStartNode.records[i]);
                    }
                }
                if (heightStartNode.siblingPointer != null) {
                    System.out.print("Pointer to: ");
                    for (int i = 0; i < heightStartNode.siblingPointer.keys.length; i++) {
                        System.out.print(heightStartNode.siblingPointer.keys[i] + " ");
                    }
                    System.out.println();
                } else {
                    System.out.println("Pointer to: N/A");
                }
                System.out.println("-------------------------------------------------");
                heightStartNode = heightStartNode.siblingPointer;
            }
        }
        if (tempDown != null && tempDown.children[0] != null) {
            tempDown = tempDown.children[0];
            height++;
            recursivePrintTree(tempDown, height);
        }
    }
}


