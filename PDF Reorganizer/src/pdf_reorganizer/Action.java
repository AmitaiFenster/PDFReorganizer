package pdf_reorganizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfConcatenate;
import com.itextpdf.text.pdf.PdfReader;

/**
 * 
 * @author amita_000 super class for actions on the PDF files.
 *
 */
public abstract class Action {

	protected String firstFileSource, secondFileSource, fileDestination;
	private PdfConcatenate d;

	/**
	 * 
	 * @param firstFileSource
	 *            Source file
	 * @param secondFileSource
	 *            Source file
	 * @param fileDestination
	 *            Destination to save the new file
	 */
	public Action(String firstFileSource, String secondFileSource,
			String fileDestination) {
		this.firstFileSource = firstFileSource;
		this.secondFileSource = secondFileSource;
		this.fileDestination = fileDestination;
	}

	/**
	 * sets the first file source
	 * 
	 * @param firstFileSource
	 *            file source String
	 */
	public void setFirstFileSource(String firstFileSource) {
		this.firstFileSource = firstFileSource;
	}

	/**
	 * sets the second file source
	 * 
	 * @param secondFileSource
	 *            file source String
	 */
	public void setSecondFileSource(String secondFileSource) {
		this.secondFileSource = secondFileSource;
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
	 * @return firstFileSource
	 */
	public String getFirstFileSource() {
		return firstFileSource;
	}

	/**
	 * @return secondFileSource
	 */
	public String getSecondFileSource() {
		return secondFileSource;
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
	 *             Exception number: 004. if the String of delete pages (deletePages)
	 *             includes a character that is not a number or space or comma
	 *             (",").
	 */
	public abstract String orderString(int pgAmount, String deletePages)
			throws Exception;

	/**
	 * combines two PDF files into one file
	 * 
	 * @param destination
	 *            String with the destination to save the final combined file.
	 * @throws Exception
	 *             Exception number: 001, 002, 003.
	 */
	public void combine(String destination) throws Exception {
		try {
			String[] files = { this.firstFileSource, this.secondFileSource };

			doFilesExist(files);

			new File(destination).createNewFile();
			this.d = new PdfConcatenate(new FileOutputStream(destination));
			for (int i = 0; i < files.length; i++)
				this.d.addPages(new PdfReader(files[i]));
			this.d.close();
		} catch (FileNotFoundException e) {
			if (this.d != null)
				this.d.close();
			e.printStackTrace();
			throw new Exception("001:\n", e);
		} catch (DocumentException e) {
			if (this.d != null)
				this.d.close();
			e.printStackTrace();
			throw new Exception("002:\n", e);
		} catch (IOException e) {
			if (this.d != null)
				this.d.close();
			e.printStackTrace();
			throw new Exception("003:\n", e);
		}
	}

	/**
	 * If a file from the array of files paths (files) does not exist, a
	 * IOException will be thrown.
	 * 
	 * @param files
	 *            array of file paths.
	 * @throws IOException
	 *             java.io.IOException: \file\ not found as file or resource.
	 */
	private void doFilesExist(String[] files) throws IOException {
		for (int i = 0; i < files.length; i++)
			if (!new File(files[i]).exists())
				throw new IOException("java.io.IOException: " + files[i]
						+ " not found as file or resource.");
	}

	/**
	 * delete the pages that are in the String deletePages.
	 * 
	 * @param deletePages
	 *            pages to delete
	 * @return pgOrder: Integer array with the order of the page not including
	 *         the pages that were deleted.
	 * @throws Exception
	 *             Exception number: 004. if the String of delete pages (deletePages)
	 *             includes a character that is not a number or space or comma
	 *             (",").
	 */
	public int[] deletePages(String deletePages, int[] pgOrder)
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
