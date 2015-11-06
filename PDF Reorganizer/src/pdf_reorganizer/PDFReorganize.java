package pdf_reorganizer;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Reorganizing two PDF files to one PDF file. After scanning all the odd pages
 * and then scanning all the even pages, use this class to combine and
 * reorganize the two PDF files that were created (odd and even) into one PDF
 * file.
 * 
 * @author Amitai Fensterheim TOAO
 *
 */
public class PDFReorganize extends PDFAction {

	PdfReader reader;
	PdfStamper stamper;

	/**
	 * Creating a new PDFReorganize instance. thins object is used for
	 * Reorganizing two PDF files to one PDF file. After scanning all the odd
	 * pages and then scanning all the even pages, use this class to combine and
	 * reorganize the two PDF files that were created (odd and even) into one
	 * PDF file.
	 * 
	 * @param firstFileLocation
	 * @param secondFileLocation
	 * @param fileDestination
	 * @throws Exception
	 */
	public PDFReorganize(String firstFileLocation, String secondFileLocation,
			String fileDestination) throws Exception {
		super(firstFileLocation, secondFileLocation, fileDestination);
	}

	@Override
	public void execute(String deletePages) throws Exception {
		reorganize(deletePages);
	}

	/**
	 * ordering in a String the order of the pages with pages to delete and in
	 * the right order for reorganizing the two PDF files (odd, even, odd...).
	 */
	@Override
	public String orderString(int pgAmount, String deletePages)
			throws Exception {
		int i, j = 0;
		int[] pgOrder = new int[pgAmount];

		for (i = 1; i <= Math.floor(pgAmount / 2); i++) {
			pgOrder[j] = i;
			j++;
			pgOrder[j] = (pgAmount - i + 1);
			j++;
		}

		if (pgAmount % 2 != 0)
			pgOrder[j] = i++;

		if (!deletePages.equals(""))
			pgOrder = removeDeletePages(deletePages, pgOrder);

		String pgOrderString = "";
		for (int k = 0; k < pgOrder.length; k++) {
			pgOrderString += pgOrder[k] + ", ";
		}
		if (pgOrderString.endsWith(", "))
			pgOrderString = pgOrderString.substring(0,
					pgOrderString.length() - 2);

		return pgOrderString;
	}

	/**
	 * reorganizes the PDF files firstFileLocation and secondFileLocation, and
	 * saves the final file to fileDestination
	 * 
	 * @param deletePages
	 *            String of pages to delete (example: "1, 2, 3"). leave blank if
	 *            there are no pages to delete.
	 * @throws Exception
	 *             Exception number: 001, 002, 003, 004
	 */
	public void reorganize(String deletePages) throws Exception {

		String[] filesLocations = new String[] { this.firstFileLocation,
				this.secondFileLocation };

		try {
			// Combining the two PDF files into one temporary file.
			combine(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
					+ "combinedTemp.pdf", filesLocations);
			this.reader = new PdfReader(System.getProperty("user.home") + "\\"
					+ "Desktop" + "\\" + "combinedTemp.pdf");
			this.reader.selectPages(orderString(this.reader.getNumberOfPages(),
					deletePages));
			this.stamper = new PdfStamper(this.reader, new FileOutputStream(
					this.fileDestination));
			this.stamper.close();
			this.reader.close();
			new File(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
					+ "combinedTemp.pdf").delete();
		} catch (Exception e) {
			e.printStackTrace();
			if (this.stamper != null)
				this.stamper.close();
			if (this.reader != null)
				this.reader.close();
			throw e;
		}

	}

}
