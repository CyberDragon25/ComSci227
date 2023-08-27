package hw1;

/**
 * printer class
 */
public class Printer {
    /**
     * gives the total number of sheets availbale
     */
    private int sheetsAvailable = 0;
    /**
     * page number of the document that will be printed
     */
    private int nextPage = 0;
    /**
     * The number of pages that need to be printed
     */
    private int pagesToPrint;
    /**
     * Stores the total number of pages printed so far
     */
    private int totalPagesPrinted;
    /**
     * Capacity of the printer
     */
    private int capacity;
    /**
     * The current number of sheets in the tray
     */
    private int sheetsInTray;

    /**
     * Constructs a new printer with the given maximum tray capacity of the number
     * of paper sheets it can hold. Initially the tray is empty and the printer
     * has not printed any pages.
     * @param trayCapacity
     */


    public Printer(int trayCapacity){
        capacity = trayCapacity;

    }

    /**
     * Starts a new job to make copies of a document that is a specified page length
     * (documentPages). updates the next page to print as 0 (detonates the first page of
     *  the document).
     * @param documentPages
     */
    public void startPrintJob(int documentPages){
        pagesToPrint = documentPages;
        nextPage = 0;


    }

    /**
     * Return the number of sheets available for printing.
     * @return
     */
    public int getSheetsAvailable(){
        return sheetsAvailable;

    }

    /**
     * Returns the next page number of the document that will be printed
     * @return
     */
    public int getNextPage(){
        return nextPage;

    }

    /**
     * Returns the count of all pages printed by the printer since its construction.
     * @return
     */
    public int getTotalPages(){
        return totalPagesPrinted;


    }

    /**
     * Stimulates the printer printing a page. Increments the total page count of the
     * printer by number of pages printed. The number of pages available to the printer are also updated
     * accordingly.
     */
    public void printPage(){

        int pageToPrint = Math.min(1, (sheetsAvailable));
        totalPagesPrinted = totalPagesPrinted + pageToPrint;
        sheetsAvailable = sheetsAvailable - pageToPrint;

        nextPage = nextPage + pageToPrint;
        sheetsInTray =  sheetsInTray - pageToPrint;


        nextPage = nextPage % pagesToPrint;

        

    }

    /**
     * Removes the paper tray from the printer; that is makes the sheets avilable to
     * printer zero.
     */

    public void removeTray(){
        sheetsAvailable = 0;

    }

    /**
     * Replaces the tray in the printer.
     */

    public void replaceTray(){
        sheetsAvailable = sheetsInTray;

    }


    /**
     * Stimulates adding the tray, and replacing it.
     * @param sheets
     */

    public void addPaper(int sheets){
        removeTray();
        sheetsAvailable = Math.min(capacity, (sheetsAvailable + sheets));
        sheetsInTray = Math.min(capacity, (sheets + sheetsInTray));
        replaceTray();

    }


    /**
     * Stimulates removing the tray.
     * @param sheets
     */
    public void removePaper(int sheets){
        removeTray();
        sheetsAvailable = Math.max(0,(sheetsAvailable-sheets));
        sheetsInTray = Math.max(0,(sheetsInTray - sheets));
        replaceTray();


    }

}
