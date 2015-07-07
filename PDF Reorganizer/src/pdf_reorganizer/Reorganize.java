package pdf_reorganizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Reorganize extends Action {

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
	 *             if the String of delete pages (deletePages) includes a
	 *             character that is not a number or space or comma (",").
	 */
	public void reorganize(String deletePages) throws Exception {
		combine(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
				+ "combinedTemp.pdf");
		PdfReader reader = new PdfReader(System.getProperty("user.home") + "\\"
				+ "Desktop" + "\\" + "combinedTemp.pdf");
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
		new File(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
				+ "combinedTemp.pdf").delete();
	}

}
