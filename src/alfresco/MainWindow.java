/*
 * $Revision: 1.2 $
 * $Id: MainWindow.java,v 1.2 2003/04/07 12:27:48 niclas Exp $
 *
 * This file is part of Alfresco
 * Copyright (C) 1998  Niclas Jareborg
 * 
 * The Alfresco source code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA  02111-1307, USA. 
 *
 * For more information contact Niclas at nic@sanger.ac.uk
 */
package alfresco;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import alfresco.corba_wrappers.*;
/**
 * Alfresco main window.<p>
 * Implements Singelton design pattern, there can only be one 
 * MainWindow instance.
 * @see java.awt.Frame
 * @version 1
 * @author Niclas Jareborg, The Sanger Centre
 */
 
public class MainWindow extends Frame {
/**
 * Holds the only possible instance of MainWindow	
 */
	private static MainWindow instance = null;
/**
 * Window title
 */
	static String title = "Alfresco";
/**
 * Parent application
 */
	Wcomp wc;

/**
 * Menu bar
 */
	MenuBar mMenuBar;
/**
 * File menu
 */
	Menu fileMenu;
/**
 * Open menu item
 */
	MenuItem openMI;
/**
 * Save menu Item
 */
	MenuItem saveMI;
/**
 * Close menu item
 */
	MenuItem closeMI;
/**
 * Read cmp file menu item
 */
	MenuItem readcmpMI;
/**
 * Open Remote EntryPair (CORBA) menu item
 */
	MenuItem openRemoteMI;
/**
 * Quit menu item
 */
	MenuItem quitMI;
/**
 * Read gff file menu item
 */
	MenuItem readgffMI;
/**
 * Read cgff file menu item
 */
	MenuItem readCgffMI;
/**
 * Write gff file menu item
 */
	MenuItem writegffMI;
/**
 * Write cgff file menu item
 */
	MenuItem writecgffMI;
/**
 * Edit menu
 */	
	Menu editMenu;
/**
 * Edit feature menu item
 */	
	MenuItem editFeatureMI;
/**
 * Make Gene menu item
 */	
	MenuItem makeGeneMI;
/**
 * Edit Gene Attributes menu item
 */	
	MenuItem editGeneMI;
/**
 * Add feature to Gene menu item
 */	
	MenuItem addToGeneMI;
/**
 * Remove feature from Gene menu item
 */	
	MenuItem removeFromGeneMI;
/**
 * Confirm exons menu item
 */	  
  MenuItem confirmExonsMI;
/**
 * Unconfirm exons menu item
 */	  
  MenuItem unconfirmExonsMI;
/**
 *  Hide Unconfirm exons menu item
 */	
  CheckboxMenuItem hideUnconfirmExonsMI;
    /**
 * Make reciprocal menu item
 */	
	MenuItem makeReciprocalMI;
/**
 * Change tier menu item
 */	
	MenuItem changeTierMI;
/**
 * Remove feature menu item
 */	
	MenuItem removeFeatureMI;
/**
 * Function menu
 */
	Menu functionMenu;
/**
 * Region set menu item
 */
	MenuItem regsetMI;
/**
 * RepeatMasker menu item
 */
	MenuItem repeatMaskerMI;
/**
 * CpG menu item
 */
	MenuItem cpgMI;
/**
 * Genscan menu item	
 */
	MenuItem genscanMI;
/**
 * BlstAlign menu item	
 */  
  MenuItem blastnMI;
/**
 * blGenewise menu item	
 */    
  MenuItem blGenewiseMI;
/**
 * BlastWise menu item	
 */    
  MenuItem blastwiseMI;
/**
 *  est_genome menu item	
 */    
  MenuItem est_genomeMI;
/**
 * tssw menu item	
 */
	MenuItem tsswMI;
/**
 * Find reciprocals menu item	
 */
	MenuItem findRecMI;
/**
 * View menu
 */
	Menu viewMenu;
/**
 * View sequence menu item
 */
	MenuItem sequenceMI;
/**
 * View ATG menu item
 */
	CheckboxMenuItem atgMI;
/**
 * View dotter menu item
 */
	MenuItem dotterMI;
/**
 * View dba alignment menu item
 */	
	MenuItem dbalignMI;
// /**
//  * View newdba alignment menu item
//  */	
// 	MenuItem newdbalignMI;
// /**
//  * View dbb alignment menu item
//  */	
// 	MenuItem dbbalignMI;	
/**
 * View NW alignment menu item
 */	
	MenuItem NWalignMI;
/**
 * View SW alignment menu item
 */	
	MenuItem SWalignMI;
/**
 * Settings menu
 */
	Menu settingsMenu;
/**
 * dba alignment settings menu item	
 */
	MenuItem dbaSettingsMI;
// /**
//  * newdba alignment settings menu item	
//  */
// 	MenuItem newdbaSettingsMI;
// /**
//  * dbb alignment settings menu item	
//  */
// 	MenuItem dbbSettingsMI;
/**
 * Trx factor binding sites settings menu item	
 */
	MenuItem tfbsMI;
/**
 * Show introns menu item
 */
	CheckboxMenuItem showIntronsMI;

/**
 * Entry 1 EntryGadget	
 */
	transient EntryGadget entryGadget1;
/**
 * Entry 2 EntryGadget	
 */
	transient EntryGadget entryGadget2;
/**
 * Panel that holds canvas for drawing and individual scroll and 
 * zoombars
 */
	Panel seqPan;
/**
 * Drawing Canvas
 */
	Canvas seqCan;
/**
 * Pressed mouse x position
 */
	int mouseX;
/**
 * Pressed mouse y position
 */
	int	mouseY;
/**
 * Entry 1 scroll bar
 */
	ColleagueScrollbar scroll1;
/**
 * Entry 2 scroll bar
 */
	ColleagueScrollbar	scroll2;
/**
 * Entry 1 zoom bar
 */
	ColleagueScrollbar	zoom1;
/**
 * Entry 2 zoom bar
 */
	ColleagueScrollbar	zoom2;
/**
 * Scroll bar for synchronous scrolling
 */
	ColleagueScrollbar	synchSB;
/**
 * Scroll bar for synchronous zooming
 */
	ColleagueScrollbar	synchZSB;
/**
 * Label for program status
 */
	Label status;
/**
 * Label for selected Glyph
 */
	Label itemLabel;
/**
 * Label for Entry 1 zoomfactor 
 */
	ZoomLabel zoomLabel1; 
/**
 * Label for Entry 2 zoomfactor 
 */
	ZoomLabel zoomLabel2;
/**
 * Handles of scroll and zoombar relationships
 */
	ScrollZoomDirector szDirector;
	Label crap; // needed???
/**
 * Window layout
 */
	LayoutManager layout = new GridBagLayout();
/**
 * Width of main window	
 */
	int width = 800;
/**
 * Height of main window
 */
	int height = 550;
/**
 * Dimension of main window
 */
	Dimension dimension = new Dimension(width, height);
/**
 * Width of sequence Panel
 */
	int seqPanWidth = width - 30 ;
/**
 * X position of sequence Panel
 */
	int seqPanX = (width-18)/2-seqPanWidth/2;
/**
 * Y position of sequence Panel
 */
	int seqPanY = 200;
/**
 * Height of sequence Panel
 */
	int seqPanHeight = height-seqPanY-25;
/**
 * Bounds of sequence Panel
 */
	Rectangle seqPanRect = new Rectangle(seqPanX, seqPanY, seqPanWidth, seqPanHeight);
/**
 * Width of sequence Canvas
 */
	int seqCanvWidth =  seqPanWidth - 15 ;
/**
 * Height of sequence Canvas
 */
	int seqCanvHeight = seqPanHeight - 40;
/**
 * Print properties
 */ 
  Properties ppr;
/**
 * AdjustmentListener for scroll and zoom bars
 */
	AdjustmentListener scrollZoomListener = new AdjustmentListener() {
																							public void adjustmentValueChanged(AdjustmentEvent e) {
// 																								ColleagueScrollbar csb = (ColleagueScrollbar) e.getAdjustable();
// 																								csb.changed(e);
																								szDirector.scrollBarChanged(e);
																							}
																						};

/**
 * Creates new Mainwindow, but can not be called directly. Use instance() instead.
 * @param wcomp Parent application
 */
	private MainWindow(Wcomp wcomp){
		super(title);
		wc = wcomp;
// 		addWindowListener( new WindowAdapter() {
// 			public void windowActivated(WindowEvent e) {
// 				if (wc.entriesSet() ) wc.drawEntries();
// 			}
// 			public void windowDeactivated(WindowEvent e) {
// 				if (wc.entriesSet() ) wc.drawEntries();
// 			}
// 		});
// 		
// 		addFocusListener( new FocusListener() {
// 			public void focusGained(FocusEvent e) {
// 				if (wc.entriesSet() ) wc.drawEntries();
// 			}
// 			public void focusLost(FocusEvent e) {
// 				if (wc.entriesSet() ) wc.drawEntries();			
// 			}
// 		});
		setSize(dimension);	// set main window size
		
		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Dimension windim = MainWindow.this.getSize();
// 				MainWindow w = (MainWindow) e.getComponent();
// 				Dimension windim = w.getSize();
// 				System.out.println("Main Window resized: " + windim);
				seqPanWidth = windim.width - 29 ;
				seqPanHeight = windim.height-seqPanY-22;
				seqPan.setSize(seqPanWidth, seqPanHeight);
// 				System.out.println("SeqPan size: " + seqPan.getSize());
				seqCanvWidth =  seqPanWidth - 15 ;
				seqCanvHeight = seqPanHeight - 40;
				seqCan.setSize(seqCanvWidth, seqCanvHeight);
				wc.updateGraphics();
				seqPan.validate();
				synchSB.setBounds(seqPanX, seqPanY+seqPanHeight-1, seqPanWidth, 18);
				synchZSB.setBounds(seqPanX+seqPanWidth, seqPanY, 18, seqPanHeight);
				if (wc.entriesSet()) {
					wc.drawEntries();
				}
			}
		});
		// For catching keypresses NOT WORKING!
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				System.out.println(e);
			}
			public void keyPressed(KeyEvent e) {
				System.out.println(e);
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					System.out.println("up arrow key pressed");
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					System.out.println("down arrow key pressed");
				}
			}
		});

		entryGadget1 = new EntryGadget(MainWindow.this, "Select Sequence 1", new EntryUpdater() {
			public void update (Entry e) {
				wc.setEntry1(e);
			}
		});
		entryGadget1.setLocation(10,60);
