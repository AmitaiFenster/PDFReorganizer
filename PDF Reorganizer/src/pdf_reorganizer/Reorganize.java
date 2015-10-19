package pdf_reorganizer;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Reorganize extends Action {

	PdfReader reader;
	PdfStamper stamper;

	public Reorganize(String firstFileSource, String secondFileSource,
			String fileDestination) {
		super(firstFileSource, secondFileSource, fileDestination);
	}

	@Override
	public void execute(String deletePages) throws Exception {
		reorganize(deletePages);
	}

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
	 * reorganizes the PDF files firstFileSource and secondFileSource, and saves
	 * the final file to fileDestination
	 * 
	 * @param deletePages
	 *            String of pages to delete (example: "1, 2, 3"). leave blank if
	 *            there are no pages to delete.
	 * @throws Exception
	 *             Exception number: 001, 002, 003, 004
	 */
	public void reorganize(String deletePages) throws Exception {
		try {
			// Combining the two PDF files into one temporary file.
			combine(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
					+ "combinedTemp.pdf");
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
