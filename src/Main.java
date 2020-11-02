import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Main {

    private static final List<String> MEDICINES = Arrays.asList("hydrocodone", "simvastatin", "lisinopril", "besylate", "metformin", "omeprazole", "azithromycin");
    private static final List<String> DAYS = Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");
    private static final int ASCII_OFFSET = 96;

    static int walkCounter = 0;
    static int doubleCounter = 0;
    static int integerCounter = 0;
    static int idkCounter = 0;
    public static void main(String[] args) throws ParseException {
        LocalDate startDate = LocalDate.of(1900, 1, 1);
        LocalDate endDate = LocalDate.of(2100, 12, 31);
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            int dayOfMonth = date.getDayOfMonth();
            int monthOfYear = date.getMonthValue();
            int year = date.getYear();

            String dayOfWeek = getDayOfWeek(dayOfMonth, monthOfYear, year);

            Map<Character, Integer> letterByNumber = new HashMap<>();
            Map<String, Double> dayByLetterAverage = new HashMap<>();

            double dayAverage = calculateDayAverage(dayOfWeek, letterByNumber, dayByLetterAverage);

            Map<Integer, String> medicineByFirstLetterNumber = prepareMedicineByLetter(dayOfWeek, letterByNumber, dayAverage);

            String selectedMedicine = selectMedicine(dayOfWeek, dayAverage, medicineByFirstLetterNumber);

            calculateNumberOfPills(dayOfMonth, selectedMedicine);
        }
        System.out.println("Walk counter: " + walkCounter);
        System.out.println("Double pills counter: " + doubleCounter);
        System.out.println("Integer pills counter: " + integerCounter);
        System.out.println("Idk counter: " + idkCounter);
    }

    private static Map<Integer, String> prepareMedicineByLetter(String dayOfWeek, Map<Character, Integer> letterByNumber, double dayAverage) {
        Map<Integer, String> medicineByFirstLetterNumber = new HashMap<>();
        MEDICINES.forEach(med -> {
            char c = med.charAt(0);
            int letterNumber = letterByNumber.get(c);
            medicineByFirstLetterNumber.put(letterNumber, med);
        });

        System.out.println("\nSelected day: " + dayOfWeek + " and its average: " + dayAverage);
        System.out.println("\nMedicine and first letter number:");
        medicineByFirstLetterNumber.entrySet()
                .forEach(System.out::println);
        return medicineByFirstLetterNumber;
    }

    private static String selectMedicine(String dayOfWeek, double dayAverage, Map<Integer, String> medicineByFirstLetterNumber) {
        List<Integer> medicineFirstNumbers = new ArrayList<>(medicineByFirstLetterNumber.keySet());
        Integer medicineNumberClosest = medicineFirstNumbers.stream()
                .min(Comparator.comparingDouble(med -> Math.abs(dayAverage - med)))
                .get();

        String selectedMedicine = medicineByFirstLetterNumber.get(medicineNumberClosest);
        System.out.println("\nClosest value: " + medicineNumberClosest + " selected medicineByFirstLetterNumber for day: " + dayOfWeek + " is: " + selectedMedicine + "\n");
        return selectedMedicine;
    }

    private static double calculateDayAverage(String dayOfWeek, Map<Character, Integer> letterByNumber, Map<String, Double> dayByLetterAverage) {
        for (char c = 'a'; c <= 'z'; c++) {
            letterByNumber.put(c, ((int) c - ASCII_OFFSET));
        }

        System.out.println("Day of week average number: ");
        DAYS.forEach(day -> {
            double dayAverage = day.chars()
                    .mapToObj(c -> (char) c)
                    .mapToInt(letterByNumber::get)
                    .average()
                    .getAsDouble();
            System.out.println(day + ":" + dayAverage);
            dayByLetterAverage.put(day, dayAverage);
        });

        return dayByLetterAverage.get(dayOfWeek);
    }

    private static void calculateNumberOfPills(int dayOfMonth, String selectedMedicine) {
        if (dayOfMonth % 10 == 0) {
            System.out.println("Kowalski will not take any medicine, instead will go for a walk.");
           walkCounter++;
        } else {
            int numberOfPills = 0;
            double squareRoot = Math.sqrt(dayOfMonth);
            if (squareRoot % 2 == 0) {
                System.out.println("Square root of day(" + dayOfMonth + ") is integer, it is: " + squareRoot);
                numberOfPills = (int) squareRoot;
                integerCounter++;
            } else if (dayOfMonth > 15 && dayOfMonth < 30 || squareRoot % 2 != 0) {
                numberOfPills = 2;
                System.out.println("(15 < x < 30 || √x / != liczba całkowita)");
                doubleCounter++;
            } else {
                idkCounter++;
            }
            System.out.println("Kowalski will take: " + numberOfPills + " of: " + selectedMedicine);
        }
    }

    private static String getDayOfWeek(int dayOfMonth, int monthOfYear, int year) throws ParseException {
        String dateString = String.format("%d-%d-%d", year, monthOfYear, dayOfMonth);
        Date date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
        String dayOfWeek2 = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

        return dayOfWeek2.toLowerCase();
    }
}
