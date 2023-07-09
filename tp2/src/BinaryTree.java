import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BinaryTree 
{
    Node root;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Calculates the height of a given node in a binary tree.
     *
     * @param  node  the node whose height needs to be calculated
     * @return       the height of the node
     */
    private int height(Node node) 
    {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * Calculates the balance factor of a given node in a binary tree.
     *
     * @param  node  the node whose balance factor is to be calculated
     * @return       the balance factor of the node
     */
    private int getBalanceFactor(Node node) 
    {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    /**
     * Rotates the given node to the left.
     *
     * @param  node  the node to be rotated
     * @return       the new root node after rotation
     */
    private Node rotateLeft(Node node) 
    {
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

    /**
     * Rotates the given node to the right.
     *
     * @param  node  the node to be rotated
     * @return       the new root node after rotation
     */
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

    /**
     * Adds a node to the binary search tree in a balanced manner.
     *
     * @param  node       the root node of the binary search tree
     * @param  nodeToAdd  the node to add to the binary search tree
     * @return            the root node of the modified binary search tree
     */
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

    /**
     * Entry point for the addRecursiveBalanced function.
     *
     * @param  node  the node to be added to the tree
     */
    public void addBalanced(Node node) 
    {
        root = addRecursiveBalanced(root, node);
    }

    /**
     * Recursive function to check if a given node contains a specific node name.
     *
     * @param  current  the current node being checked
     * @param  name     the name of the node to search for
     * @return          the node that contains the specified name, or null if not found
     */
    private Node containsNodeNameRecursive(Node current, String name) 
    {
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

    /**
     * Entry point for the containsNodeNameRecursive function.
     *
     * @param  name  name of node
     * @return       the node that contains the specified name, or null if not found
     */
    public Node containsNodeName(String name) 
    {
        return containsNodeNameRecursive(root, name);
    }

    /**
     * Recursively searches for a node in the tree that has the specified expire date.
     *
     * @param  current     the current node being examined
     * @param  expiredate  the expire date to search for
     * @return             the node with the specified expire date, or null if not found
     */
    private Node containsNodeRecursive(Node current, Date expiredate) 
    {
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

    /**
     * Retrieves the node containing the given expiration date in the binary tree.
     *
     * @param  expiredate  the expiration date to search for
     * @return             the node containing the expiration date, or null if not found
     */
    public Node containsNode(Date expiredate) {
        return containsNodeRecursive(root, expiredate);
    }

    /**
     * Process a node recursively to search for a medication and update its quantity.
     *
     * @param  current         the current node in the search tree
     * @param  name            the name of the medication to search for
     * @param  quantity        the quantity of medication to update
     * @param  requiredDate    the required date for the medication
     * @param  currentDate     the current date
     * @return                 true if the medication is found and updated, false otherwise
     */
    private boolean processNodeRecursive(Node current, String name, int quantity, Date requiredDate, Date currentDate) 
    {
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

    /**
     * Process the node with the given parameters.
     *
     * @param  name           the name of the node
     * @param  quantity       the quantity of the node
     * @param  requiredDate   the required date for the node
     * @param  currentDate    the current date
     * @return                true if the node is processed successfully, false otherwise
     */
    public boolean processNode(String name, int quantity, Date requiredDate, Date currentDate) 
    {
        return processNodeRecursive(root, name, quantity, requiredDate, currentDate);
    }

    /**
     * Finds the smallest value in a binary tree.
     *
     * @param  root  the root node of the binary tree
     * @return       the smallest value in the binary tree
     */
    private Date findSmallestValue(Node root) 
    {
        return root.left == null ? root.ExpireDate : findSmallestValue(root.left);
    }

    /**
     * Deletes a node from the binary search tree recursively.
     *
     * @param  current   the current node in the binary search tree
     * @param  value     the value to be deleted
     * @return           the updated binary search tree after deletion
     */
    private Node deleteRecursive(Node current, Date value) 
    {
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

    /**
     * Entry point for the deleteRecursive function, Deletes a value from the binary search tree.
     *
     * @param  value the value to be deleted
     * @return       void
     */
    public void delete(Date value) 
    {
        root = deleteRecursive(root, value);
    }

    /**
     * Find the successor node of the given node.
     *
     * @param  node  the node for which to find the successor
     * @return       the successor node
     */
    private Node findSuccessor(Node node) 
    {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * Deletes all nodes in the binary tree that have a date before the specified value date.
     *
     * @param  current      the current node being processed
     * @param  valueDate    the date to compare with the node's date
     * @return              the modified binary tree after deletion
     */
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

    /**
     * Removes all expired nodes from the tree starting from the root node.
     *
     * @param  value  the date used to determine which nodes are expired
     */
    public void removeExpiredNodes(Date value) 
    {
        root = deleteAllBeforeRecursive(root, value);
    }

    /**
     * Traverses a binary tree in order and generates a string output of medication store information.
     *
     * @param  node         the root node of the binary tree
     * @param  strOutput    the current string output
     * @param  currentDate  the current date for comparison with medication expiration dates
     * @return              the updated string output after traversing the binary tree
     */
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

}
