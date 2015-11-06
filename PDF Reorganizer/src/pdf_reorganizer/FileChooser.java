package pdf_reorganizer;

import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * FileChooser uses JFileChoose to choose the directory of the files to open or
 * save. Second option that wasn't used is FileDialog.
 * 
 * @author Amitai Fensterheim TOAO
 */
public class FileChooser {

	private final JFileChooser fc;
	Component com;

	public FileChooser(Component com) {
		this.fc = new JFileChooser(System.getProperty("user.home") + "\\"
				+ "Desktop");
		this.com = com;
	}

	/**
	 * Choose File to open
	 * 
	 * @param com
	 *            Component
	 * @return string with directory
	 */
	public String fileChooseOpen() {
		String filePath;
		FileFilter filter = new FileNameExtensionFilter("PDF File", "pdf");
		fc.setFileFilter(filter);
		int responce = fc.showOpenDialog(com);
		if (responce == JFileChooser.APPROVE_OPTION) {
			filePath = fc.getSelectedFile().toString();
		} else {
			filePath = "";
		}
		return filePath;

	}

	/**
	 * Choose File to save
	 * 
	 * @param com
	 *            Component
	 * @return string with directory
	 */
	public String fileChooseSave() {
		String filePath;
		FileFilter filter = new FileNameExtensionFilter("PDF File", "pdf");
		fc.setFileFilter(filter);
		int responce = fc.showSaveDialog(com);
		if (responce == JFileChooser.APPROVE_OPTION) {
			filePath = fc.getSelectedFile().toString();
			if (!filePath.endsWith(".pdf"))
				filePath += ".pdf";
		} else {
			filePath = "cancelled";
		}
		return filePath;

	}

}