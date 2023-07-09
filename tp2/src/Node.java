import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Node 
{
    Date ExpireDate;
    Node left;
    Node right;
    int height;
    public List<Medication> medicationStores = new ArrayList<Medication>();

    Node(Date expire_date, String name, int quantity) 
    {
        Medication medObj = new Medication();
        medObj.Name = name;
        medObj.Quantity = quantity;
        medicationStores.add(medObj);
        ExpireDate = expire_date;
        right = null;
        left = null;
        height = 0;
    }

}
