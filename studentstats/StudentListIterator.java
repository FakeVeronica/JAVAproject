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
    private final StudentList studentList;
    private Student[] currentPage;
    private int currentPageNumber;
    private int currentIndex;
    private final int retries;
    private int retriesRemaining;
    private boolean hasLoadedLastPage;

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
        this.retries = retries;
        this.retriesRemaining = retries;
        this.currentPage = null;
        this.currentPageNumber = -1;
        this.currentIndex = 0;
        this.hasLoadedLastPage = false;
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

    private void loadPage(int pageNum) throws ApiUnreachableException {
        int attempts = 0;
        while (attempts < retries) {
            try {
                currentPage = studentList.getPage(pageNum);
                currentPageNumber = pageNum;
                currentIndex = 0;
                if (pageNum == studentList.getNumPages() - 1) {
                    hasLoadedLastPage = true;
                }
                return;
            } catch (QueryTimedOutException e) {
                attempts++;
            }
        }
        throw new ApiUnreachableException();
    }
    

    @Override
    public boolean hasNext() {
        // TASK(8.4): Implement StudentListIterator
        if (currentPage == null || currentIndex >= currentPage.length) {
            if (currentPageNumber >= studentList.getNumPages() - 1) {
                return false;
            }
            try {
                loadPage(currentPageNumber + 1);
            } catch (ApiUnreachableException e) {
                return false;
            }
        }
        return currentIndex < currentPage.length;
    }

    @Override
    public Student next() {
        // TASK(8.5): Implement StudentListIterator
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return currentPage[currentIndex++];
    }

    @Override
    public Student reverseNext() {
        // TASK(8.6): Implement StudentListIterator
        if (currentPage == null || currentIndex == 0) {
            if (currentPageNumber == 0 && currentIndex == 0) {
                throw new NoSuchElementException("No more elements to iterate backwards.");
            }
            try {
                loadPage(currentPageNumber > 0 ? --currentPageNumber : studentList.getNumPages() - 1);
                currentIndex = currentPage.length;
            } catch (ApiUnreachableException e) {
                throw new NoSuchElementException("API unreachable, cannot load previous page.");
            }
        }
        return currentPage[--currentIndex];
    }
}