//		entryGadget1.setBackground(Color.red);
		add(entryGadget1);
		
		entryGadget2 = new EntryGadget(MainWindow.this, "Select Sequence 2", new EntryUpdater() {
			public void update (Entry e) {
				wc.setEntry2(e);
			}
		});
		entryGadget2.setLocation(10,90);
		add(entryGadget2);
				
		// menu bar for main window
		mMenuBar = new MenuBar();
		setMenuBar(mMenuBar);
		
		if(wc.isApplication()) {
			initFullGUI();
		} else {
			initAppletGUI();
		}
		

		
		
		// Synchro scrollbar
		synchSB = new ColleagueScrollbar(szDirector,Scrollbar.HORIZONTAL,0,100,0,100);
		synchSB.addAdjustmentListener(scrollZoomListener);
		synchSB.setBounds(seqPanX, seqPanY+seqPanHeight, seqPanWidth, 18);
		synchSB.setBackground(Color.gray);
		add(synchSB);
		
		// Synchro zoombar
		synchZSB = new ColleagueScrollbar(szDirector,Scrollbar.VERTICAL);
		synchZSB.addAdjustmentListener(scrollZoomListener);
		synchZSB.setBounds(seqPanX+seqPanWidth, seqPanY, 18, seqPanHeight);
		synchZSB.setBackground(Color.gray);
		add(synchZSB);
		
		// seqPan for holding seqCan and scrollbars
		seqPan = new Panel(new BorderLayout());
		seqPan.setBounds(seqPanRect);
// 		seqPan.setBackground(Color.red);

  	//	seqCan for drawing sequence representations
		// anonymous class to be able to override paint()
		// which is called by the system when window needs updating
  	seqCan = new Canvas(){
			public void paint(Graphics g) {
				if (wc.entriesSet()) {
					wc.drawEntries(g);
				}
			}
		};
  	seqCan.setSize(seqCanvWidth, seqCanvHeight);
  	seqCan.setBackground(Color.white);
// 		seqCan.addFocusListener( new FocusListener() {
// 			public void focusGained(FocusEvent e) {
// 				if (wc.entriesSet() ) wc.drawEntries();
// 			}
// 			public void focusLost(FocusEvent e) {
// 				if (wc.entriesSet() ) wc.drawEntries();			
// 			}
// 		});
		setSize(dimension);	// set main window size
  	
  	seqCan.addMouseListener( new MouseAdapter() {
  		public void mouseClicked(MouseEvent e) {
//   				if (e.isShiftDown()) { System.out.println("Shift pressed"); }
//   			if (e.getModifiers() == InputEvent.BUTTON1_MASK || e.getModifiers() == Event.SHIFT_MASK) {
  			if (e.getModifiers() == InputEvent.BUTTON1_MASK || e.getModifiers() == InputEvent.BUTTON2_MASK || e.isShiftDown()) {
//   			if (true) {
//   				if (e.getModifiers() == Event.SHIFT_MASK) { System.out.println("Shift pressed"); }
					if(wc.isApplication())
  					wc.pw.addB.setEnabled(false);

  				//System.out.println("Mouse clicked at " + e.getX() + ", " + e.getY());
  				Glyph newSel = wc.entries.clicked(e.getX(), e.getY());

  				if (newSel == null) { 
  					System.out.println("Mouse clicked at " + e.getX() + ", " + e.getY());
  					itemLabel.setText("");
	  				wc.entries.resetSelector();
	  				if (wc.entries.numberOfSelectedGlyphs() > 0) {
	  					wc.entries.removeSelectedGlyphs();
	  					wc.entries.currentRegionSet = null;
							if(wc.isApplication())
  	  					wc.pw.removeB.setEnabled(false);
	  				}
//   					if (wc.entries.selectedGlyph != null) {
//   						wc.entries.selectedGlyph.select(false);
//   						wc.entries.currentRegionSet = null;
//   						wc.pw.removeB.setEnabled(false);
//   						wc.entries.selectedGlyph = null;
//   					}
  					wc.drawEntries();
  					return; 
  				}
  				if (wc.entries.numberOfSelectedGlyphs() > 0 && !(e.getModifiers() == InputEvent.BUTTON2_MASK || e.isShiftDown()) ) {
						wc.entries.removeSelectedGlyphs();
//   					wc.entries.selectedGlyph.select(false);
  					itemLabel.setText("");
  				}
  				wc.entries.selectGlyph(newSel);
//   				wc.entries.selectedGlyph = newSel;
  				wc.entries.currentRegionSet = null;
					if(wc.isApplication())
    				wc.pw.removeB.setEnabled(false);
  				if (newSel instanceof RegionSet) { 
  					wc.pw.removeB.setEnabled(true); 
  				}
  				if (newSel instanceof DotterRegion) {
  					DotterRegion dr = (DotterRegion) newSel;
//   					System.out.println(dr + " clicked");
  					RegionSet rs = null;
//   					try {
  					Properties dbaprop = 	(Properties) wc.getResources().get("Dba");
            rs = new RegionSet(wc.entries, dr, dbaprop);
//   					} catch (WarningMessageException wme) { 
//   						status.setText(wme.getMessage()); 
//   					}
						if (rs.isWarning()) { status.setText(rs.getWarning()); }
  					rs.promote();
  					wc.entries.currentRegionSet = rs;
//   					System.out.println("Current reg set: " + wc.entries.currentRegionSet);
  					wc.pw.addB.setEnabled(true);
  				}
//   				wc.entries.selectedGlyph.select(true);
  				itemLabel.setText(newSel.toString());
  				if (!(e.getModifiers() == InputEvent.BUTTON2_MASK || e.isShiftDown())) {
   					wc.entries.resetSelector();
   				}
					wc.drawEntries();
  			}
  		}
  		
  		public void mousePressed(MouseEvent e) {
//   			if(e.getModifiers() == InputEvent.BUTTON2_MASK) {
//   				zoomIn(wc.entries.entry1);
// 					updateScroll(wc.entries.entry1, scroll1);
// 					zoomLabel1.setValue(wc.entries.entry1.coord.zoom);
//   				zoomIn(wc.entries.entry2);
//   				updateScroll(wc.entries.entry2, scroll2);
//   				zoomLabel2.setValue(wc.entries.entry2.coord.zoom);
//   				wc.drawEntries();
//   			} else if(e.getModifiers() == InputEvent.BUTTON3_MASK) {
//   				zoomOut(wc.entries.entry1);
//   				updateScroll(wc.entries.entry1, scroll1);
//   				zoomLabel1.setValue(wc.entries.entry1.coord.zoom);
//   				zoomOut(wc.entries.entry2);
//   				updateScroll(wc.entries.entry2, scroll2);
//   				zoomLabel2.setValue(wc.entries.entry2.coord.zoom);
//   				wc.drawEntries();
//   			} 
				mouseX = e.getX();
				mouseY = e.getY();
				status.setText("OK");
// 				System.out.println("Mouse pressed, x:" + mouseX + ", y:" + mouseY);
  		}
  	});
  	seqCan.addMouseMotionListener(new MouseMotionAdapter() {
  		public void mouseDragged(MouseEvent e) {
  			if (e.getModifiers() == InputEvent.BUTTON1_MASK || e.getModifiers() == InputEvent.BUTTON2_MASK || e.isShiftDown()) {
  			
	//   			System.out.println("Mouse dragged, x: " + e.getX() + ", y: " + e.getY());
  				if (wc.entries.numberOfSelectedGlyphs() > 0 && !(e.getModifiers() == InputEvent.BUTTON2_MASK || e.isShiftDown())) {
  					wc.entries.removeSelectedGlyphs();
	//   				wc.entries.selectedGlyph.select(false);
  					itemLabel.setText("");
  				}
  				wc.entries.setSelector(mouseX, mouseY, e.getX() - mouseX, e.getY()-mouseY, e.getModifiers() == InputEvent.BUTTON1_MASK);
//   				wc.entries.selectGlyph(wc.entries.selector);
  				itemLabel.setText(wc.entries.selectors.toString());
  				wc.drawEntries();
	//   			Graphics g = seqCan.getGraphics();
	//   			g.drawRect(mouseX, mouseY, e.getX() - mouseX, e.getY()-mouseY);
	//   			g.dispose();
				}
  		}
  	});
//   	add(seqCan);
		seqPan.add(seqCan, "Center");
		
		scroll1 = new ColleagueScrollbar(szDirector, Scrollbar.HORIZONTAL,0,100,0,100);
		scroll1.addAdjustmentListener(scrollZoomListener);
		seqPan.add(scroll1, "North");
		scroll2 = new ColleagueScrollbar(szDirector, Scrollbar.HORIZONTAL,0,100,0,100);
		scroll2.addAdjustmentListener(scrollZoomListener);
		seqPan.add(scroll2, "South");

		Panel zoomPan = new Panel(new GridLayout(2,1));
		zoom1 = new ColleagueScrollbar(szDirector, Scrollbar.VERTICAL);
		zoom1.addAdjustmentListener(scrollZoomListener);
		zoomPan.add(zoom1);
		zoom2 = new ColleagueScrollbar(szDirector, Scrollbar.VERTICAL);
		zoom2.addAdjustmentListener(scrollZoomListener);
		zoomPan.add(zoom2);
		
		seqPan.add(zoomPan, "East");
		add(seqPan);
				
				//	status label
		status = new Label("OK");
		status.setBounds(10, 115, width-20, 30);
		add(status);
		
		
		zoomLabel1 = new ZoomLabel("Zoom 1:");
		zoomLabel1.setBounds(10, 135, 290, 30);
		add(zoomLabel1);
		zoomLabel2 = new ZoomLabel("Zoom 2:");
		zoomLabel2.setBounds(10, 155, 290, 30);
		add(zoomLabel2);

		itemLabel = new Label("");
		itemLabel.setBounds(10, 175, 800, 30);
		add(itemLabel);

		szDirector = new ScrollZoomDirector(this);

		// crap label to handle bug? in layout manager. Without it the last component won't be shown
		crap = new Label("");
		crap.setBounds(0, 0, 0, 0);
		add(crap);

