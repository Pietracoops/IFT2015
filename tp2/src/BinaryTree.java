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

    private Node addRecursiveBalanced(Node node, Node nodeToAdd) 
    {
        if (node == null) 
        {
            return nodeToAdd;
        }

        if (nodeToAdd.ExpireDate.before(node.ExpireDate)) {
            node.left = addRecursiveBalanced(node.left, nodeToAdd);
        } else if (nodeToAdd.ExpireDate.after(node.ExpireDate)) {
            node.right = addRecursiveBalanced(node.right, nodeToAdd);
        } else {
            // Value already exists in the tree, add the medication to the Node
            node.medicationStores.add(nodeToAdd.medicationStores.get(0));
            return node;
        }

        // Update the height of the current node
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Check the balance factor of the current node
        int balanceFactor = getBalanceFactor(node);

        // Perform rotations if the tree is unbalanced
        if (balanceFactor > 1 && nodeToAdd.ExpireDate.before(node.left.ExpireDate)) {
            node = rotateRight(node);
            return node;
        }

        if (balanceFactor < -1 && nodeToAdd.ExpireDate.after(node.right.ExpireDate)) {
            node = rotateLeft(node);
            return node;
        }

        if (balanceFactor > 1 && nodeToAdd.ExpireDate.after(node.left.ExpireDate)) {
            node.left = rotateLeft(node.left);
            node = rotateRight(node);
            return node;
        }

        if (balanceFactor < -1 && nodeToAdd.ExpireDate.before(node.right.ExpireDate)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            return node;
        }

        return node;
    }

    public void addBalanced(Node node) 
    {
        root = addRecursiveBalanced(root, node);
    }

    private Node containsNodeNameRecursive(Node current, String name) {
        if (current == null) {
            return null;
        }

        for (int i = 0; i < current.medicationStores.size(); i++)
        {
            Medication medObj = current.medicationStores.get(i);
            if (medObj.Name.equals(name))
            {
                return current;
            }

        }

        Node leftResult = containsNodeNameRecursive(current.left, name);
        if (leftResult != null) 
        {
            return leftResult;
        }

        Node rightResult = containsNodeNameRecursive(current.right, name);
        if (rightResult != null) 
        {
            return rightResult;
        }

        return null;
    }

    public Node containsNodeName(String name) {
        return containsNodeNameRecursive(root, name);
    }


    private Node containsNodeRecursive(Node current, Date expiredate) {
        if (current == null) {
            return null;
        }

        if (expiredate.equals(current.ExpireDate)) {
            return current;
        }

        Node leftResult = containsNodeRecursive(current.left, expiredate);
        if (leftResult != null) 
        {
            return leftResult;
        }

        Node rightResult = containsNodeRecursive(current.right, expiredate);
        if (rightResult != null) 
        {
            return rightResult;
        }

        return null;
    }

    public Node containsNode(Date expiredate) {
        return containsNodeRecursive(root, expiredate);
    }

    private boolean processNodeRecursive(Node current, String name, int quantity, Date requiredDate, Date currentDate) {
        if (current == null) {
            return false;
        }

        if (requiredDate.before(current.ExpireDate) || requiredDate.equals(current.ExpireDate)) 
        {
            // We can start searching for the drug
            // Perform check for all medications
            for (int i = 0; i < current.medicationStores.size(); i++)
            {
                Medication medObj = current.medicationStores.get(i);
                if (medObj.Name.equals(name))
                {
                    // Found the medication, now we check quantity
                    if (medObj.Quantity > quantity)
                    {
                        medObj.Quantity -= quantity;
                        current.medicationStores.set(i, medObj);
                        return true;
                    }
                    else if (medObj.Quantity == quantity)
                    {
                        current.medicationStores.remove(i);
                        if (current.medicationStores.size() == 0)
                        {
                            delete(current.ExpireDate); // Remove from tree
                            return true;
                        }
                    }

                }

            }

            // We can search left and right

            if (processNodeRecursive(current.left, name, quantity, requiredDate, currentDate))
            {
                return true;
            }

            if (processNodeRecursive(current.right, name, quantity, requiredDate, currentDate))
            {
                return true;
            }

        }
        else
        {
            // We should only search right
            if (processNodeRecursive(current.right, name, quantity, requiredDate, currentDate))
            {
                return true;
            }
        }

        return false;
    }

    public boolean processNode(String name, int quantity, Date requiredDate, Date currentDate) {
        return processNodeRecursive(root, name, quantity, requiredDate, currentDate);
    }

    private Date findSmallestValue(Node root) {
        return root.left == null ? root.ExpireDate : findSmallestValue(root.left);
    }
    private Node deleteRecursive(Node current, Date value) {
        if (current == null) {
            return null;
        }

        if (current.ExpireDate.equals(value)) {
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
            Date smallestValue = findSmallestValue(current.right);
            current.ExpireDate = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue);
            return current;
        }
        if (value.before(current.ExpireDate)) {
            current.left = deleteRecursive(current.left, value);
            return current;
        }
        current.right = deleteRecursive(current.right, value);
        return current;
    }

    public void delete(Date value) {
        root = deleteRecursive(root, value);
    }


    private Node findSuccessor(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private Node deleteNode(Node node) 
    {
        if (node.left == null)
        {
            return node.right;
        } else if (node.right == null) 
        {
            return node.left;
        } else {
            node = findSuccessor(node.right);
            if (node.right != null)
            {
                node.right = deleteNode(node.right);
            }

        }
        return node;
    }

    private Node deleteAllBeforeRecursive(Node current, Date valueDate) 
    {
        if (current == null) {
            return null;
        }

        current.left = deleteAllBeforeRecursive(current.left, valueDate);
        current.right = deleteAllBeforeRecursive(current.right, valueDate);
        
        // Delete the current node if its date is before the value date
        if (current.ExpireDate.before(valueDate)) {
            return current.right;
        }

        return current;
    }


    public void removeExpiredNodes(Date value) 
    {
        root = deleteAllBeforeRecursive(root, value);
    }

    public String traverseInOrder(Node node, String strOutput, Date currentDate) {
        if (node != null) 
        {
            if (currentDate.before(node.ExpireDate))
            {
                strOutput = traverseInOrder(node.left, strOutput, currentDate);

                StringBuilder strOutputBuilder = new StringBuilder(strOutput);
                for (int i = 0; i < node.medicationStores.size(); i++)
                {
                    strOutputBuilder.append(node.medicationStores.get(i).Name).append(" ").append(node.medicationStores.get(i).Quantity).append(" ").append(format.format(node.ExpireDate)).append("\n");
                }
                strOutput = strOutputBuilder.toString();

                strOutput = traverseInOrder(node.right, strOutput, currentDate);
            }
            else if (currentDate.after(node.ExpireDate)) 
            {
                strOutput = traverseInOrder(node.right, strOutput, currentDate);
            }
            else
            {
                strOutput = traverseInOrder(node.right, strOutput, currentDate);
            }
        }
        return strOutput;
    }

//    public void traversePreOrder(Node node) {
//        if (node != null) {
//            System.out.print(" " + node.MedicationNumber);
//            traversePreOrder(node.left);
//            traversePreOrder(node.right);
//        }
//    }
//
//    public void traversePostOrder(Node node) {
//        if (node != null) {
//            traversePostOrder(node.left);
//            traversePostOrder(node.right);
//            System.out.print(" " + node.MedicationNumber);
//        }
//    }



}
