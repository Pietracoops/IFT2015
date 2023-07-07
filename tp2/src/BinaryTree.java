import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BinaryTree {
    Node root;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private Node rotateLeft(Node node) {
        List<Node> nodeList = new ArrayList<>();
        Node newRoot = node.right;
        Node leftChildOfNewRoot = newRoot.left;

        // Perform rotation
        newRoot.left = node;
        node.right = leftChildOfNewRoot;

        // Update heights
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newRoot.height = Math.max(height(newRoot.left), height(newRoot.right)) + 1;

        return newRoot;
    }

    private Node rotateRight(Node node)
    {
        List<Node> nodeList = new ArrayList<>();
        Node newRoot = node.left;
        Node rightChildOfNewRoot = newRoot.right;

        // Perform rotation
        newRoot.right = node;
        node.left = rightChildOfNewRoot;

        // Update heights
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newRoot.height = Math.max(height(newRoot.left), height(newRoot.right)) + 1;

        return newRoot;
    }

    private Node addRecursiveBalanced(Node node, Node nodeToAdd) {
    if (node == null) {
        return nodeToAdd;
    }

    if (nodeToAdd.MedicationNumber < node.MedicationNumber) {
        node.left = addRecursiveBalanced(node.left, nodeToAdd);
    } else if (nodeToAdd.MedicationNumber > node.MedicationNumber) {
        node.right = addRecursiveBalanced(node.right, nodeToAdd);
    } else {
        // Value already exists in the tree
        return node;
    }

    // Update the height of the current node
    node.height = 1 + Math.max(height(node.left), height(node.right));

    // Check the balance factor of the current node
    int balanceFactor = getBalanceFactor(node);

    // Perform rotations if the tree is unbalanced
    List<Node> nodeList = new ArrayList<>();
    if (balanceFactor > 1 && nodeToAdd.MedicationNumber < node.left.MedicationNumber) {
        node = rotateRight(node);
        return node;
    }

    if (balanceFactor < -1 && nodeToAdd.MedicationNumber > node.right.MedicationNumber) {
        node = rotateLeft(node);
        return node;
    }

    if (balanceFactor > 1 && nodeToAdd.MedicationNumber > node.left.MedicationNumber) {
        node.left = rotateLeft(node.left);
        node = rotateRight(node);
        return node;
    }

    if (balanceFactor < -1 && nodeToAdd.MedicationNumber < node.right.MedicationNumber) {
        node = rotateRight(node.right);
        node.right = rotateLeft(node);
        return node;
    }

    return node;
    }


    public void addBalanced(Node node) 
    {
        root = addRecursiveBalanced(root, node);
    }
    private Node addRecursive(Node current, Node addnode) {
        if (current == null) {
            return addnode;
        }

        if (addnode.MedicationNumber < current.MedicationNumber) {
            current.left = addRecursive(current.left, addnode);
        } else if (addnode.MedicationNumber > current.MedicationNumber) {
            current.right = addRecursive(current.right, addnode);
        } else {
            // value already exists
            return current;
        }

        return current;
    }

    public void add(Node addnode) {
        root = addRecursive(root, addnode);
    }


    private Node containsNodeRecursive(Node current, int value) {
        if (current == null) {
            return null;
        }

        if (value == current.MedicationNumber) {
            return current;
        }

        Node leftResult = containsNodeRecursive(current.left, value);
        if (leftResult != null) {
            return leftResult;
        }

        Node rightResult = containsNodeRecursive(current.right, value);
        if (rightResult != null) {
            return rightResult;
        }

        return null;
    }

    public Node containsNode(int value) {
        return containsNodeRecursive(root, value);
    }

    private boolean processNodeRecursive(Node current, int value, int quantity, Date currentDate) {
        if (current == null) {
            return false;
        }

        if (value == current.MedicationNumber) {
            // Perform check for all medications
            for (int i = 0; i < current.medicationStores.size(); i++)
            {
                // Find a valid one from the list: Expire date needs to be after current date
                int dateResult = currentDate.compareTo(current.medicationStores.get(i).ExpireDate);
                if (dateResult < 0)
                {
                    // Current date before expiration date, now check quantities
                    Medication medObj = current.medicationStores.get(i);
                    if (medObj.Quantity > quantity)
                    {
                        medObj.Quantity -= quantity;
                        current.medicationStores.set(i, medObj);
                        return true;
                    } else if (medObj.Quantity == quantity)
                    {
                        current.medicationStores.remove(i);
                        if (current.medicationStores.size() == 0)
                        {
                            delete(value); // Remove from tree
                            return true;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    continue;
                }

            }
            return false;
        }

        if (processNodeRecursive(current.left, value, quantity, currentDate)) {
            return true;
        }

        return processNodeRecursive(current.right, value, quantity, currentDate);
    }

    public boolean processNode(int value, int quantity, Date currentDate) {
        return processNodeRecursive(root, value, quantity, currentDate);
    }

    private int findSmallestValue(Node root) {
        return root.left == null ? root.MedicationNumber : findSmallestValue(root.left);
    }
    private Node deleteRecursive(Node current, int value) {
        if (current == null) {
            return null;
        }

        if (value == current.MedicationNumber) {
            // Node to delete found
            if (current.left == null && current.right == null) {
                return null;
            }
            if (current.right == null) {
                return current.left;
            }
            if (current.left == null) {
                return current.right;
            }
            int smallestValue = findSmallestValue(current.right);
            current.MedicationNumber = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue);
            return current;
        }
        if (value < current.MedicationNumber) {
            current.left = deleteRecursive(current.left, value);
            return current;
        }
        current.right = deleteRecursive(current.right, value);
        return current;
    }

    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    public String traverseInOrder(Node node, String strOutput, Date currentDate) {
        if (node != null) {
            strOutput = traverseInOrder(node.left, strOutput, currentDate);
            StringBuilder strOutputBuilder = new StringBuilder(strOutput);
            for (int i = 0; i < node.medicationStores.size(); i++)
            {
                strOutputBuilder.append(node.Name).append(" ").append(node.medicationStores.get(i).Quantity).append(" ").append(format.format(node.medicationStores.get(i).ExpireDate)).append("\n");
            }
            strOutput = strOutputBuilder.toString();
            //System.out.print(" " + node.MedicationNumber);
            strOutput = traverseInOrder(node.right, strOutput, currentDate);
        }
        return strOutput;
    }

    public void traversePreOrder(Node node) {
        if (node != null) {
            System.out.print(" " + node.MedicationNumber);
            traversePreOrder(node.left);
            traversePreOrder(node.right);
        }
    }

    public void traversePostOrder(Node node) {
        if (node != null) {
            traversePostOrder(node.left);
            traversePostOrder(node.right);
            System.out.print(" " + node.MedicationNumber);
        }
    }



}