// 		show();
//   	System.out.println("seqPan size: " + seqPan.getSize().width + ", " + seqPan.getSize().height);
//   	System.out.println("seqCan size: " + seqCan.getSize().width + ", " + seqCan.getSize().height);

// 		System.out.println("zoomLabel1: " + zoomLabel1.getLocation() + " " + zoomLabel1.getSize());
		
	}
	
	/**
	 * Inits GUI for application
	 */
	public void initFullGUI() {
		// print properties
    ppr = new Properties();
		// File menu
		fileMenu = new Menu("File");
			// menu items
		openMI = new MenuItem("Open...", new MenuShortcut(KeyEvent.VK_O));
		openMI.addActionListener(new ActionListener() {		// register event listener
			public void actionPerformed(ActionEvent e) { 
				if (wc.entriesSet()) {
					OkNoCancelDialog askD = new OkNoCancelDialog(instance, "Save current Entry pair?");
					askD.show();
					int answer = askD.getAnswer();
					if (answer == OkNoCancelDialog.CANCEL) {
						return;
					} else if (answer == OkNoCancelDialog.OK) {
						if (!wc.entries.interactiveSave()) { return ;}
					}
				}
				FileDialog f = new FileDialog(instance, "Open Entry Pair", FileDialog.LOAD);
				f.show();
				String fname = f.getFile();
        String dname = f.getDirectory();
				if (fname != null) {
          File infile = new File(dname, fname);
					try {
						// Should ask if saving is needed
// 						wc.entries = EntryPair.load(fname, wc);
						wc.entries = EntryPair.load(infile, wc);
						entryGadget1.update(wc.entries.entry1);
						entryGadget2.update(wc.entries.entry2);
						wc.entries.zoom = -1;
						wc.drawEntries();
					} catch (IOException ioe) { System.out.println(ioe); }
					catch (ClassNotFoundException cnfe) { System.out.println(cnfe); }
				}
			}
		});
		fileMenu.add(openMI);
		
		saveMI = new MenuItem("Save...", new MenuShortcut(KeyEvent.VK_S));
		saveMI.addActionListener(new ActionListener() {		// register event listener
			public void actionPerformed(ActionEvent e) {
// 				FileDialog f = new FileDialog(instance, "Save Entry Pair", FileDialog.SAVE);
// 				f.show();
// 				String fname = f.getFile();
// 				if (fname != null) {
// 					try {
// 						wc.entries.save(fname);
// 					} catch (IOException ioe) { System.out.println(ioe); }
// 				}
				wc.entries.interactiveSave();
			}
		});
		fileMenu.add(saveMI);
		
		MenuItem saveAsMI = new MenuItem("Save As...");
		saveAsMI.addActionListener(new ActionListener() {		// register event listener
			public void actionPerformed(ActionEvent e) {
// 				FileDialog f = new FileDialog(instance, "Save Entry Pair", FileDialog.SAVE);
// 				f.show();
// 				String fname = f.getFile();
// 				if (fname != null) {
// 					try {
// 						wc.entries.save(fname);
// 					} catch (IOException ioe) { System.out.println(ioe); }
// 				}
				wc.entries.interactiveSaveAs();
			}
		});
		fileMenu.add(saveAsMI);
		
		closeMI = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		closeMI.addActionListener(new ActionListener() {		// register event listener
			public void actionPerformed(ActionEvent e) {
				// Should ask if saving is needed
				OkNoCancelDialog askD = new OkNoCancelDialog(instance, "Save Entry pair before closing?");
				askD.show();
				int answer = askD.getAnswer();
				if (answer == OkNoCancelDialog.CANCEL) {
					return;
				} else if (answer == OkNoCancelDialog.OK) {
					// bring up save dialog and save
					if (!wc.entries.interactiveSave()) { return ;}
				}
				entryGadget1.reset();
				entryGadget2.reset();
				zoomLabel1.reset();
				zoomLabel2.reset();
				wc.resetEntries();
				wc.drawEntries();
			}
		});
		fileMenu.add(closeMI);
		
		
		fileMenu.addSeparator();
		
		readcmpMI = new MenuItem("Read .cmp file");		// read .cmp file menu item
		readcmpMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(MainWindow.this, "Open .cmp File", FileDialog.LOAD);
				fd.setFilenameFilter(new FilenameFilter(){		// filter *.cmp. Doesn't work!
					public boolean accept(File dir, String fn) {
						System.out.println("accept() called with: " + dir + ", " + fn);
						return fn.endsWith(".cmp");
					}
				});
				fd.show();
				String filename = fd.getFile();		// get file name from file dialog
				String path = fd.getDirectory();
				if ( filename == null ) { return; }
				CmpFile cmpf = new CmpFile(path,filename);
				if (!cmpf.exists() ) {
					status.setText(filename+": no such file");
					return;
				}
				status.setText("Reading .cmp file...");	// set status label to "Reading..."
				try {
					cmpf.open();
					cmpf.parse(wc.entries);
				} catch (FileNotFoundException fnf) {
					status.setText(filename+": no such file");	// Hmm, not sure this catching only catches filename problems
					return;
				}
				wc.pw.cdsCheckboxP.setEnabled(true);		// enable exclude checkboxes in region set parameter window
				status.setText("OK");			// set status label to OK
				wc.drawEntries();
			}
		});
		fileMenu.add(readcmpMI);

		readgffMI = new MenuItem("Read .gff file");		// read .gff file menu item
		readgffMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(MainWindow.this, "Open .gff File", FileDialog.LOAD);
				fd.show();
				String filename = fd.getFile();		// get file name from file dialog
				String path = fd.getDirectory();
				if ( filename == null ) { return; }
				File gffF = new File(path, filename);
				if (!gffF.exists() ) {
					status.setText(gffF.getPath() + ": no such file");
					return;
				}
				status.setText("Reading .gff file...");	// set status label to "Reading..."
				try {
					GffParser gp = new GffParser(wc.entries, gffF);
				} catch (FormatException fex) { 
					ErrorDialog ed = new ErrorDialog(MainWindow.this, fex.getMessage());
// 					ErrorDialog ed = new ErrorDialog(MainWindow.this, ex.toString());
					ed.show();
// 					System.out.println(ex);
				} catch (IOException ioe) { System.out.println(ioe); }
// 				  catch (FileNotFoundException fnfe) { System.out.println(fnfe); }
			}
		});
// 		readgffMI.setEnabled(false);
		fileMenu.add(readgffMI);
		
		readCgffMI = new MenuItem("Read .cgff file");		// read .mgff file menu item
		readCgffMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(MainWindow.this, "Open .cgff File", FileDialog.LOAD);
				fd.show();
				String filename = fd.getFile();		// get file name from file dialog
				String path = fd.getDirectory();
				if ( filename == null ) { return; }
				File cgffF = new File(path, filename);
				if (!cgffF.exists() ) {
					status.setText(cgffF.getPath() + ": no such file");
					return;
				}
				status.setText("Reading .cgff file...");	// set status label to "Reading..."
				try {
					CgffParser gp = new CgffParser(wc.entries, cgffF);
				} catch (FormatException fex) { 
					ErrorDialog ed = new ErrorDialog(MainWindow.this, fex.getMessage());
// 					ErrorDialog ed = new ErrorDialog(MainWindow.this, ex.toString());
					ed.show();
// 					System.out.println(ex);
				} catch (IOException ioe) { System.out.println(ioe); }
// 				  catch (FileNotFoundException fnfe) { System.out.println(fnfe); }
			}
				
		});
// 		readCgffMI.setEnabled(false);
		fileMenu.add(readCgffMI);
		
		fileMenu.addSeparator();

		writegffMI = new MenuItem("Write .gff file...");	// write  .gff file menu item
		writegffMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				FileDialog f = new FileDialog(instance, "Write gff file", FileDialog.SAVE);
				f.show();
				String fname = f.getFile();
				String path = f.getDirectory();
				if (fname != null) {
					GffFile gf = new GffFile(path, fname, wc.entries);
					status.setText("Writing " + fname + "...");
					gf.write();
					status.setText("OK");
				}
			}
		});
		fileMenu.add(writegffMI);

		writecgffMI = new MenuItem("Write .cgff file...");	// write  .cgff file menu item
		writecgffMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				FileDialog f = new FileDialog(instance, "Write cgff and gff files. (.cgff and .gff will be added to file name)", FileDialog.SAVE);
				f.show();
				String fname = f.getFile();
				String path = f.getDirectory();
				if (fname != null) {
					String gffname = fname + ".gff";
					String cgffname = fname + ".cgff";
					GffFile gf = new GffFile(path, gffname, wc.entries);
					CgffFile cgf = new CgffFile(path, cgffname, gffname, wc.entries);
					status.setText("Writing " + gffname + "...");
					gf.write();
					status.setText("Writing " + cgffname + "...");
					cgf.write();
					status.setText("OK");
					
				}
			}
		});
		fileMenu.add(writecgffMI);
		fileMenu.addSeparator();
    
		MenuItem printMI = new MenuItem("Print...");	// write  .cgff file menu item
		printMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        Toolkit tk = Toolkit.getDefaultToolkit();
//         Properties ppr = new Properties();
        PrintJob pj = tk.getPrintJob(MainWindow.this, "Print canvas", ppr);
        Graphics gr = pj.getGraphics();
        wc.entries.draw(gr);
        gr.dispose();
        pj.end();
