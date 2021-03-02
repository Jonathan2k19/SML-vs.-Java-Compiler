/* #######################################################################
 * 	ENVIRONMENT: Assign (several) variable(s) to boolean value(s)
 * 		--> there cannot be any duplicate IDs in the environment
 * 	
 * 	EXAMPLE:
 * 		Environment env = [x = true, y = false]
 * 		--> the value of ID:x would be true
 * 		--> the value of ID:y would be false
 * 		--> the value of ID:z would throw an exception (Unbound id)
 * #######################################################################
 **/

package expressions;

import java.util.HashMap;

public class Environment {
	private HashMap<String, Boolean> environment;	// <nameOfIdentifier, valueOfIdentifier>
	
	// CONSTRUCTOR
	public Environment () {
		this.environment = new HashMap<String, Boolean>();
	}
	
	
	// METHODS
	public boolean lookupId (String name) {
		if (this.environment.get(name) == null) {
			// id does not exist in environment
			throw new NullPointerException("ERROR_ENVIRONMENT: There is no id with name \"" + name + "\" in this environment");
		} else {
			return (this.environment.get(name));
		}
	}
	
	public void removeId (String name) {
		if (this.environment.get(name) == null) {
			throw new IllegalArgumentException("ERROR_ENVIRONMENT: There is no id with name \""+ name + "\" to remove from the environment");
		} else {
			this.environment.remove(name);
		}
	}
	
	public void clearEnv () {
		this.environment.clear();
	}
	
	public void updateId (String name, boolean newValue) {
		if (name != null) {
			this.environment.put(name, newValue);	// the put methods updates the value if key already exists in hash map
		} else {
			throw new IllegalArgumentException("ERROR_ENVIRONMENT: the identifier has to have a name != null");
		}
	}
	
	public void addEnvEntry (String name, boolean value) {
		if(name != null) {
			this.environment.put(name, value);
		}else {
			throw new IllegalArgumentException("ERROR_ENVIRONMENT: the identifier has to have a name != null");
		}
	}
}
