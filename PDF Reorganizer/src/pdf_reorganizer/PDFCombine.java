package pdf_reorganizer;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * combining PDF files to one PDF file.
 * 
 * @author Amitai Fensterheim TOAO
 *
 */
public class PDFCombine extends PDFAction {

	PdfReader reader;
	PdfStamper stamper;

	/**
	 * Creating a new PDFCombine instance. this is used to combine PDF files to
	 * one PDF file.
	 * 
	 * @param firstFileLocation
	 * @param secondFileLocation
	 * @param fileDestination
	 * @throws Exception
	 */
	public PDFCombine(String firstFileLocation, String secondFileLocation,
			String fileDestination) throws Exception {
		super(firstFileLocation, secondFileLocation, fileDestination);
	}

	@Override
	public void execute(String deletePages) throws Exception {

		String[] filesLocations = new String[] { this.firstFileLocation,
				this.secondFileLocation };

		try {
			if (deletePages.equals("")) {
				combine(fileDestination, filesLocations);
			} else {
				combine(System.getProperty("user.home") + "\\" + "Desktop"
						+ "\\" + "combinedTemp.pdf", filesLocations);

				this.reader = new PdfReader(System.getProperty("user.home")
						+ "\\" + "Desktop" + "\\" + "combinedTemp.pdf");
				this.reader.selectPages(orderString(
						this.reader.getNumberOfPages(), deletePages));
				this.stamper = new PdfStamper(this.reader,
						new FileOutputStream(this.fileDestination));
				this.stamper.close();
				this.reader.close();
				new File(System.getProperty("user.home") + "\\" + "Desktop"
						+ "\\" + "combinedTemp.pdf").delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (this.stamper != null)
				this.stamper.close();
			if (this.reader != null)
				this.reader.close();
			throw e;
		}

	}

	@Override
	public String orderString(int pgAmount, String deletePages)
			throws Exception {
		int[] pgOrder = new int[pgAmount];

		for (int i = 1; i <= pgAmount; i++) {
			pgOrder[i] = i;
		}

		if (!deletePages.equals(""))
			pgOrder = removeDeletePages(deletePages, pgOrder);

		String pgOrderString = "";
		for (int i = 0; i < pgOrder.length; i++) {
			pgOrderString += pgOrder[i] + ", ";
		}
		if (pgOrderString.endsWith(", "))
			pgOrderString = pgOrderString.substring(0,
					pgOrderString.length() - 2);

		return pgOrderString;
	}

}
