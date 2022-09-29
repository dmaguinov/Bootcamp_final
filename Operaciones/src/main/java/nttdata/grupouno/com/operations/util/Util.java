package nttdata.grupouno.com.operations.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.encrypt.Encryptors;

public final class Util {

    private Util(){

    }

    public static String dateToString(Date date){
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        return formatter.format(date);
    }

    public static Date stringToDate(String dateString) {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd");
        if (dateString == null){
            return null;
        }
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date addDay(Date date, int amount){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,amount);
        return calendar.getTime();
    }

    public static String getMonth(Date date){
        DateFormat formatter = new SimpleDateFormat("MM");
        return formatter.format(date);
    }

    public static String getYear(Date date){
        DateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(date);
    }

    /**
     * Generate cart number
     * @return
     */
    public static String generateCartNumber() {
        return "4152".concat(RandomStringUtils.randomNumeric(12));
    }

    public String encriptAES(String pan, String codeClient){
        Encryptors.standard("password", "salt");

        return "";
    }

    public String desencriptAES(String panEncript, String codeClient) {
        
        return "";
    }

    public String generateHash(String text) {
        return BCrypt.hashpw(text, BCrypt.gensalt());
    }
}
