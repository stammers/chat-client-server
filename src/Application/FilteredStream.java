package Application;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * Filters a stream of bytes into a String and appends that to the stored
 * JTextArea.
 * 
 * @author Daniel
 *
 */
class FilteredStream extends FilterOutputStream {
	private JTextArea textArea;
	private String previous;

	/**
	 * Creates a FilteredStream with the given OutputStream and JTextArea.
	 * 
	 * @param aStream
	 * @param textArea
	 */
	public FilteredStream(OutputStream aStream, JTextArea textArea) {
		super(aStream);
		this.textArea = textArea;
		previous = "";
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte b[]) throws IOException {
		String aString = new String(b);
		/*
		 * The code below would replace the last line of this method. It would
		 * stop duplicate lines being printed out, which will occur when the
		 * sink is registered with multiple sources.
		 */
		if(aString.equals("\r\n")){ 
			if(previous.endsWith("\r\n")){ 
				return; 
			}
		} //if the previous string is the same it isn't printed out
		if(!previous.equals(aString+"\r\n")){
			textArea.append(aString);
		}else{ 
			return; 
		}

		//remembers the previous printed string 
		if(aString.equals("\r\n")){
			previous = previous + aString; 
		}else{ 
			previous = aString; 
		}


		//textArea.append(aString);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		String aString = new String(b, off, len);
		/*
		 * The code below would replace the last line of this method. It would
		 * stop duplicate lines being printed out, which will occur when the
		 * sink is registered with multiple sources.
		 */

		if(aString.equals("\r\n")){ 
			if(previous.endsWith("\r\n")){
				return; 
			}
		} //if the previous string is the same it isn't printed out
		if(!previous.equals(aString+"\r\n")){
			textArea.append(aString);
		}else{
			return;
		}

		//remembers the previous printed string 
		if(aString.equals("\r\n")){
			previous = previous + aString; 
		}else{ 
			previous = aString;
		}

		//textArea.append(aString);
	}

}