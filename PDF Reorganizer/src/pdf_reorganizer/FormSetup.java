package pdf_reorganizer;

import java.awt.Toolkit;
import java.awt.Dimension;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Text;

public class FormSetup {

	public static final Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();

	public static int getWidth(double ratio) {
		return (int) (screenSize.getWidth() * ratio);
	}

	public static int getHeight(double ratio) {
		return (int) (screenSize.getHeight() * ratio);
	}

	public static void setDropTargets(Text textfirstFileSource,
			Text textSecondFileSource) {
		// Allow data to be copied or moved to the drop target
		int operations = DND.DROP_LINK | DND.DROP_MOVE | DND.DROP_COPY
				| DND.DROP_DEFAULT;
		DropTarget target1 = new DropTarget(textfirstFileSource, operations);
		DropTarget target2 = new DropTarget(textSecondFileSource, operations);

		// Receive data in Text or File format
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[] { fileTransfer };
		target1.setTransfer(types);
		target1.addDropListener(new DropListener(fileTransfer,
				textfirstFileSource));
		target2.setTransfer(types);
		target2.addDropListener(new DropListener(fileTransfer,
				textSecondFileSource));
	}
}
