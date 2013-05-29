/**Name: Victor Malchikov
 * File: BasicListener.java
 */
package hw.button;
import hw.CanvasSpace;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.JPanel;

/**BasicListener class that each button must have to be able to catch
 *               events generated by the user. Each BasicButton object will
 *               have a BasicListener. This forces the user to implement 
 *               an class with an ActionListener.
 * @author Victor
 */
public abstract class BasicListener implements ActionListener, Serializable 
{
	//***************************BasicListener()************************************
	public BasicListener(JPanel container, CanvasSpace drawingArea)
	{
		//implementation to be done by created listener class 
	}
	
}
