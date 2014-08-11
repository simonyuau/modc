package edu.monash.merc.eddy.modc.ws.jaxbadapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by simonyu on 1/08/2014.
 */
public class SimpleJaxbDateAdapter extends XmlAdapter<String, Date> {

    //private static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Date unmarshal(String strDate) throws Exception {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(strDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String marshal(Date date) throws Exception {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}
