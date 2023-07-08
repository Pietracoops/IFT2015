import java.util.Date;
import java.util.Calendar;
public class Commande {
    String Name;
    int Quantity;
    Date RequiredDate;

    Commande(String nameInput, int quantityInput, Date currentDate)
    {
        Name = nameInput;
        Quantity = quantityInput;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, Quantity);
        RequiredDate = calendar.getTime();
    }
}
