import ca.ubc.cs304.controller.SuperCar;
import ca.ubc.cs304.database.DatabaseConnectionHandler;
import org.junit.Before;
import org.junit.Test;

public class DBCHTest  {



    @Test
    public void testDailyReport() {
        SuperCar sc = new SuperCar();
        sc.start();
        sc.dbHandler.dailyRentalBranch("21-NOV-19", "UBC");
    }

}
