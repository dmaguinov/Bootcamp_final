package nttdata.grupouno.com.operations.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Util {

    public static String dateToString(Date date){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String resp = formatter.format(date);
        return resp;
    }

    public static Date stringToDate(String dateString){
        Date date;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd");
        try {
            date =(Date) formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static String getMonth(Date date){
        DateFormat formatter = new SimpleDateFormat("MM");
        String resp = formatter.format(date);
        return resp;
    }

    public static String getYear(Date date){
        DateFormat formatter = new SimpleDateFormat("yyyy");
        String resp = formatter.format(date);
        return resp;
    }

}
