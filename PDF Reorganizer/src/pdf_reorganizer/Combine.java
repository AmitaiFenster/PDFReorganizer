package pdf_reorganizer;

import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Combine extends Action {

	PdfReader reader;
	PdfStamper stamper;

	public Combine(String firstFileSource, String secondFileSource,
			String fileDestination) {
		super(firstFileSource, secondFileSource, fileDestination);
	}

	@Override
	public void execute(String deletePages) throws Exception {
		try {
			if (deletePages.equals("")) {
				combine(fileDestination);
			} else {
				combine(System.getProperty("user.home") + "\\" + "Desktop"
						+ "\\" + "combinedTemp.pdf");

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
