package de.metalcon.socialgraph.operations;

import de.metalcon.socialgraph.SocialGraph;

/**
 * create command: user
 * 
 * @author sebschlicht
 * 
 */
public class CreateUser extends SocialGraphOperation {

	/**
	 * identifier of the new user
	 */
	private final String userId;

	/**
	 * display name of the new user
	 */
	private final String displayName;

	/**
	 * path to the profile picture of the new user
	 */
	private final String profilePicturePath;

	/**
	 * create a new create user command
	 * 
	 * @param responder
	 *            client responder
	 * @param userId
	 *            identifier of the new user
	 * @param displayName
	 *            display name of the new user
	 * @param profilePicturePath
	 *            path to the profile picture of the new user
	 */
	public CreateUser(final ClientResponder responder, final String userId,
			final String displayName, final String profilePicturePath) {
		super(responder, null);
		this.userId = userId;
		this.displayName = displayName;
		this.profilePicturePath = profilePicturePath;
	}

	@Override
	protected boolean execute(final SocialGraph graph) {
		graph.createUser(this.userId, this.displayName, this.profilePicturePath);
		this.responder.addLine("ok");
		return true;
	}

}
