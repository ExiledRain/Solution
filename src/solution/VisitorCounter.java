package solution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Assignment class to calculate maximum amount of customers at point in time.
 *
 * @author Vassili Moskaljov
 * @version 1.0
 */
public class VisitorCounter {
    private final List<Times> timesList = new ArrayList<>();
    private final Map<Integer, ArrayList<Times>> resultingCollection = new HashMap<>();

    /**
     * Method initiates algorithm.
     * 1.Take input with file location from user.
     * 2.Parse the document, putting data to timesList collection.
     * 3.Sort timesList collection.
     * 4.Calculate times data.
     * 5.Print resulting String.
     */
    public void init() {
        FileReader in = getInput();
        if (parseFile(in)) {
            return;
        }
        timesList.sort(new TimesComparator());
        String result = calculateData(timesList);
        System.out.println(result);
    }

    /**
     * Takes user input with file location, and runs until it will get a correct input or Quit command.
     *
     * @return FileReader with correctly set file location.
     */
    private FileReader getInput() {
        Scanner input = new Scanner(System.in);
        String location;
        FileReader in;
        while (true) {
            try {
                System.out.println("Please specify file location io hit Q to exit (You can copy and paste path from the explorer) : ");
                location = input.nextLine();
                if (location.isEmpty() || location.equalsIgnoreCase("q")) {
                    return null;
                }
                in = new FileReader(location);
                break;
            } catch (FileNotFoundException e) {
                System.err.println("File location was specified incorrectly, please check your input (You can copy and paste path from the explorer).");
            }
        }
        return in;
    }

    /**
     * Parse text document by line, each parsed line is converted to the Times class and added to the collection.
     *
     * @param in FileReader with text file that will be parsed.
     * @return If file parsed successfully then it will return false.
     */
    private boolean parseFile(FileReader in) {
        if (in == null) return true;

        try (BufferedReader fileInput = new BufferedReader(in)) {
            String line;
            while ((line = fileInput.readLine()) != null) {
                parseTime(line);
            }
        } catch (IOException e) {
            System.err.println("Something went wrong,maybe you have empty line in your file or one of them not matching the template");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Parses each line of input to the time when customer came in, and time when customer left,
     * converted in to the minutes and adds each input to the timesList collection.
     *
     * @param timeString String containing time when people came and left.
     */
    private void parseTime(String timeString) {
        int numberOfBadLines = 0;
        if (timeString == null || timeString.length() != 11) {
            return;
        }

        try {
            String[] times = timeString.split(",");
            String[] startingTime = times[0].split(":");
            String[] endingTime = times[1].split(":");

            int in = (Integer.parseInt(startingTime[0]) * 60) + Integer.parseInt(startingTime[1]);
            int out = (Integer.parseInt(endingTime[0]) * 60) + Integer.parseInt(endingTime[1]);
            timesList.add(new Times(in, out));
        } catch (NumberFormatException e) {
            System.err.println("Time input is incorrect, please follow the template HH:MM,HH:MM");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            numberOfBadLines++;
            System.err.println("You have " + numberOfBadLines + " of incorrect line(s).");
        }
    }

    /**
     * Checks all time variables and count max customer at point in time.
     *
     * @param timesCollection take collection of sorted Solution.Times objects.
     * @return String with final result for the output to the customer.
     */
    private String calculateData(List<Times> timesCollection) {
        int largestNumber = 0;
        for (int i = 0; i < timesCollection.size(); i++) {
            int counter = 1;
            Times biggestGap = timesCollection.get(i);

            for (int j = i + 1; j < timesCollection.size(); j++) {
                Times compareTimes = compareTimes(timesCollection.get(i), timesCollection.get(j));
                if (compareTimes != null && timesCollection.get(i).isGreater(timesCollection.get(j))) {
                    biggestGap.setIn(Math.max(biggestGap.getIn(), compareTimes.getIn()));
                    biggestGap.setOut(Math.min(biggestGap.getOut(), compareTimes.getOut()));
                    counter++;
                }
            }

            ArrayList<Times> tempList = new ArrayList<Times>() {{
                add(biggestGap);
            }};
            if (resultingCollection.containsKey(counter)) {
                tempList.addAll(resultingCollection.get(counter));
            }
            resultingCollection.put(counter, tempList);
            if (largestNumber < counter) {
                largestNumber = counter;
            }
        }
        return makeString(resultingCollection.get(largestNumber), largestNumber);
    }

    /**
     * Method makes a String for the resulting output from input inserted Solution.Times collection and number of customers.
     *
     * @param counter largest number of customers at point in time.
     * @param times   collection of time frames with maximum number of customers at point in time.
     */
    private String makeString(List<Times> times, int counter) {
        StringBuilder resultingString = new StringBuilder();

        try {
            for (Times time : times) {
                resultingString.append(convertToTime(time)).append(", ");
            }

        } catch (NullPointerException e) {
            System.err.println("You have empty lines or incorrect input format in your file, it should follow this template HH:MM,HH:MM\nPlease fix them and try again.");
        }

        return resultingString.append(counter).toString();
    }

    /**
     * Converts Solution.Times class to time String by template HH:MM,HH:MM
     *
     * @param times Solution.Times class to be converted.
     */
    private String convertToTime(Times times) {
        return getStringFromMinutes(times.getIn()) + "," + getStringFromMinutes(times.getOut());
    }

    /**
     * Converts minutes to String of time by template HH:MM
     *
     * @param time amount of time in minutes.
     */
    private static String getStringFromMinutes(int time) {
        int hours = time / 60;
        int minutes = time % 60;
        String minutesString = String.valueOf(minutes).length() < 2 ? "0" + minutes : String.valueOf(minutes);
        return String.valueOf(hours).length() < 2 ? "0" + hours + ":" + minutesString : hours + ":" + minutesString;
    }

    /**
     * Method compares 2 Times objects for eligibility and returns gap in time.
     *
     * @param a 1st Solution.Times object for comparing.
     * @param b 2nd Solution.Times object for comparing.
     * @return new Solution.Times object with the smallest time gap.
     */
    private Times compareTimes(Times a, Times b) {
        if (b.getIn() >= a.getIn() && b.getIn() <= a.getOut()) {
            return new Times(Math.max(b.getIn(), a.getIn()), Math.min(b.getOut(), a.getOut()));
        } else {
            return null;
        }
    }
}