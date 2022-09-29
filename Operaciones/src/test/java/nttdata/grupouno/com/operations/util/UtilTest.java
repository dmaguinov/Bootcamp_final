package nttdata.grupouno.com.operations.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void getMonth() {
        String resp = Util.getMonth(dateRepresentation);
        assertEquals("01", resp);
    }

    @Test
    void getYear() {
        assertEquals("2022", Util.getYear(dateRepresentation));
    }

    @Test
    void stringToDate() throws ParseException {
        assertEquals(dateRepresentation, Util.stringToDate("2022.01.01"));
    }

    @Test
    void generateCartNumber(){
        assertNotNull(Util.generateCartNumber());
    }
}