import java.util.Date;
import java.text.SimpleDateFormat;

public class Time{
    private String strDay, strMonth;
    private String HHMMSS, DDMMYY;
    
    public Time(){
        Date date = new Date();
        SimpleDateFormat formatter = 
        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatterStrDay = new SimpleDateFormat("EEEE");
        SimpleDateFormat formatterStrMonth = new SimpleDateFormat("MMMM");
        
        strDay = formatterStrDay.format(new Date());
        strMonth = formatterStrMonth.format(new Date());
        HHMMSS = formatter.format(date).substring(11,19);
        DDMMYY = formatter.format(date).substring(0,10);
    }
    
    public static String[] getWeek(){
        String[] dayOfTheWeek = 
        {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", 
         "Saturday", "Sunday"};
        return dayOfTheWeek;
    }
    public static String[] getMonth(){
        String[] month = 
        {"January", "February", "March", "April", "May", "June", 
         "July", "August", "September", "October", "November", "December"};
        return month;
    }
    public String getHHMMSS(){return HHMMSS;}
    public String getDDMMYY(){return DDMMYY;}
    public String getStrDay(){return strDay;}
    public String getStrMonth(){return strMonth;}
    
    public String toString(){
        return DDMMYY + " " + HHMMSS;
    }
} 