// 				FileDialog f = new FileDialog(instance, "Write cgff and gff files. (.cgff and .gff will be added to file name)", FileDialog.SAVE);
// 				f.show();
// 				String fname = f.getFile();
// 				String path = f.getDirectory();
// 				if (fname != null) {
// 					String gffname = fname + ".gff";
// 					String cgffname = fname + ".cgff";
// 					GffFile gf = new GffFile(path, gffname, wc.entries);
// 					CgffFile cgf = new CgffFile(path, cgffname, gffname, wc.entries);
// 					status.setText("Writing " + gffname + "...");
// 					gf.write();
// 					status.setText("Writing " + cgffname + "...");
// 					cgf.write();
// 					status.setText("OK");
// 					
// 				}
			}
		});
		fileMenu.add(printMI);
    
		fileMenu.addSeparator();
    
    // REMOVE THIS!!!!
		openRemoteMI = new MenuItem("Open Remote EntryPair");		// Open Remote EntryPair menu item
		openRemoteMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				String [] args = new String[0];
    // Old ORB init     
// 				org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, new Properties());
    // New ORB init. Forces use of Orbacus implementation
        java.util.Properties props = System.getProperties();
        props.put("org.omg.CORBA.ORBClass",
                 "com.ooc.CORBA.ORB");
        props.put("org.omg.CORBA.ORBSingletonClass",
                 "com.ooc.CORBA.ORBSingleton");
        System.setProperties(props);

        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
    // End New ORB init 
        
				String ref = null;
				try {
					BufferedReader in = new BufferedReader(new FileReader("/nfs/disk50/nic/WWW/PairServer.ior"));
					ref = in.readLine();
				} catch (IOException ioe) { ioe.printStackTrace(); }

				org.omg.CORBA.Object obj = orb.string_to_object(ref);
				alfresco.server.AlfrescoServer server = alfresco.server.AlfrescoServerHelper.narrow(obj);

				String [] names = server.getNames();
				System.out.println("Available pairs:");
				for (int i = 0; i < names.length; i++) {
					System.out.println(names[i]);
				}
				System.out.println();
				System.out.print("Enter pair: ");
				String name = null;
				try {
					BufferedReader inr = new BufferedReader(new InputStreamReader(System.in));
					name = inr.readLine();
				} catch (IOException ioe) { ioe.printStackTrace(); }
				alfresco.server.PairStruct pair = server.getPair(name);
				wc.entries = new EntryPair(wc, pair);
				wc.drawEntries();
				
// 				FileDialog fd = new FileDialog(MainWindow.this, "Open .cgff File", FileDialog.LOAD);
// 				fd.show();
// 				String filename = fd.getFile();		// get file name from file dialog
// 				String path = fd.getDirectory();
// 				if ( filename == null ) { return; }
// 				File cgffF = new File(path, filename);
// 				if (!cgffF.exists() ) {
// 					status.setText(cgffF.getPath() + ": no such file");
// 					return;
// 				}
// 				status.setText("Reading .cgff file...");	// set status label to "Reading..."
// 				try {
// 					CgffParser gp = new CgffParser(wc.entries, cgffF);
// 				} catch (FormatException fex) { 
// 					ErrorDialog ed = new ErrorDialog(MainWindow.this, fex.getMessage());
// 					ed.show();
// 				} catch (IOException ioe) { System.out.println(ioe); }
			}
				
		});
		fileMenu.add(openRemoteMI);


		fileMenu.addSeparator();

		quitMI = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q));
		quitMI.addActionListener(new ActionListener() {		// register event listener
			public void actionPerformed(ActionEvent e) { wc.quit() ; }
		});
		fileMenu.add(quitMI);
		mMenuBar.add(fileMenu);
		
		//Edit menu
		editMenu = new Menu("Edit");
			// menu items
		editFeatureMI = new MenuItem("Edit/Create Feature");
		editFeatureMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof SeqFeature) {
							SeqFeature sf = (SeqFeature) gl;
							FeatureEditDialog fd = new FeatureEditDialog(wc.entries, sf);
							fd.show();
						} else if (gl instanceof Entry) {
							Entry ent = (Entry) gl;
							FeatureEditDialog fd = new FeatureEditDialog(wc.entries, ent);
							fd.show();
						} else {
							FeatureEditDialog fd = new FeatureEditDialog(wc.entries);
							fd.show();
						}
					}
				} else {
					FeatureEditDialog fd = new FeatureEditDialog(wc.entries);
					fd.show();
				}
			}
		});
		editMenu.add(editFeatureMI);

		changeTierMI = new MenuItem("Change Tier");
		changeTierMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				if (wc.entries.numberOfSelectedGlyphs() == 1) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					Glyph gl = (Glyph) glyphs.nextElement();
					if (gl instanceof SeqFeature || gl instanceof Gene) {
						int t = showDialog(gl.getTier());
						if (t != 0) {
							gl.setTier(t);
							wc.drawEntries();
						}
					}
				} else if (wc.entries.numberOfSelectedGlyphs() > 1) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					Glyph gl = (Glyph) glyphs.nextElement();
					int setTier = showDialog(gl.getTier());
					if (setTier != 0) {
						glyphs = wc.entries.selectedGlyphs();
						while (glyphs.hasMoreElements() ) {
							gl = (Glyph) glyphs.nextElement();
							if (gl instanceof SeqFeature || gl instanceof Gene) {
								gl.setTier(setTier);
							}
						}
						wc.drawEntries();
					}
				}
			}
			public int showDialog(int tier) {
				TierDialog d = new TierDialog(MainWindow.this, tier);
				d.show();
				int answer = d.getAnswer();
				int outTier = 0;
				if( answer == TierDialog.OK) {
					outTier = d.getTier();
				}
				return outTier;
			}
		});
		editMenu.add(changeTierMI);

		removeFeatureMI= new MenuItem("Remove Feature");
		removeFeatureMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				OkNoCancelDialog askD = new OkNoCancelDialog(MainWindow.this, "Remove selected feature(s)?", false);
				askD.show();
				int answer = askD.getAnswer();
				if (answer == OkNoCancelDialog.NO || answer == OkNoCancelDialog.CANCEL) return;
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof SeqFeature ||
								gl instanceof CompositeSeqFeature) {
							Glyph parent = gl.getParent();
							parent.removeChild(gl);
							wc.entries.reciprocals.purgeReciprocals();
							wc.drawEntries();
						} else if (gl instanceof Reciprocal) {
							Glyph parent = gl.getParent();
							parent.removeChild(gl);
							wc.drawEntries();
						}
					}
				}
			}
		});
		editMenu.add(removeFeatureMI);
		editMenu.addSeparator();
		
		makeGeneMI = new MenuItem("Make Gene");
		makeGeneMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					Glyph gl = (Glyph) glyphs.nextElement();
					GeneEditDialog d = new GeneEditDialog(MainWindow.this, "New gene", gl.getTier());
					d.show();
// 					int answer = d.getAnswer();
					if ( d.getAnswer() == GeneEditDialog.CANCEL) return;
					int t = d.getTier();
					String n = d.getName();
					if(gl instanceof SeqFeature) {
						SeqFeature sf = (SeqFeature) gl;
						Entry ent = sf.getEntry();
// 						Gene gene = new Gene(ent, "New Gene");
						Gene gene = new Gene(ent, n);
						sf.removeFromParent();
						gene.addChild(sf);
						while(glyphs.hasMoreElements()) {
							Object o = glyphs.nextElement();
							if (o instanceof SeqFeature) {
								SeqFeature seqf = (SeqFeature) o;
								if (seqf.getEntry().equals(ent)) {
									seqf.removeFromParent();
									gene.addChild(seqf);
// 									gene.setTier(gene.getTier());
								}
							}
						}
						gene.setTier(t);
						ent.addChild(gene);
					}
					wc.drawEntries();
				}
			}
		});
		editMenu.add(makeGeneMI);
		
		editGeneMI = new MenuItem("Edit Gene Attributes");
		editGeneMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				if (wc.entries.numberOfSelectedGlyphs() == 1) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while(glyphs.hasMoreElements()) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof Gene) {
							Gene g = (Gene) gl;
							GeneEditDialog d = new GeneEditDialog(MainWindow.this, g.getName(), g.getTier());
							d.show();
							if ( d.getAnswer() == GeneEditDialog.CANCEL) return;
							g.setName(d.getName());
							g.setTier(d.getTier());							
						}
					}
					wc.drawEntries();
				}
			}
		});
		editMenu.add(editGeneMI);
		
		addToGeneMI = new MenuItem("Add Feature to Gene");
		addToGeneMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				if (wc.entries.numberOfSelectedGlyphs() > 1) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while(glyphs.hasMoreElements()) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof Gene) {
							Gene g = (Gene) gl;
							Enumeration gls = wc.entries.selectedGlyphs();
							while(gls.hasMoreElements()) {
								Glyph selgl = (Glyph) gls.nextElement();
								if(selgl instanceof Exon ||
										selgl instanceof UTR ||
										selgl instanceof PolyA) {
									SeqFeature sf = (SeqFeature) selgl;
									sf.removeFromParent();
									g.addChild(sf);
									g.setTier(g.getTier());
								}
							}
						}
					}
					wc.drawEntries();
				}
			}
		});
		editMenu.add(addToGeneMI);
		
		removeFromGeneMI = new MenuItem("Remove Feature from Gene");
		removeFromGeneMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while(glyphs.hasMoreElements()) {
						Glyph gl = (Glyph) glyphs.nextElement();
						Glyph parent = gl.getParent();
						if (parent instanceof Gene) {
							Gene g = (Gene) parent;
							Glyph gparent = g.getParent();
							
							((SeqFeature)gl).setTier(Math.abs(g.getTier()) + 1);
							gl.removeFromParent();
							gparent.addChild(gl);
						} else if (parent instanceof CDS) {
// 							System.out.println(gl + " is child of " + parent);
							CDS cds = (CDS) parent;
							Gene g = (Gene) cds.getParent();
							Glyph gparent = g.getParent();
							((SeqFeature)gl).setTier(Math.abs(g.getTier()) + 1);
							gl.removeFromParent();
							gparent.addChild(gl);
						}
					}
					wc.drawEntries();
				}
			}
		});
		editMenu.add(removeFromGeneMI);

		editMenu.addSeparator();
		
		confirmExonsMI = new MenuItem("Confirm Exons");
		confirmExonsMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
