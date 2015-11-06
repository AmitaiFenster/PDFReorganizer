package pdf_reorganizer;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Text;

/**
 * Setting up a drop listener for the user to drop a file to a text field in
 * order to determine a file path.
 * 
 * @author Amitai Fensterheim TOAO
 *
 */
public class DropListener implements DropTargetListener {

	FileTransfer fileTransfer;
	Text targetText;

	/**
	 * Creates a new drop listener instance. this Allows the user to drop a file
	 * in a text field in order to determine a file path.
	 * 
	 * @param fileTransfer
	 * @param targetText
	 */
	public DropListener(FileTransfer fileTransfer, Text targetText) {
		this.fileTransfer = fileTransfer;
		this.targetText = targetText;
	}

	public void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
		// will accept text but prefer to have files dropped
		for (int i = 0; i < event.dataTypes.length; i++) {
			if (fileTransfer.isSupportedType(event.dataTypes[i])) {
				event.currentDataType = event.dataTypes[i];
				// files should only be copied
				if (event.detail != DND.DROP_COPY) {
					event.detail = DND.DROP_NONE;
				}
				break;
			}
		}
	}

	public void dragLeave(DropTargetEvent arg0) {

	}

	public void dragOperationChanged(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
		// allow text to be moved but files should only be copied
		if (fileTransfer.isSupportedType(event.currentDataType)) {
			if (event.detail != DND.DROP_COPY) {
				event.detail = DND.DROP_NONE;
			}
		}
	}

	public void drop(DropTargetEvent event) {
		if (fileTransfer.isSupportedType(event.currentDataType)) {
			String[] files = (String[]) event.data;
			targetText.setText(files[0]);
		}
	}

	public void dropAccept(DropTargetEvent arg0) {

	}

	public void dragOver(DropTargetEvent arg0) {

	}

}
