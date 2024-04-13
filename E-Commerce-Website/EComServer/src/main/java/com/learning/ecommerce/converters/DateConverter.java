package com.learning.ecommerce.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {
	public static String covertDaysIntoDate(int d) {
		// get a calendar Instance
		Calendar c = Calendar.getInstance();
		// Set calendar time to today's date
		c.setTime(new Date());
		// add dates in todays days
		c.add(Calendar.DATE, d);
		// create a SimpleDateFormat object
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		// format the date into String
		return sdf.format(c.getTime());
	}

	public static int convertDateIntoDays(String dateString) throws ParseException {
		// Create a SimpleDateFormat object for parsing the date string
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		// Parse the date string into a Date object
		Date date = sdf.parse(dateString);

		// Get a calendar instance for today's date
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(new Date()); // Set calendar time to today's date

		// Get a calendar instance for the parsed date
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(date);

		// Calculate the difference in milliseconds between the two dates
		long differenceInMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();

		// Convert milliseconds to days
		int daysDifference = (int) (differenceInMillis / (1000 * 60 * 60 * 24));

		return daysDifference;
	}
}
