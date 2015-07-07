package pdf_reorganizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfConcatenate;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 
 * @author amita_000 PDF class reorganizes the PDF files.
 */
public class PDF {

	private String firstFileSource, secondFileSource, fileDestination;

	/**
	 * 
	 * @param firstFileSource
	 *            Source file
	 * @param secondFileSource
	 *            Source file
	 * @param fileDestination
	 *            Destination to save the new file
	 */
	public PDF(String firstFileSource, String secondFileSource,
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
	 * reorganizes the PDF files firstFileSource and secondFileSource, and saves
	 * the final file to fileDestination
	 * 
	 * @param deletePages
	 *            String of pages to delete (example: "1, 2, 3"). leave blank if
	 *            there are no pages to delete.
	 * @throws Exception
	 *             if the String of delete pages (deletePages) includes a
	 *             character that is not a number or space or comma (",").
	 */
	public void reorganize(String deletePages) throws Exception {
		combine();
		PdfReader reader = new PdfReader(System.getProperty("user.home")
				+ "\\" + "Desktop" + "\\" + "combinedTemp.pdf");
		try {
			reader.selectPages(orderString(reader.getNumberOfPages(),
					deletePages));
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					this.fileDestination));
			stamper.close();
		} catch (Exception ex) {
			MessageDialog.openError(null, "Error", "101\n" + ex);
		}
		reader.close();
		new File(System.getProperty("user.home") + "\\"
				+ "Desktop" + "\\" + "combinedTemp.pdf").delete();
	}

	/**
	 * combines two PDF files
	 * 
	 * @param firstFileSource
	 *            first file
	 * @param secondFileSource
	 *            second file
	 */
	public void combine() {
		try {
			new File(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
					+ "combinedTemp.pdf").createNewFile();
			PdfConcatenate d = new PdfConcatenate(new FileOutputStream(
					System.getProperty("user.home") + "\\" + "Desktop" + "\\"
							+ "combinedTemp.pdf"));
			String[] files = { this.firstFileSource, this.secondFileSource };
			for (int i = 0; i < files.length; i++)
				d.addPages(new PdfReader(files[i]));
			d.close();
		} catch (FileNotFoundException e) {
			MessageDialog.openError(null, "Error", "201\n" + e);
			e.printStackTrace();
		} catch (DocumentException e) {
			MessageDialog.openError(null, "Error", "202\n" + e);
			e.printStackTrace();
		} catch (IOException e) {
			MessageDialog.openError(null, "Error", "203\n" + e);
			e.printStackTrace();
		}
	}

	/**
	 * ordering in an String the order of the pages with pages to delete.
	 * 
	 * @param pgAmount
	 *            total amount of pages to order.
	 * @param deletePages
	 *            String with the pages to delete
	 * @return pgOrder: String with the order of the page. for example: "1,2,3"
	 * @throws Exception
	 *             if the String of delete pages (deletePages) includes a
	 *             character that is not a number or space or comma (",").
	 */
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

		if (deletePages != "")
			pgOrder = deletePages(deletePages, pgOrder);

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
	 * delete the pages that are in the String deletePages.
	 * 
	 * @param deletePages
	 *            pages to delete
	 * @return pgOrder: String with the order of the page not including the
	 *         pages that were deleted.
	 * @throws Exception
	 *             if the String of delete pages (deletePages) includes a
	 *             character that is not a number or space or comma (",").
	 */
	public int[] deletePages(String deletePages, int[] pgOrder)
			throws Exception {
		int[] deletePagesArr = pagesToInt(deletePages);
		for (int i = 0; i < deletePagesArr.length; i++)
			pgOrder[deletePagesArr[i] - 1] = 0;
		int[] finalPgOrder = new int[pgOrder.length - deletePagesArr.length];
		int j = 0;
		for (int i = 0; i < pgOrder.length; i++)
			if (pgOrder[i] != 0) {
				finalPgOrder[j] = pgOrder[i];
				j++;
			}
		return finalPgOrder;
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

}
