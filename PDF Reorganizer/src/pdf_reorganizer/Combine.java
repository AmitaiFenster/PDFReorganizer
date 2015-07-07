package pdf_reorganizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Combine extends Action {

	public Combine(String firstFileSource, String secondFileSource,
			String fileDestination) {
		super(firstFileSource, secondFileSource, fileDestination);
	}

	@Override
	public void execute(String deletePages) throws IOException {
		if (deletePages.equals("")){
			MessageDialog.openInformation(null, "fileDestination", fileDestination);
			combine(fileDestination);
		}
		else {
			combine(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
					+ "combinedTemp.pdf");

			PdfReader reader = new PdfReader(System.getProperty("user.home")
					+ "\\" + "Desktop" + "\\" + "combinedTemp.pdf");
			try {
				reader.selectPages(orderString(reader.getNumberOfPages(),
						deletePages));
				PdfStamper stamper = new PdfStamper(reader,
						new FileOutputStream(this.fileDestination));
				stamper.close();
			} catch (Exception ex) {
				MessageDialog.openError(null, "Error", "101\n" + ex);
			}
			reader.close();
			new File(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
					+ "combinedTemp.pdf").delete();
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
			pgOrder = deletePages(deletePages, pgOrder);

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
