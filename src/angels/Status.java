/**
 * This class represents the different status associated with an angels
 * status. The fields are used without the program in order maintain naming
 * conventions.
 * 
 * @author Nicholas Kunzler
 */

package angels;

public enum Status {

	COMPLETE, 	// Angel is complete
	HOLD, 		// Angel is missing items
	PULL, 		// The angel needs to be pulled for unknown reasons
	FILLING, 	// Angel is waiting to be given out or started
	OUT, 		// Angel is out at a different location
	NOT_STARTED;// Angel that is non of the above
}