//         System.out.println("In confirm exons");
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while(glyphs.hasMoreElements()) {
						Glyph gl = (Glyph) glyphs.nextElement();
            if (gl instanceof Exon) {
              Exon ex = (Exon) gl;
//               System.out.println("Found " + ex + " to be confirmed");
              ex.confirmed(true);
            } else if (gl instanceof Gene) {
              Gene g = (Gene) gl;
              g.confirmExons(true);
            }
					}
					wc.drawEntries();
				}
			}
		});
		editMenu.add(confirmExonsMI);
		
		
		unconfirmExonsMI = new MenuItem("Unconfirm Exons");
		unconfirmExonsMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
//         System.out.println("In UNconfirm exons");
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while(glyphs.hasMoreElements()) {
						Glyph gl = (Glyph) glyphs.nextElement();
            if (gl instanceof Exon) {
              Exon ex = (Exon) gl;
//               System.out.println("Found " + ex + " to be UNconfirmed");
              ex.confirmed(false);
            } else if (gl instanceof Gene) {
              Gene g = (Gene) gl;
              g.confirmExons(false);
            }
					}
					wc.drawEntries();
				}
			}
		});
		editMenu.add(unconfirmExonsMI);
    
		hideUnconfirmExonsMI = new CheckboxMenuItem("Hide Unconfirm Exons", false);
		hideUnconfirmExonsMI.addItemListener(new ItemListener() {	// register event listener
			public void itemStateChanged(ItemEvent e) {
//         wc.entries.showUnconfirmedExons(false);
        wc.entries.showUnconfirmedExons(!hideUnconfirmExonsMI.getState());
				wc.drawEntries();
			}
		});
		editMenu.add(hideUnconfirmExonsMI);
		
		editMenu.addSeparator();
		makeReciprocalMI = new MenuItem("Make Reciprocal");
		makeReciprocalMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				if (wc.entries.numberOfSelectedGlyphs() == 2) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					Glyph gl1 = (Glyph) glyphs.nextElement();
					Glyph gl2 = (Glyph) glyphs.nextElement();
					if (gl1 instanceof SeqFeature && gl2 instanceof SeqFeature) {
						SeqFeature sf1 = (SeqFeature) gl1;
						SeqFeature sf2 = (SeqFeature) gl2;
						if (sf1.getEntry().equals(wc.entries.entry1)) {
							wc.entries.reciprocals.addChild(new Reciprocal(sf1, sf2));
						} else {
							wc.entries.reciprocals.addChild(new Reciprocal(sf2, sf1));							
						}
						wc.drawEntries();
					}
				}
			}
		});
		editMenu.add(makeReciprocalMI);
		
		
		
		mMenuBar.add(editMenu);
		
		//Function menu
		functionMenu = new Menu("Function");
			// menu items
			cpgMI = new MenuItem("CpG (Micklem)");	// CpG menu item
		cpgMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Finding CpG islands, this may take a while.");
				wc.entries.callCpG();
				wc.drawEntries();
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
			}
		});
		functionMenu.add(cpgMI);

		repeatMaskerMI = new MenuItem("RepeatMasker (Smit & Green)");	// RepeatMasker menu item
		repeatMaskerMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				RepeatMaskerSpeciesDialog sd = new RepeatMaskerSpeciesDialog(MainWindow.this,
						                                                         wc.entries.entry1.getFilename(),
						                                                         wc.entries.entry2.getFilename());
				sd.show();
				if(sd.wasCanceled()) return;
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Finding repeats, this may take a while.");
        wc.entries.callRepeatMasker(sd.getOption1(), sd.getOption2());
// // 				wc.entries.entry1.callRepeatMasker();
// 				wc.entries.entry1.callRepeatMasker(sd.getOption1());
// 				status.setText("Finding repeats in " + wc.entries.entry2 + ", This may take a while.");
// // 				wc.entries.entry2.callRepeatMasker();
// 				wc.entries.entry2.callRepeatMasker(sd.getOption2());
				wc.drawEntries();
				wc.pw.repCheckboxP.setEnabled(true);
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
			}
			
			
		});
		functionMenu.add(repeatMaskerMI);
		
		functionMenu.addSeparator();
		
		genscanMI = new MenuItem("Genscan (Burge & Karlin)");		// Genscan menu item
		genscanMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Running genscan, this may take a while.");
				wc.entries.callGenscan();
				wc.drawEntries();
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
				findRecMI.setEnabled(true);
			}
		});
		functionMenu.add(genscanMI);

		blastnMI = new MenuItem("Blastn alignment");		// blastalign menu item
		blastnMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Running blast, this may take a while.");
				wc.entries.callBlastnAlign();
				wc.drawEntries();
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
// 				findRecMI.setEnabled(true);
			}
		});
		functionMenu.add(blastnMI);
		functionMenu.addSeparator();

// 		blastwiseMI = new MenuItem("BlastWise (Birney)");		// blastwise menu item
// 		blastwiseMI.addActionListener(new ActionListener() {	// register event listener
// 			public void actionPerformed(ActionEvent e) {	
// 				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
// 				status.setText("Running blastwise, this may take a while.");
// 				wc.entries.callBlastWise();
// 				wc.drawEntries();
// 				status.setText("OK");
// 				setCursor(Cursor.getDefaultCursor());
// 			}
// 		});
// 		functionMenu.add(blastwiseMI);
//     
		blGenewiseMI = new MenuItem("blGenewise (blast + genewise)");		// blGenewise menu item
		blGenewiseMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Running blGenewise, this may take a while.");
				wc.entries.callBlGenewise();
				wc.drawEntries();
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
			}
		});
		functionMenu.add(blGenewiseMI);
    
		MenuItem genewiseMI = new MenuItem("genewise (Birney)");		// genewise menu item
		genewiseMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
        int cancelcount = 0;
        FastaFile fF1 = null;
        FastaFile fF2 = null;
				FileDialog fd = new FileDialog(MainWindow.this, "Select Fasta File for sequence 1", FileDialog.LOAD);
				fd.show();
				String filename1 = fd.getFile();		// get file name from file dialog
				String path1 = fd.getDirectory();
				if ( filename1 != null ) { 
				  fF1 = new FastaFile(path1, filename1);
				  if (!fF1.exists() ) {
					  status.setText(fF1.getPath() + ": no such file");
					  return;
				  }
        } else {
          cancelcount++;
        }
				fd = new FileDialog(MainWindow.this, "Select Fasta File for sequence 2", FileDialog.LOAD);
				fd.show();
				String filename2 = fd.getFile();		// get file name from file dialog
				String path2 = fd.getDirectory();
				if ( filename2 != null ) {  
				  fF2 = new FastaFile(path2, filename2);
				  if (!fF2.exists() ) {
					  status.setText(fF2.getPath() + ": no such file");
					  return;
				  }
        } else {
          cancelcount++;
        }
        
        if (cancelcount == 2) { return; }
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Running genewise, this may take a while.");
// 				wc.entries.callEst_Genome(fF1.sequences(), fF2.sequences());
				wc.entries.callGenewise(fF1, fF2);
				wc.drawEntries();
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
			}
		});
		functionMenu.add(genewiseMI);

		functionMenu.addSeparator();
		MenuItem blest_genomeMI = new MenuItem("BlEst_Genome (blast + est_genome)");		// blEst_genome menu item
		blest_genomeMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Running blEst_genome, this may take a while.");
				wc.entries.callBlEst_Genome();
				wc.drawEntries();
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
			}
		});
		functionMenu.add(blest_genomeMI);

		est_genomeMI = new MenuItem("est_genome (Mott)");		// est_genome menu item
		est_genomeMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
        int cancelcount = 0;
        FastaFile fF1 = null;
        FastaFile fF2 = null;
				FileDialog fd = new FileDialog(MainWindow.this, "Select Fasta File for sequence 1", FileDialog.LOAD);
				fd.show();
				String filename1 = fd.getFile();		// get file name from file dialog
				String path1 = fd.getDirectory();
				if ( filename1 != null ) { 
				  fF1 = new FastaFile(path1, filename1);
				  if (!fF1.exists() ) {
					  status.setText(fF1.getPath() + ": no such file");
					  return;
				  }
        } else {
          cancelcount++;
        }
				fd = new FileDialog(MainWindow.this, "Select Fasta File for sequence 2", FileDialog.LOAD);
				fd.show();
				String filename2 = fd.getFile();		// get file name from file dialog
				String path2 = fd.getDirectory();
				if ( filename2 != null ) {  
				  fF2 = new FastaFile(path2, filename2);
				  if (!fF2.exists() ) {
					  status.setText(fF2.getPath() + ": no such file");
					  return;
				  }
        } else {
          cancelcount++;
        }
        
        if (cancelcount == 2) { return; }
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Running est_genome, this may take a while.");
// 				wc.entries.callEst_Genome(fF1.sequences(), fF2.sequences());
				wc.entries.callEst_Genome(fF1, fF2);
				wc.drawEntries();
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
			}
		});
		functionMenu.add(est_genomeMI);
		
		MenuItem spideyMI = new MenuItem("spidey");		// spidey menu item
		spideyMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
        int cancelcount = 0;
        FastaFile fF1 = null;
        FastaFile fF2 = null;
				FileDialog fd = new FileDialog(MainWindow.this, "Select Fasta File for sequence 1", FileDialog.LOAD);
				fd.show();
				String filename1 = fd.getFile();		// get file name from file dialog
				String path1 = fd.getDirectory();
				if ( filename1 != null ) { 
				  fF1 = new FastaFile(path1, filename1);
				  if (!fF1.exists() ) {
					  status.setText(fF1.getPath() + ": no such file");
					  return;
				  }
        } else {
          cancelcount++;
        }
				fd = new FileDialog(MainWindow.this, "Select Fasta File for sequence 2", FileDialog.LOAD);
				fd.show();
				String filename2 = fd.getFile();		// get file name from file dialog
				String path2 = fd.getDirectory();
				if ( filename2 != null ) {  
				  fF2 = new FastaFile(path2, filename2);
				  if (!fF2.exists() ) {
					  status.setText(fF2.getPath() + ": no such file");
					  return;
				  }
        } else {
          cancelcount++;
        }
        
        if (cancelcount == 2) { return; }
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				status.setText("Running spidey, this may take a while.");
// 				wc.entries.callEst_Genome(fF1.sequences(), fF2.sequences());
				wc.entries.callSpidey(fF1, fF2);
				wc.drawEntries();
				status.setText("OK");
				setCursor(Cursor.getDefaultCursor());
			}
		});
		functionMenu.add(spideyMI);
		
