package pdf_reorganizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Composite;

import com.itextpdf.awt.geom.Dimension;

/**
 * Main class for the user interface.
 * 
 * @author Amitai Fensterheim TOAO
 *
 */
public class Main {

	public static final short COMBINE = 1;
	public static final short REORGANIZE = 2;

	protected Shell shell;
	private Text textfirstFileSource, textSecondFileSource, deletePagesText;

	// Action buttons
	private Button reorganizeButton;
	private Button combineButton;

	// File choosing buttons
	private Button btnChoose1;
	private Button btnChoose2;

	FileChooser fc;
	String firstFileSource, secondFileSource, fileDestination;

	int pgAmount;
	int[] erasePages;

	private Action pdfAction;

	// Menu bar
	Menu menuBar, fileMenu, helpMenu;
	MenuItem fileMenuHeader, helpMenuHeader;
	MenuItem fileExitItem, fileCombineItem, helpAboutItem, fileReorganizeItem;

	SelectionAdapter combineSelectionAdapter, reorganizeSelectionAdapter;
	private Composite composite;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.setMenuBar(menuBar);
		new Label(shell, SWT.NONE);
		shell.open();
		shell.layout();
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		fc = new FileChooser(null);

		// KeyListener listener = new KeyListener() {
		// public void keyReleased(KeyEvent e) {
		// if (e.character == 13)
		// reorganizePage();
		// }
		//
		// public void keyPressed(KeyEvent e) {
		//
		// }
		// };
		// textfirstFileSource.addKeyListener(listener);
		// textSecondFileSource.addKeyListener(listener);
		// deletePagesText.addKeyListener(listener);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.MAX | SWT.MIN);
		shell.setImage(SWTResourceManager.getImage(Main.class,
				"/pdf_reorganizer/pdf-512.ico"));
		shell.setSize(FormSetup.getWidth(1200.0 / 3200.0),
				FormSetup.getHeight(584.0 / 1800.0));

		// TODO Main PDF fix minimum size, fix size to be good for all screen
		// resolutions.

		// shell.setMinimumSize(1110, 584);
		shell.setText("PDF Reorganizer");
		shell.setLayout(new GridLayout(5, false));

		combineSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				execute(COMBINE);
			}
		};

		reorganizeSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				execute(REORGANIZE);
			}
		};

		Label lblfirstFileSource = new Label(shell, SWT.NONE);
		lblfirstFileSource.setText("First file source");
		new Label(shell, SWT.NONE);

		textfirstFileSource = new Text(shell, SWT.BORDER);
		textfirstFileSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		btnChoose1 = new Button(shell, SWT.NONE);
		btnChoose1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				firstFileSource = fc.fileChooseOpen();
				textfirstFileSource.setText(firstFileSource);
			}
		});
		btnChoose1.setText("choose...");
		new Label(shell, SWT.NONE);

		Label lblSecondFileSource = new Label(shell, SWT.NONE);
		lblSecondFileSource.setText("Second file source");
		new Label(shell, SWT.NONE);

		textSecondFileSource = new Text(shell, SWT.BORDER);
		textSecondFileSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		btnChoose2 = new Button(shell, SWT.NONE);
		btnChoose2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				secondFileSource = fc.fileChooseOpen();
				textSecondFileSource.setText(secondFileSource);
			}
		});
		btnChoose2.setText("choose...");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Label deletePagesLable = new Label(shell, SWT.NONE);
		deletePagesLable.setText("Delete Pages");
		new Label(shell, SWT.NONE);

		deletePagesText = new Text(shell, SWT.BORDER);
		deletePagesText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("     ");

		Label deleteExample = new Label(shell, SWT.NONE);
		deleteExample.setText("Pages Example: 1,3,5\nleave blank if necessary");
		new Label(shell, SWT.NONE);

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setText("   ");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		composite = new Composite(shell, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));

		reorganizeButton = new Button(composite, SWT.NONE);
		reorganizeButton.setFont(SWTResourceManager.getFont("Segoe UI", 12,
				SWT.BOLD));
		reorganizeButton.addSelectionListener(reorganizeSelectionAdapter);
		reorganizeButton.setBounds(361, 441, 222, 64);
		reorganizeButton.setText("Reorganize");

		new Label(composite, SWT.NONE).setText("\t");

		combineButton = new Button(composite, SWT.NONE);
		combineButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				true, 1, 1));
		combineButton.setSize(180, 64);
		combineButton.addSelectionListener(combineSelectionAdapter);
		combineButton.setText("Combine");
		combineButton.setFont(SWTResourceManager.getFont("Segoe UI", 12,
				SWT.BOLD));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		setMenuBar();

		FormSetup.setDropTargets(this.textfirstFileSource,
				this.textSecondFileSource);
	}

	private void setMenuBar() {
		this.menuBar = new Menu(shell, SWT.BAR);

		this.fileMenuHeader = new MenuItem(this.menuBar, SWT.CASCADE);
		this.fileMenuHeader.setText("&File");
		this.fileMenu = new Menu(shell, SWT.DROP_DOWN);
		this.fileMenuHeader.setMenu(this.fileMenu);

		this.fileCombineItem = new MenuItem(this.fileMenu, SWT.PUSH);
		this.fileCombineItem.setText("&Combine");

		this.fileReorganizeItem = new MenuItem(this.fileMenu, SWT.PUSH);
		this.fileReorganizeItem.setText("&Reorganize");

		this.fileExitItem = new MenuItem(this.fileMenu, SWT.PUSH);
		this.fileExitItem.setText("E&xit");

		this.helpMenuHeader = new MenuItem(this.menuBar, SWT.CASCADE);
		this.helpMenuHeader.setText("&Help");
		this.helpMenu = new Menu(shell, SWT.DROP_DOWN);
		this.helpMenuHeader.setMenu(this.helpMenu);

		this.helpAboutItem = new MenuItem(this.helpMenu, SWT.PUSH);
		this.helpAboutItem.setText("&About PDF Reorganizer");

		this.fileExitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		this.fileReorganizeItem
				.addSelectionListener(this.reorganizeSelectionAdapter);
		this.fileCombineItem.addSelectionListener(this.combineSelectionAdapter);
		this.helpAboutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					aboutPopup();
				} catch (FileNotFoundException e1) {
					MessageDialog.openError(shell, "Error",
							"Cannot open 'About PDF Reorganizer' window");
					e1.printStackTrace();
				}
			}
		});

	}

	/**
	 * reorganizing the two PDF documents that were inserted in firstFileSource
	 * and secondFileSource, and saves the reorganized document to
	 * fileDestination. Opens MessageDialog with an error message if the
	 * information that was entered is not valid.
	 */
	private void execute(final short action) {
		firstFileSource = this.textfirstFileSource.getText();
		secondFileSource = this.textSecondFileSource.getText();
		fileDestination = fc.fileChooseSave();
		if (fileDestination.equals("cancelled"))
			return;
		String deletePagesString = deletePagesText.getText();
		try {
			if (action == REORGANIZE)
				pdfAction = new Reorganize(firstFileSource, secondFileSource,
						fileDestination);
			else if (action == COMBINE)
				pdfAction = new Combine(firstFileSource, secondFileSource,
						fileDestination);
			pdfAction.execute(deletePagesString);
			MessageDialog.openInformation(null, "PDF File Created",
					"PDF file created at " + fileDestination);
		} catch (Exception ex) {
			// Delete temporary file:
			new File(System.getProperty("user.home") + "\\" + "Desktop" + "\\"
					+ "combinedTemp.pdf").delete();
			String sMessage = ex.getMessage(), sCause = ex.getCause()
					.getMessage();
			MessageDialog.openError(shell, "Error",
					"Please enter valid information!\n\nError " + sMessage
							+ sCause);
			ex.printStackTrace();
		}
	}

	/**
	 * Opens a popup with information about the application PDFReorganizer. The
	 * text with the information is taken from 'About.txt' which is found in the
	 * project folder.
	 * 
	 * @throws FileNotFoundException
	 *             Exception number: 005. If the text file 'About.txt' wasn't
	 *             found in the project directory.
	 */
	private void aboutPopup() throws FileNotFoundException {
		MessageDialog.openInformation(shell, "About PDF Reorganizer",
				new Scanner(new File("About.txt")).useDelimiter("\\Z").next());
	}
}