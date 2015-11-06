package pdf_reorganizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfConcatenate;
import com.itextpdf.text.pdf.PdfReader;

/**
 * super class for actions on the PDF files. examples: PDF combining, PDF
 * reorganizing.
 * 
 * @author Amitai Fensterheim TOAO
 *
 */
public abstract class PDFAction {

	protected String firstFileLocation, secondFileLocation, fileDestination;

	/**
	 * 
	 * @param firstFileLocation
	 *            Location file
	 * @param secondFileLocation
	 *            Location file
	 * @param fileDestination
	 *            Destination to save the new file
	 * @throws Exception
	 */
	public PDFAction(String firstFileLocation, String secondFileLocation,
			String fileDestination) throws Exception {
		if (!firstFileLocation.endsWith(".pdf")
				|| !secondFileLocation.endsWith(".pdf"))
			throw new Exception("005:\nPlease enter pdf files only!");
		this.firstFileLocation = firstFileLocation;
		this.secondFileLocation = secondFileLocation;
		this.fileDestination = fileDestination;
	}

	/**
	 * sets the first file location
	 * 
	 * @param firstFileLocation
	 *            file location String
	 */
	public void setFirstFileLocation(String firstFileLocation) {
		this.firstFileLocation = firstFileLocation;
	}

	/**
	 * sets the second file location
	 * 
	 * @param secondFileLocation
	 *            file location String
	 */
	public void setSecondFileLocation(String secondFileLocation) {
		this.secondFileLocation = secondFileLocation;
	}

	/**
	 * sets the file destination
	 * 
	 * @param fileDestination
	 *            file destination String
	 */
	public void setFileDestination(String fileDestination) {
		this.fileDestination = fileDestination;
	}

	/**
	 * @return firstFileLocation
	 */
	public String getFirstFileLocation() {
		return firstFileLocation;
	}

	/**
	 * @return secondFileLocation
	 */
	public String getSecondFileLocation() {
		return secondFileLocation;
	}

	/**
	 * @return fileDestination
	 */
	public String getFileDestination() {
		return fileDestination;
	}

	/**
	 * main execution method
	 * 
	 * @param deletePages
	 *            String with the pages to delete
	 */
	public abstract void execute(String deletePages) throws Exception;

	/**
	 * ordering in a String the order of the pages with pages to delete.
	 * 
	 * @param pgAmount
	 *            total amount of pages to order.
	 * @param deletePages
	 *            String with the pages to delete
	 * @return pgOrder: String with the order of the page. for example: "1,2,3"
	 * @throws Exception
	 *             Exception number: 004. if the String of delete pages
	 *             (deletePages) includes a character that is not a number or
	 *             space or comma (",").
	 */
	public abstract String orderString(int pgAmount, String deletePages)
			throws Exception;

	/**
	 * combines two PDF files into one file
	 * 
	 * @param destination
	 *            String with the destination to save the final combined file.
	 * @param filesLocations
	 *            array with the files
	 * @throws Exception
	 *             Exception number: 001, 002, 003.
	 */
	public static void combine(String destination, String[] filesLocations)
			throws Exception {

		PdfConcatenate d = new PdfConcatenate(new FileOutputStream(destination));

		try {
			doFilesExist(filesLocations);
			new File(destination).createNewFile();
			for (int i = 0; i < filesLocations.length; i++)
				d.addPages(new PdfReader(filesLocations[i]));
			d.close();
		} catch (FileNotFoundException e) {
			if (d != null)
				d.close();
			e.printStackTrace();
			throw new Exception("001:\n", e);
		} catch (DocumentException e) {
			if (d != null)
				d.close();
			e.printStackTrace();
			throw new Exception("002:\n", e);
		} catch (IOException e) {
			if (d != null)
				d.close();
			e.printStackTrace();
			throw new Exception("003:\n", e);
		}
	}

	/**
	 * If a file from the array of files locations (files) does not exist, a
	 * IOException will be thrown.
	 * 
	 * @param files
	 *            array of file locations.
	 * @throws IOException
	 *             java.io.IOException: \file\ not found as file or resource.
	 */
	public static void doFilesExist(String[] files) throws IOException {
		for (int i = 0; i < files.length; i++)
			if (!new File(files[i]).exists())
				throw new IOException("java.io.IOException: " + files[i]
						+ " not found as file or resource.");
	}

	/**
	 * removing from the integer array of page order the pages that need to be
	 * removed according to the String deletePages.
	 * 
	 * @param deletePages
	 *            pages to delete
	 * @return pgOrder: Integer array with the order of the page not including
	 *         the pages that were deleted.
	 * @throws Exception
	 *             Exception number: 004. if the String of delete pages
	 *             (deletePages) includes a character that is not a number or
	 *             space or comma (",").
	 */
	public int[] removeDeletePages(String deletePages, int[] pgOrder)
			throws Exception {
		try {
			int[] pages = duplicateIntArray(pgOrder);
			int[] deletePagesArr;
			deletePagesArr = pagesToInt(deletePages);
			for (int i = 0; i < deletePagesArr.length; i++)
				pages[deletePagesArr[i] - 1] = 0;
			int[] finalPgOrder = new int[pages.length - deletePagesArr.length];
			int j = 0;
			for (int i = 0; i < pages.length; i++)
				if (pages[i] != 0) {
					finalPgOrder[j] = pages[i];
					j++;
				}
			return finalPgOrder;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"004:\ndelete pages includes invalid characters\n", e);

		}

	}

	/**
	 * converts a String of pages numbers (for example: "1, 2, 3") to an integer
	 * array.
	 * 
	 * @param pagesString
	 *            string of pages to delete
	 * @return integer array with the pages to delete
	 * @throws Exception
	 *             if the String of delete pages (deletePages) includes a
	 *             character that is not a number or space or comma (",").
	 */
	public static int[] pagesToInt(String pagesString) throws Exception {
		pagesString = pagesString.replaceAll(" ", "");
		String[] st = pagesString.split(",");
		int[] pages = new int[st.length];
		for (int i = 0; i < st.length; i++)
			pages[i] = Integer.parseInt(st[i]);
		return pages;
	}

	/**
	 * duplicates a Integer Array.
	 * 
	 * @param original
	 *            Integer array
	 * @return duplicated Integer array
	 */
	public static int[] duplicateIntArray(int[] original) {
		int[] newArray = new int[original.length];
		for (int i = 0; i < newArray.length; i++)
			newArray[i] = original[i];
		return newArray;
	}
}