// 		tsswMI = new MenuItem("tssw promoter prediction (Solovyev)");		// tssw menu item
// 		tsswMI.addActionListener(new ActionListener() {	// register event listener
// 			public void actionPerformed(ActionEvent e) {	
// 				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
// 				status.setText("Running tssw, this may take a while.");
// 				if (wc.entries.numberOfSelectedGlyphs() > 0) {
// 					Enumeration glyphs = wc.entries.selectedGlyphs();
// 					while (glyphs.hasMoreElements() ) {
// 						Glyph gl = (Glyph) glyphs.nextElement();
// 						if (gl instanceof SeqFeature) {
// 							Tssw t = new Tssw((SeqFeature) gl);
// 							t.addPromotersToEntry();
// 						}
// 					}
// 				}
// 				
// 				wc.drawEntries();
// 				status.setText("OK");
// 				setCursor(Cursor.getDefaultCursor());
// 				findRecMI.setEnabled(true);
// 			}
// 		});
//     tsswMI.setEnabled(false);
// 		functionMenu.add(tsswMI);

// 		findRecMI = new MenuItem("Find reciprocals");		// Find reciprocals menu item
// 		findRecMI.addActionListener(new ActionListener() {	// register event listener
// 			public void actionPerformed(ActionEvent e) {	
// 				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
// 				status.setText("Finding reciprocals, this may take a while.");
// 				wc.entries.findReciprocals();
// 				wc.drawEntries();
// 				status.setText("OK");
// 				setCursor(Cursor.getDefaultCursor());
// 			}
// 		});
// 		findRecMI.setEnabled(false);
// 		functionMenu.add(findRecMI);

		
		functionMenu.addSeparator();
		
		regsetMI = new MenuItem("Region set");		// Region set menu item
		regsetMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				wc.pw.show();	// show region set parameter window
				if (wc.pw.dotterSet != null) {
					wc.entries.setDotterRegions(wc.pw.dotterSet);
				}
				if (wc.entriesSet()) {
					wc.drawEntries();
				}
			}
		});
		functionMenu.add(regsetMI);



		mMenuBar.add(functionMenu);
		
		// View menu
		viewMenu = new Menu("View");
			// menu items
		sequenceMI = new MenuItem("Sequence"); // (sub)sequence menu item
		sequenceMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof SeqFeature) {
							SeqFeature sf = (SeqFeature) gl;
							SequenceDisplay seqD = new SequenceDisplay(sf);
							seqD.show();
						} else if (gl instanceof RegionSet) {
							RegionSet rs = (RegionSet) gl;
							Enumeration rsen = rs.getChildEnumeration();
							while(rsen.hasMoreElements()) {
								DotterRegion dr = (DotterRegion) rsen.nextElement();
								SequenceDisplay seqD = new SequenceDisplay(dr);
								seqD.show();
							}
						} else if (gl instanceof Reciprocal) {
							Reciprocal r = (Reciprocal) gl;
							SequenceDisplay seqD1 = new SequenceDisplay(r.getFeature1());
							seqD1.show();
							SequenceDisplay seqD2 = new SequenceDisplay(r.getFeature2());
							seqD2.show();
						}
					}
				}
			}
		});
		viewMenu.add(sequenceMI);

		MenuItem aaSequenceMI = new MenuItem("Amino acid sequence translation"); // aa sequence menu item
		aaSequenceMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
// 				System.out.println("Selected Amino acid sequence translation menu item");
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
// 					System.out.println("Selected glyphs > 0");
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof Gene) {
// 							System.out.println("Selected glyphs is a Gene");
							Gene g = (Gene) gl;
							CDS c = g.getCDS();
							String aaseq = c.getAminoAcidSequence();
							String title = "Translation of " + g.toString();
							SequenceDisplay seqD = new SequenceDisplay(aaseq, title);
							seqD.show();
// 							System.out.println(aaseq);
// 							SequenceDisplay seqD = new SequenceDisplay(sf);
// 							seqD.show();
						}
					}
				}
			}
		});
		viewMenu.add(aaSequenceMI);
		
		atgMI = new CheckboxMenuItem("ATGs", false); // ATGs sequence menu item
		atgMI.addItemListener(new ItemListener() {	// register event listener
			public void itemStateChanged(ItemEvent e) {
        if(atgMI.getState()) {	
				  wc.getEntry1().addATGs();
				  wc.getEntry2().addATGs();
        } else {
          wc.getEntry1().removeChildrenByClass(ATG.class);
          wc.getEntry2().removeChildrenByClass(ATG.class);
        }
			  wc.drawEntries();
			}
		});
		viewMenu.add(atgMI);
		
		dotterMI = new MenuItem("Dotter (Sonnhammer & Durbin)"); //  to call dotter on current RegionSet (etc.)
		dotterMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				if (wc.entries.currentRegionSet != null) {
// 					System.out.println("Callin dotter on " + wc.entries.currentRegionSet);
					wc.entries.currentRegionSet.callDotter();
					return;
				}
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof Reciprocal) {
							Reciprocal r = (Reciprocal) gl;
							r.callDotter();
						} else if (gl instanceof RegionSet) {
							RegionSet rs = (RegionSet) gl;
							rs.callDotter();
						} else if (gl instanceof SeqFeature) {
							if (wc.entries.numberOfSelectedGlyphs() == 2 && glyphs.hasMoreElements()) {
								Glyph othergl = (Glyph) glyphs.nextElement();
								if (othergl instanceof SeqFeature) {
									wc.entries.callDotter((SeqFeature)gl, (SeqFeature)othergl);
									return;
								}
							}
						}
					}
				}
			}
		});
		viewMenu.add(dotterMI);

		dbalignMI = new MenuItem("dba alignment (Birney & Durbin)"); //  to call dba on current RegionSet (etc.)
		dbalignMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
