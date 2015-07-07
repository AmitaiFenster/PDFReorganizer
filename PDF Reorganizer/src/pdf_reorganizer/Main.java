package pdf_reorganizer;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

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
	private Text TextfirstFileSource;
	private Text textSecondFileSource;
	private Text deletePagesText;
	private Button reorganizeButton;
	private Button combineButton;
	FileChooser fc;
	String firstFileSource, secondFileSource, fileDestination;

	int pgAmount;
	int[] erasePages;

	private Action pdfAction;

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
		shell.open();
		shell.layout();
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
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
		// TextfirstFileSource.addKeyListener(listener);
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
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(Main.class,
				"/pdf_reorganizer/pdf-512.ico"));
		shell.setSize(1110, 683);
		// TODO Main PDF fix minimum size, fix size to be good for all screen
		// resolutions.
		shell.setMinimumSize(1110, 659);
		shell.setText("PDF Reorganizer");
		shell.setLayout(new GridLayout(5, false));

		Label lblfirstFileSource = new Label(shell, SWT.NONE);
		lblfirstFileSource.setText("First file source");
		new Label(shell, SWT.NONE);

		TextfirstFileSource = new Text(shell, SWT.BORDER);
		TextfirstFileSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button btnChoose1 = new Button(shell, SWT.NONE);
		btnChoose1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				firstFileSource = fc.fileChooseOpen();
				TextfirstFileSource.setText(firstFileSource);
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

		Button btnChoose2 = new Button(shell, SWT.NONE);
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
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setText("   ");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

		Label deleteExample = new Label(shell, SWT.NONE);
		deleteExample.setText("Pages Example: 1,3,5\nleave blank if necessary");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
				
						reorganizeButton = new Button(shell, SWT.NONE);
						reorganizeButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
								false, false, 1, 1));
						reorganizeButton.setFont(SWTResourceManager.getFont("Segoe UI", 12,
								SWT.BOLD));
						reorganizeButton.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e) {
								execute(REORGANIZE);
							}
						});
						reorganizeButton.setBounds(361, 441, 361, 65);
						reorganizeButton.setText("Reorganize");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
				combineButton = new Button(shell, SWT.NONE);
				combineButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
						false, 1, 1));
				combineButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						execute(COMBINE);
					}
				});
				combineButton.setText("Combine");
				combineButton.setFont(SWTResourceManager.getFont("Segoe UI", 12,
						SWT.BOLD));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

	}

	// execute

	/**
	 * reorganizing the two PDF documents that were inserted in firstFileSource
	 * and secondFileSource, and saves the reorganized document to
	 * fileDestination. Opens MessageDialog with an error message if the
	 * information that was entered is not valid.
	 */
	public void execute(final short action) {
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
		} catch (Exception ex) {
			MessageDialog.openError(shell, "Error",
					"Please enter valid information!\n" + ex);
			ex.printStackTrace();
		}
	}
}