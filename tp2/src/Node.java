public class Node extends MedicationsInfo {
    Node left;
    Node right;
    int height;

    Node(int value) {
        this.MedicationNumber = value;
        right = null;
        left = null;
        height = 0;
    }

}