// 				System.out.println("Calling DBA");
        if (wc.entries.currentRegionSet != null) {
// 					System.out.println("Showing dbas of " + wc.entries.currentRegionSet);
					wc.entries.currentRegionSet.showDbas();
					return;
				}
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
//           System.out.println("There are more than 0 glyphs, good");
          Properties prop = (Properties) wc.getResources().get("Dba");
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
//             System.out.println("checking first glyph");
						if (gl instanceof Reciprocal) {
							Reciprocal r = (Reciprocal) gl;
// 							if(r.getFeature1() instanceof DbaBlock) {
							if(r.getFeature1() instanceof HCR) {
// 								DbaBlock bl = (DbaBlock) r.getFeature1();
								HCR bl = (HCR) r.getFeature1();
								Vector bv = new Vector();
								AlignmentBlock alb = bl.getBlock();
								if (alb != null) {
									bv.addElement(bl.getBlock());
								} else {
									HCR dba1 = (HCR) r.getFeature1();
									HCR dba2 = (HCR) r.getFeature2();
									SeqRange r1 = dba1.getSeqRange();
									SeqRange r2 = dba2.getSeqRange();
									HCR ndba1 = new HCR(dba1.getEntry(),
											new SeqRange(r1.getStart() - 3, r1.getStop() + 3, r1.isComplement() )
											);
									HCR ndba2 = new HCR(dba2.getEntry(),
											new SeqRange(r2.getStart() - 3, r2.getStop() + 3, r2.isComplement() )
											);
// 									NewDba d = new NewDba(r.getFeature1(), r.getFeature2());
									Dba d = new Dba(ndba1, ndba2, prop);
									if(d.hasBlocks()) {
										bv = d.getAlignments();
										((HCR)r.getFeature1()).setBlock((AlignmentBlock)bv.elementAt(0));
										((HCR)r.getFeature2()).setBlock((AlignmentBlock)bv.elementAt(0));
									}
								}
								AlignmentDisplay alD = new AlignmentDisplay(bv);
								alD.show();
							} else {
								
                SeqFeature f1 = r.getFeature1();
                SeqFeature f2 = r.getFeature2();
                Entry entry1 = f1.getEntry();
                Entry entry2 = f2.getEntry();
                Vector rep1 = entry1.getRepeats();
								Vector rep2 = entry2.getRepeats();
      // New local/Corba stuff
                GffDataStruct result = null;
                String [] dbaBlocks = null;
                try {
                  if (prop.getProperty("local").equals("true")) {
                    alfresco.Dba d = new Dba(f1, f2, rep1, rep2, prop);
                    d.start();
                    d.join();
                    result = d.getGffData();
                  }else {
                    InputStructGenerator isg = new InputStructGenerator ();
                    SeqRange range1 = f1.getSeqRange();
                    SeqRange range2 = f2.getSeqRange();
                    int start1 = range1.getStart();
                    int start2 = range2.getStart();
                    isg.addInput(entry1.getFilename() + "_" + start1 + "-" + range1.getStop(), f1.getRepeatMaskedSequence());
                    isg.addInput(entry2.getFilename() + "_" + start2 + "-" + range2.getStop(), f2.getRepeatMaskedSequence());
                    // have to set parameters: match, gap, offsets, etc!!
                    isg.addParameter("matchA=" + prop.get("matchA"));
                    isg.addParameter("matchB=" + prop.get("matchB"));
                    isg.addParameter("matchC=" + prop.get("matchC"));
                    isg.addParameter("matchD=" + prop.get("matchD"));
                    isg.addParameter("umatch=" + prop.get("umatch"));
                    isg.addParameter("gap=" + prop.get("gap"));
                    isg.addParameter("blockopen=" + prop.get("blockopen"));
                    isg.addParameter("offset1=" + start1);
                    isg.addParameter("offset2=" + start2);
                    CorbaMethodRunner cmr = new CorbaMethodRunner("Dba", isg.getInputStruct(), prop);
                    cmr.start();
			              cmr.join();
                    result = cmr.getGffData();
                  }
                  dbaBlocks = result.suplements;
                  
                } catch (Exception ex) {
                  if (!wc.batch) {
			              ErrorDialog ed = new ErrorDialog(wc.mw, ex.getMessage());
                  } else {
                    ex.printStackTrace();
                  }
			            return;
                }      
								if(dbaBlocks != null) {
									AlignmentDisplay alD = new AlignmentDisplay(AlignmentBlock.pffToAlignmentBlocks(dbaBlocks, entry1, entry2));
									alD.show();
								}
     // How it used to be....
// 								Dba d = new Dba(r.getFeature1(), r.getFeature2(), rep1, rep2, prop);
// 								if(d.hasBlocks()) {
// 									AlignmentDisplay alD = new AlignmentDisplay(d.getAlignments());
// 									alD.show();
// 								}
								return;
							}
						} else if (gl instanceof RegionSet) {
							RegionSet rs = (RegionSet) gl;
							rs.showDbas();
						} else if (gl instanceof SeqFeature) {
							if (wc.entries.numberOfSelectedGlyphs() == 2 && glyphs.hasMoreElements()) {
// 								System.out.println("There are 2 seqfeatures, even better");
                Glyph othergl = (Glyph) glyphs.nextElement();
								if (othergl instanceof SeqFeature) {
                  SeqFeature f1 = (SeqFeature)gl;
                  SeqFeature f2 = (SeqFeature)othergl;
                  Entry entry1 = f1.getEntry();
                  Entry entry2 = f2.getEntry();
									Vector rep1 = entry1.getRepeats();
									Vector rep2 = entry2.getRepeats();
      // New stuff 
                  GffDataStruct result = null;
                  String [] dbaBlocks = null;
                  try {
                    if (prop.getProperty("local").equals("true")) {
                      System.out.println("running dba locally");
                      alfresco.Dba d = new Dba(f1, f2, rep1, rep2, prop);
                      d.start();
                      d.join();
                      result = d.getGffData();
                    }else {
//                       System.out.println("running dba remotely");
                      InputStructGenerator isg = new InputStructGenerator ();
                      SeqRange range1 = f1.getSeqRange();
                      SeqRange range2 = f2.getSeqRange();
                      int start1 = range1.getStart();
                      int start2 = range2.getStart();
                      isg.addInput(entry1.getFilename() + "_" + start1 + "-" + range1.getStop(), f1.getRepeatMaskedSequence());
                      isg.addInput(entry2.getFilename() + "_" + start2 + "-" + range2.getStop(), f2.getRepeatMaskedSequence());
                      // have to set parameters: match, gap, offsets, etc!!
                      isg.addParameter("matchA=" + prop.get("matchA"));
                      isg.addParameter("matchB=" + prop.get("matchB"));
                      isg.addParameter("matchC=" + prop.get("matchC"));
                      isg.addParameter("matchD=" + prop.get("matchD"));
                      isg.addParameter("umatch=" + prop.get("umatch"));
                      isg.addParameter("gap=" + prop.get("gap"));
                      isg.addParameter("blockopen=" + prop.get("blockopen"));
                      isg.addParameter("offset1=" + start1);
                      isg.addParameter("offset2=" + start2);
                      CorbaMethodRunner cmr = new CorbaMethodRunner("Dba", isg.getInputStruct(), prop);
//                       System.out.println("CorbaMethodRunner object: " + cmr);
                      cmr.start();
			                cmr.join();
                      result = cmr.getGffData();
                    }
                    dbaBlocks = result.suplements;
//                     System.out.println("Elements in dbaBlocks: " + dbaBlocks.length);
//                     for (int i = 0; i < dbaBlocks.length; i++) {
//                       System.out.println("blocks: >" + dbaBlocks[i] + "<");   
//                     }
                    if (dbaBlocks.length == 1 && dbaBlocks[0].equals("") ) {
//                       System.out.println("Empty dba result");
                      return;
                    }

                  } catch (Exception ex) {
                    if (!wc.batch) {
                      ex.printStackTrace();
			                ErrorDialog ed = new ErrorDialog(wc.mw, ex.getMessage());
//                       ed.show();
                    } else {
                      ex.printStackTrace();
                    }
			              return;
                  }      
								  if(dbaBlocks != null) {
									  AlignmentDisplay alD = new AlignmentDisplay(AlignmentBlock.pffToAlignmentBlocks(dbaBlocks, entry1, entry2));
									  alD.show();
								  }
                             
			// How it used to be. To be commented out
// 									Dba d = new Dba((SeqFeature)gl, (SeqFeature)othergl, rep1, rep2, prop);
// // 									System.out.println((d.hasBlocks()?"":"No ") + "Blocks Found!");
// 									if(d.hasBlocks()) {
// 										AlignmentDisplay alD = new AlignmentDisplay(d.getAlignments());
// 										alD.show();
// 									}
									return;
								}
							}
						}
					}
				}
			}
		});
		viewMenu.add(dbalignMI);

		NWalignMI = new MenuItem("NW alignment"); //  to do NW on features (etc.)
		NWalignMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
// 				if (wc.entries.currentRegionSet != null) {
// // 					System.out.println("Showing dbas of " + wc.entries.currentRegionSet);
// 					wc.entries.currentRegionSet.showDbas();
// 					return;
// 				}
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof Reciprocal) {
							Reciprocal r = (Reciprocal) gl;
// 							if(r.getFeature1() instanceof DbaBlock) {
// 								DbaBlock bl = (DbaBlock) r.getFeature1();
// 								Vector bv = new Vector();
// 								bv.addElement(bl.getBlock());
// 								AlignmentDisplay alD = new AlignmentDisplay(bv);
// 								alD.show();
// 							} else {
								NWAlign nw = new NWAlign(r.getFeature1(), r.getFeature2());
								Vector v = new Vector();
								AlignmentBlock bl = nw.getAlignmentBlock();
								v.addElement(bl);
								AlignmentDisplay alD = new AlignmentDisplay(v);
								alD.show();
								return;
// 							}
// 						} else if (gl instanceof RegionSet) {
// 							RegionSet rs = (RegionSet) gl;
// 							rs.showDbas();
						} else if (gl instanceof SeqFeature) {
							if (wc.entries.numberOfSelectedGlyphs() == 2 && glyphs.hasMoreElements()) {
								Glyph othergl = (Glyph) glyphs.nextElement();
								if (othergl instanceof SeqFeature) {
									NWAlign nw = new NWAlign((SeqFeature)gl, (SeqFeature)othergl);
									Vector v = new Vector();
									v.addElement(nw.getAlignmentBlock());
									AlignmentDisplay alD = new AlignmentDisplay(v);
									alD.show();
								}
									return;
							}
						}
					}
				}
			}
			
		});
		viewMenu.add(NWalignMI);

		SWalignMI = new MenuItem("SW alignment"); //  to do SW on features (etc.)
		SWalignMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
// 				if (wc.entries.currentRegionSet != null) {
// // 					System.out.println("Showing dbas of " + wc.entries.currentRegionSet);
// 					wc.entries.currentRegionSet.showDbas();
// 					return;
// 				}
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof Reciprocal) {
							Reciprocal r = (Reciprocal) gl;
// 							if(r.getFeature1() instanceof DbaBlock) {
// 								DbaBlock bl = (DbaBlock) r.getFeature1();
// 								Vector bv = new Vector();
// 								bv.addElement(bl.getBlock());
// 								AlignmentDisplay alD = new AlignmentDisplay(bv);
// 								alD.show();
// 							} else {
								SWAlign sw = new SWAlign(r.getFeature1(), r.getFeature2());
								Vector v = new Vector();
								AlignmentBlock bl = sw.getAlignmentBlock();
								v.addElement(bl);
								AlignmentDisplay alD = new AlignmentDisplay(v);
								alD.show();
								return;
// 							}
// 						} else if (gl instanceof RegionSet) {
// 							RegionSet rs = (RegionSet) gl;
// 							rs.showDbas();
						} else if (gl instanceof SeqFeature) {
							if (wc.entries.numberOfSelectedGlyphs() == 2 && glyphs.hasMoreElements()) {
								Glyph othergl = (Glyph) glyphs.nextElement();
								if (othergl instanceof SeqFeature) {
									SWAlign sw = new SWAlign((SeqFeature)gl, (SeqFeature)othergl);
									Vector v = new Vector();
									v.addElement(sw.getAlignmentBlock());
									AlignmentDisplay alD = new AlignmentDisplay(v);
									alD.show();
								}
									return;
							}
						}
					}
				}
			}
			
		});
		viewMenu.add(SWalignMI);
		viewMenu.addSeparator();
		MenuItem exonsMI = new MenuItem("save exons to file"); //  test MI
		exonsMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				FastaFile f1 = new FastaFile (".", "exons1", wc.getEntry1(), wc.getEntry1().getExonRanges());
				f1.write();
				FastaFile f2 = new FastaFile (".", "exons2", wc.getEntry2(), wc.getEntry2().getExonRanges());
				f2.write();
			}
		});
		viewMenu.add(exonsMI);
		
		
				
		mMenuBar.add(viewMenu);
		
		// Settings menu
		settingsMenu = new Menu("Settings");
			// menu items
		showIntronsMI = new CheckboxMenuItem("Show Introns", false);
		showIntronsMI.addItemListener(new ItemListener() {		// register event listener
			public void itemStateChanged(ItemEvent e) { 
// 				boolean show = showIntronsMI.getState();
// 				if(show) {
// // 					System.out.println("Introns should be shown");
// 					wc.entries.intronsVisible(true);
// 				} else {
// // 					System.out.println("Introns should NOT be shown");
// 				}
				wc.entries.showIntrons(showIntronsMI.getState());
				wc.drawEntries();
			}
		});
		settingsMenu.add(showIntronsMI);
		
		dbaSettingsMI = new MenuItem("dba settings...");	
		dbaSettingsMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        // old parameter dialog
// 				wc.dbapw.show();
        
        // new generic dialog
        MethodsDialog md = new MethodsDialog(wc, "Dba");
				md.show();
        
			}
		});
		settingsMenu.add(dbaSettingsMI);
