package com.watsoo.dms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class TimeUtility {

	public static Date getTimeStringToDateFormat(String dateTime) {
        // Parse the dateTime string into a Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date eventServerCreateTime = null;
        try {
            eventServerCreateTime = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parse exception
        }
        return eventServerCreateTime;
    }
	
	
	public static Date convertDateFormater(Date date, String dateFormat) {
		try {
			date = new SimpleDateFormat(dateFormat).getCalendar().getTime();
			return date;
		} catch (Exception e) {
			return null;
		}
	}


}
