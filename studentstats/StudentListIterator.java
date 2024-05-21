package studentstats;

import java.util.NoSuchElementException;

import itertools.DoubleEndedIterator;

import studentapi.*;

/**
 * A (double ended) iterator over student records pulled from the student API.
 *
 * <p>This does not load the whole student list immediately, but rather queries the API ({@link
 * StudentList#getPage}) only as needed.
 */
public class StudentListIterator implements DoubleEndedIterator<Student> {
    // TASK(8.1): Implement StudentListIterator: Add any fields you require
    private StudentList studentList;
    private int retryLimit;
    private int currentPageIndex = 0;
    private List<Student> studentBuffer = new ArrayList<>();
    private boolean hasMorePages = true;

    /**
     * Construct an iterator over the given {@link StudentList} with the specified retry quota.
     *
     * @param list The API interface.
     * @param retries The number of times to retry a query after getting {@link
     *     QueryTimedOutException} before declaring the API unreachable and throwing an {@link
     *     ApiUnreachableException}.
     */
    public StudentListIterator(StudentList list, int retries) {
        // TASK(8.2): Implement StudentListIterator
        this.studentList = list;
        this.retryLimit = retries;
    }


    /**
     * Construct an iterator over the given {@link StudentList} with a default retry quota of 3.
     *
     * @param list The API interface.
     */
    public StudentListIterator(StudentList list) {
        // TASK(8.3): Implement StudentListIterator
        this(list, 3);
    }
    

    @Override
    public boolean hasNext() {
        // TASK(8.4): Implement StudentListIterator
        if (studentBuffer.isEmpty() && hasMorePages) {
            loadNextPage();
        }
        return !studentBuffer.isEmpty();
    }


    @Override
    public Student next() {
    	// TASK(8.5): Implement StudentListIterator
        if (!hasNext()) {
            throw new NoSuchElementException("No more students available.");
        }
        return studentBuffer.remove(0);
    }

    @Override
    public Student reverseNext() {
    	// TASK(8.6): Implement StudentListIterator
        if (!hasNext()) {
            throw new NoSuchElementException("No more students available.");
        }
        return studentBuffer.remove(studentBuffer.size() - 1);
    }

    private void loadNextPage() {
        for (int attempt = 0; attempt < retryLimit; attempt++) {
            try {
                if (currentPageIndex >= studentList.getNumPages()) {
                    hasMorePages = false;
                    return;
                }
                Student[] students = studentList.getPage(currentPageIndex);
                if (students.length == 0) {
                    hasMorePages = false;
                    return;
                }
                studentBuffer.addAll(Arrays.asList(students));
                currentPageIndex++;
                return;
            } catch (QueryTimedOutException e) {
                // retry logic
            }
        }
        throw new ApiUnreachableException();
    }
}

       
       
    }

}   