// 		newdbaSettingsMI = new MenuItem("newdba settings...");	
// 		newdbaSettingsMI.addActionListener(new ActionListener() {	// register event listener
// 			public void actionPerformed(ActionEvent e) {
// 				wc.newdbapw.show();
// 			}
// 		});
// 		settingsMenu.add(newdbaSettingsMI);
// 		dbbSettingsMI = new MenuItem("dbb settings...");	
// 		dbbSettingsMI.addActionListener(new ActionListener() {	// register event listener
// 			public void actionPerformed(ActionEvent e) {
// 				wc.dbbpw.show();
// 			}
// 		});
// 		settingsMenu.add(dbbSettingsMI);
		
		tfbsMI = new MenuItem("TF binding sites");
		tfbsMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
				wc.getTFBSDialog().show();
			}
		});
		settingsMenu.add(tfbsMI);
    tfbsMI.setEnabled(false);

		MenuItem BlastAlignMI = new MenuItem("BlastAlign");
		BlastAlignMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "BlastAlign");
				md.show();
			}
		});
		settingsMenu.add(BlastAlignMI);
    

// 		MenuItem BlastWiseMI = new MenuItem("BlastWise");
// 		BlastWiseMI.addActionListener(new ActionListener() {	// register event listener
// 			public void actionPerformed(ActionEvent e) {
//         MethodsDialog md = new MethodsDialog(wc, "BlastWise");
// 				md.show();
// 			}
// 		});
// 		settingsMenu.add(BlastWiseMI);
//     

		MenuItem BlEst_GenomeMI = new MenuItem("BlEst_Genome");
		BlEst_GenomeMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "BlEst_Genome");
				md.show();
			}
		});
		settingsMenu.add(BlEst_GenomeMI);
    
		MenuItem BlGenewiseMI = new MenuItem("BlGenewise");
		BlGenewiseMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "BlGenewise");
				md.show();
			}
		});
		settingsMenu.add(BlGenewiseMI);

		MenuItem CpGMI = new MenuItem("CpG");
		CpGMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "CpG");
				md.show();
			}
		});
		settingsMenu.add(CpGMI);
    
		MenuItem Est_GenomeMI = new MenuItem("Est_Genome");
		Est_GenomeMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "Est_Genome");
				md.show();
			}
		});
		settingsMenu.add(Est_GenomeMI);

		MenuItem SpideyMI = new MenuItem("Spidey");
		SpideyMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "Spidey");
				md.show();
			}
		});
		settingsMenu.add(SpideyMI);
    
		MenuItem GenewiseMI = new MenuItem("Genewise");
		GenewiseMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "Genewise");
				md.show();
			}
		});
		settingsMenu.add(GenewiseMI);
    
		MenuItem GenscanMI = new MenuItem("Genscan");
		GenscanMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "Genscan");
				md.show();
			}
		});
		settingsMenu.add(GenscanMI);
    
		MenuItem RepeatMaskerMI = new MenuItem("RepeatMasker");
		RepeatMaskerMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
        MethodsDialog md = new MethodsDialog(wc, "RepeatMasker");
				md.show();
			}
		});
		settingsMenu.add(RepeatMaskerMI);
//     tfbsMI.setEnabled(false);
		
		mMenuBar.add(settingsMenu);
		
// 		MainWindow win = this;

// 		entryGadget1 = new EntryGadget(win, "Select Sequence 1", new EntryUpdater() {
// 			public void update (Entry e) {
// 				wc.setEntry1(e);
// 			}
// 		});
// 		entryGadget1.setLocation(10,60);
// //		entryGadget1.setBackground(Color.red);
// 		add(entryGadget1);
// 		
// 		entryGadget2 = new EntryGadget(win, "Select Sequence 2", new EntryUpdater() {
// 			public void update (Entry e) {
// 				wc.setEntry2(e);
// 			}
// 		});
// 		entryGadget2.setLocation(10,90);
// 		add(entryGadget2);
		
	}
	
	/**
	 * Inits GUI for Applet
	 */
	public void initAppletGUI() {
		entryGadget1.hideButtonAndTextField();
		entryGadget2.hideButtonAndTextField();
		
		// File menu
		fileMenu = new Menu("File");
			// menu items
		openRemoteMI = new MenuItem("Open Remote EntryPair");		// Open Remote EntryPair menu item
		openRemoteMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {
// 				String [] args = new String[0];
// 				org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, new Properties());
// 				String ref = null;
// 				try {
// 					BufferedReader in = new BufferedReader(new FileReader("/nfs/disk50/nic/WWW/PairServer.ior"));
// 					ref = in.readLine();
// 				} catch (IOException ioe) { ioe.printStackTrace(); }
// 
// 				org.omg.CORBA.Object obj = orb.string_to_object(ref);
// 				alfresco.server.AlfrescoServer server = alfresco.server.AlfrescoServerHelper.narrow(obj);
				alfresco.server.AlfrescoServer server = wc.getServer();
				String [] names = null;
				try {
					names = server.getNames();
				} catch (org.omg.CORBA.COMM_FAILURE cf) {
					ErrorDialog ed = new ErrorDialog(MainWindow.this, "Could not connect to server!");
					ed.show();
					return;
				}
// 				System.out.println("Available pairs:");
// 				for (int i = 0; i < names.length; i++) {
// 					System.out.println(names[i]);
// 				}
// 				System.out.println();
// 				System.out.print("Enter pair: ");
// 				String name = null;
// 				try {
// 					BufferedReader inr = new BufferedReader(new InputStreamReader(System.in));
// 					name = inr.readLine();
// 				} catch (IOException ioe) { ioe.printStackTrace(); }
				alfresco.applet.PairDialog pd = new alfresco.applet.PairDialog(MainWindow.this, names);
				pd.show();
				String name = pd.getSelection();
				if (name == null) return;
				alfresco.server.PairStruct pair = server.getPair(name);
				wc.entries = new EntryPair(wc, pair);
				wc.drawEntries();
				
			}
				
		});
		fileMenu.add(openRemoteMI);
		
		quitMI = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q));
		quitMI.addActionListener(new ActionListener() {		// register event listener
			public void actionPerformed(ActionEvent e) { MainWindow.this.dispose() ; }
		});
		fileMenu.add(quitMI);
		
		mMenuBar.add(fileMenu);
		
		// View menu
		viewMenu = new Menu("View");
			// menu items
		sequenceMI = new MenuItem("Sequence"); // (sub)sequence menu item
		sequenceMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof SeqFeature) {
							SeqFeature sf = (SeqFeature) gl;
							SequenceDisplay seqD = new SequenceDisplay(sf);
							seqD.show();
						} else if (gl instanceof RegionSet) {
							RegionSet rs = (RegionSet) gl;
							Enumeration rsen = rs.getChildEnumeration();
							while(rsen.hasMoreElements()) {
								DotterRegion dr = (DotterRegion) rsen.nextElement();
								SequenceDisplay seqD = new SequenceDisplay(dr);
								seqD.show();
							}
						} else if (gl instanceof Reciprocal) {
							Reciprocal r = (Reciprocal) gl;
							SequenceDisplay seqD1 = new SequenceDisplay(r.getFeature1());
							seqD1.show();
							SequenceDisplay seqD2 = new SequenceDisplay(r.getFeature2());
							seqD2.show();
						}
					}
				}
			}
		});
		viewMenu.add(sequenceMI);

		NWalignMI = new MenuItem("NW alignment"); //  to do NW on features (etc.)
		NWalignMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof Reciprocal) {
							Reciprocal r = (Reciprocal) gl;
								NWAlign nw = new NWAlign(r.getFeature1(), r.getFeature2());
								Vector v = new Vector();
								AlignmentBlock bl = nw.getAlignmentBlock();
								v.addElement(bl);
								AlignmentDisplay alD = new AlignmentDisplay(v);
								alD.show();
								return;
						} else if (gl instanceof SeqFeature) {
							if (wc.entries.numberOfSelectedGlyphs() == 2 && glyphs.hasMoreElements()) {
								Glyph othergl = (Glyph) glyphs.nextElement();
								if (othergl instanceof SeqFeature) {
									NWAlign nw = new NWAlign((SeqFeature)gl, (SeqFeature)othergl);
									Vector v = new Vector();
									v.addElement(nw.getAlignmentBlock());
									AlignmentDisplay alD = new AlignmentDisplay(v);
									alD.show();
								}
									return;
							}
						}
					}
				}
			}
			
		});
		viewMenu.add(NWalignMI);

		SWalignMI = new MenuItem("SW alignment"); //  to do SW on features (etc.)
		SWalignMI.addActionListener(new ActionListener() {	// register event listener
			public void actionPerformed(ActionEvent e) {	
				if (wc.entries.numberOfSelectedGlyphs() > 0) {
					Enumeration glyphs = wc.entries.selectedGlyphs();
					while (glyphs.hasMoreElements() ) {
						Glyph gl = (Glyph) glyphs.nextElement();
						if (gl instanceof Reciprocal) {
							Reciprocal r = (Reciprocal) gl;
								SWAlign sw = new SWAlign(r.getFeature1(), r.getFeature2());
								Vector v = new Vector();
								AlignmentBlock bl = sw.getAlignmentBlock();
								v.addElement(bl);
								AlignmentDisplay alD = new AlignmentDisplay(v);
								alD.show();
								return;
						} else if (gl instanceof SeqFeature) {
							if (wc.entries.numberOfSelectedGlyphs() == 2 && glyphs.hasMoreElements()) {
								Glyph othergl = (Glyph) glyphs.nextElement();
								if (othergl instanceof SeqFeature) {
									SWAlign sw = new SWAlign((SeqFeature)gl, (SeqFeature)othergl);
									Vector v = new Vector();
									v.addElement(sw.getAlignmentBlock());
									AlignmentDisplay alD = new AlignmentDisplay(v);
									alD.show();
								}
									return;
							}
						}
					}
				}
			}
			
		});
		viewMenu.add(SWalignMI);

				
		mMenuBar.add(viewMenu);
		
	}
					
/**
 * Returns a MainWindow instance. Makes sure that there is only one instance of Mainwindow
 * @return a MainWindow instance
 * @param wc the parent application
 */
	static MainWindow instance(Wcomp wc) {
		if (instance == null) {
			instance = new MainWindow(wc);
		}
		return instance;
	}
}
