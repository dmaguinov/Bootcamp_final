package nttdata.grupouno.com.operations.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class UtilTest {
    @Autowired
    Date dateRepresentation;

    @BeforeEach
    void init(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2022);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dateRepresentation = cal.getTime();
    }

    @Test
    void dateToString() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String resp = formatter.format(dateRepresentation);
        String h = Util.dateToString(dateRepresentation);
        assertEquals(resp, h);
    }

    @Test
    void testDateToString() {
    }

    @Test
    void getMonth() {
        String resp = Util.getMonth(dateRepresentation);
        assertEquals(resp, "01");
    }

    @Test
    void getYear() {
        String resp = Util.getYear(dateRepresentation);
        assertEquals(resp, "2022");
    }

    @Test
    void comparetest(){
        Integer a= 10;
        Integer b= 9;
        System.out.println(a.compareTo(b));
    }

    @Test
    void stringToDate() throws ParseException {
        String sDate1="2022.09.25";
        String sDate2="2022.09.26";
        Date convertedDate;
        Date convertedDate2;
        Date today = new Date();
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy.MM.dd");
        convertedDate =(Date) formatter1.parse(sDate1);
        convertedDate2 =(Date) formatter1.parse(sDate2);
        today.compareTo(today);
        System.out.println(convertedDate);
        System.out.println("<<>>>>>" + convertedDate.compareTo(convertedDate2));
        Integer a= 10;
        Integer b= 9;
        System.out.println(a.compareTo(b));
    }
}