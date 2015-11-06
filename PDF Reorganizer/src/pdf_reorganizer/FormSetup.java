package pdf_reorganizer;

import java.awt.Toolkit;
import java.awt.Dimension;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Text;

/**
 * This class is a static class. Use for form setup (finding the proper
 * dimensions for the form, setting up drop targets for file paths and more).
 * 
 * @author Amitai Fensterheim TOAO
 *
 */
public class FormSetup {

	// Screen size in pixels (according to the screen resolution).
	public static final Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();

	/**
	 * Use when need to set the form size according to the screen size
	 * (resolution) and the wanted ratio.
	 * 
	 * @param Ratio
	 *            the ratio for the width of the form (form size relative to he
	 *            screen).
	 * @return Target size for the form in pixels.
	 */
	public static int getWidth(double ratio) {
		return (int) (screenSize.getWidth() * ratio);
	}

	/**
	 * Use when need to set the form size according to the screen size
	 * (resolution) and the wanted ratio.
	 * 
	 * @param Ratio
	 *            the ratio for the height of the form (form size relative to he
	 *            screen).
	 * @return Target size for the form in pixels.
	 */
	public static int getHeight(double ratio) {
		return (int) (screenSize.getHeight() * ratio);
	}

	/**
	 * setting up a drop target for the user to drop a file to a text field in
	 * order to determine a file path.
	 * 
	 * @param textFileSource
	 */
	public static void setDropTargets(Text textFileSource) {
		// Allow data to be copied or moved to the drop target
		int operations = DND.DROP_LINK | DND.DROP_MOVE | DND.DROP_COPY
				| DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(textFileSource, operations);

		// Receive data in Text or File format
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[] { fileTransfer };
		target.setTransfer(types);
		target.addDropListener(new DropListener(fileTransfer, textFileSource));
	}
}
