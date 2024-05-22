package studentstats;

import itertools.Itertools;
import studentapi.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/** A class for computing the average of a number of integer samples. */
class IntegerAverage {
    private int totalSum = 0;
    private int sampleCount = 0;

    public void addSample(int sample) {
        totalSum += sample;
        sampleCount++;
    }

    public int getAverage() {
        return totalSum / sampleCount;
    }
}

/** A {@link BiFunction} adding an integer sample to an {@link IntegerAverage}. */
class IntegerAverageReduction implements BiFunction<IntegerAverage, Integer, IntegerAverage> {
    public IntegerAverage apply(IntegerAverage avg, Integer sample) {
        if (sample == null) return avg;
        avg.addSample(sample);
        return avg;
    }
}

/** A {@link Function} retrieving the mark for a particular unit from a {@link Student} record. */
class GetUnitMark implements Function<Student, Integer> {
    String unitCode;

    public GetUnitMark(String unitCode) {
        this.unitCode = unitCode;
    }

    public Integer apply(Student student) {
        return student.getMark(unitCode);
    }
}

/** An iterator to traverse the list of students in reverse order. */
class ReverseStudentIterator implements Iterator<Student> {
    private final List<Student> studentList;
    private int currentIndex;

    public ReverseStudentIterator(List<Student> studentList) {
        this.studentList = studentList;
        this.currentIndex = studentList.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return currentIndex >= 0;
    }

    @Override
    public Student next() {
        if (currentIndex < 0) {
            return null;
        }
        return studentList.get(currentIndex--);
    }
}

/** A collection of statistical and analytical methods for working with the student API. */
public class StudentStats {
    /**
     * Returns the average mark (integer division) across all students who have completed a given
     * unit.
     *
     * @param list The student API interface.
     * @param unit The unit code.
     * @return The average mark for all students who have taken `unit`.
     */
    public static int unitAverage(StudentList list, String unit) {
        return Itertools.reduce(
                        Itertools.map(new StudentListIterator(list), new GetUnitMark(unit)),
                        new IntegerAverage(),
                        new IntegerAverageReduction())
                .getAverage();
    }

    /**
     * Returns an iterator over the students who have taken a given unit, from newest to oldest.
     *
     * @param list The student API interface.
     * @param unit The unit code.
     * @return An iterator over the students who have taken `unit`, from newest to oldest.
     */
    public static Iterator<Student> unitNewestStudents(StudentList list, String unit) {
        List<Student> relevantStudents = new ArrayList<>();
        int totalPages = list.getNumPages();
        int currentPage = totalPages - 1;
        int totalFetched = 0; // Track the total number of students fetched

        while (currentPage >= 0 && relevantStudents.size() <= 10) {
            Student[] pageContent = fetchPage(list, currentPage);
            if (pageContent != null) {
                totalFetched += pageContent.length;
                for (Student student : pageContent) {
                    if (student.getMark(unit) != null) {
                        relevantStudents.add(student);
                        if (relevantStudents.size() >= 10) {
                            break;
                        }
                    }
                }
            }
            if (relevantStudents.size() >= 10) {
                break;
            }
            currentPage--;
        }

        relevantStudents.sort(Comparator.comparing(Student::getId).reversed());
        return relevantStudents.iterator();
    }

    private static Student[] fetchPage(StudentList studentList, int pageNumber) {
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return studentList.getPage(pageNumber);
            } catch (QueryTimedOutException e) {
                if (attempt == maxRetries) {
                    return null;
                }
            }
        }
        return null;
    }
}
