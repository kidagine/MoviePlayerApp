 package privatemoviecollection.bll.util;

/**
 * The {@code TimeConverter} class is responsible for
 * converting time between seconds (int) and a format specific to our 
 * application (String). Format of string with time depends on amount of seconds:
 * If seconds are less than 3600, string is in format "mm:ss".
 * If seconds are more or equal 3600, string is in format "h:mm:ss".
 * 
 * @author schemabuoi
 * @author kiddo
 */
public class TimeConverter {
    
    /**
     * Converts the time from seconds (int) to
     * format specific for our application (string).
     * 
     * @param timeInSeconds The time in seconds.
     * @return The time in string format.
     */
    public static String convertToString(int timeInSeconds)
    {
        String result = "";
        int hours = timeInSeconds/3600;
        timeInSeconds %= 3600;
        int minutes = timeInSeconds/60;
        timeInSeconds %= 60;
        int seconds = timeInSeconds;
        if(hours != 0)
        {
            result += hours + ":";
        }
        if(minutes != 0)
        {
            if(minutes<10)
            {
                result += "0";
            }
            result += minutes + ":";
        }
        else
        {
            result += "00:";
        }
        if(seconds<10)
        {
            result += "0";
        }
        result += timeInSeconds;
        return result;
        
    }
    
    /**
     * Converts the time from format specific for 
     * our application (string) to seconds (int).
     * 
     * @param timeInString The time in string.
     * @return The time in seconds.
     */
    public static int convertToInt(String timeInString)
    {
        String[] time = timeInString.split(":");
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if(time.length == 2)
        {
            minutes += Integer.parseInt(time[0]);
            seconds += Integer.parseInt(time[1]);
        }
        else
        {
            hours += Integer.parseInt(time[0]);
            minutes += Integer.parseInt(time[1]);
            seconds += Integer.parseInt(time[2]);
            
        }
        return seconds + minutes*60 + hours*3600;
        
    }
    
}
