package h4131.xml;

import java.io.File;


import javafx.stage.FileChooser;

public class XMLfileOpener {// Singleton
	
	private static XMLfileOpener instance = null;

	private XMLfileOpener(){}
	
	protected static XMLfileOpener getInstance(){
		if (instance == null) instance = new XMLfileOpener();
		return instance;
	}


 	public File open(boolean read, String fileType ) throws ExceptionXML{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(fileType);
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		if(read){
			
			File selectedFile = (fileChooser.showOpenDialog(null));
			if (accept(selectedFile)){
				return selectedFile;
			}
			else{
				throw new ExceptionXML("Problem when opening file");
			}
		}
		else{
			return fileChooser.showSaveDialog(null);
		}
		
		
        	
        
 	}
 	
    public boolean accept(File f) {
    	if (f == null) return false;
    	//if (f.isDirectory()) return true;
    	String extension = getExtension(f);
    	if (extension == null) return false;
    	return extension.contentEquals("xml");
    }

    private String getExtension(File f) {
	    String filename = f.getName();
	    int i = filename.lastIndexOf('.');
	    if (i>0 && i<filename.length()-1) 
	    	return filename.substring(i+1).toLowerCase();
	    return null;
   }
}
