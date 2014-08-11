package edu.monash.merc.eddy.modc.web.support;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by simonyu on 6/08/2014.
 */
public class DateTypeEditor extends PropertyEditorSupport {

    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final int DATE_FORMAT_LEN = 10;

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? TIMESTAMP_FORMAT.format(value) : "");
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        text = text.trim();
        if (!StringUtils.hasText(text)) {
            setValue(null);
            return;
        }
        try {
            if (text.length() <= DATE_FORMAT_LEN) {
                setValue(new java.sql.Date(DATE_FORMAT.parse(text).getTime()));
            } else {
                setValue(new java.sql.Timestamp(TIMESTAMP_FORMAT.parse(text).getTime()));
            }
        } catch (ParseException ex) {
            IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
            iae.initCause(ex);
            throw iae;
        }
    }
}
