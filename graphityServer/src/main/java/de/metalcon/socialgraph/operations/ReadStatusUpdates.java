package de.metalcon.socialgraph.operations;

import java.util.List;

import org.neo4j.graphdb.Node;

import de.metalcon.server.exceptions.RequestFailedException;
import de.metalcon.socialgraph.SocialGraph;

/**
 * read command: status updates
 * 
 * @author Sebastian Schlicht
 * 
 */
public class ReadStatusUpdates extends SocialGraphOperation {

	/**
	 * owner of the stream targeted
	 */
	private final Node poster;

	/**
	 * number of items to be read
	 */
	private final int numItems;

	/**
	 * single stream flag
	 */
	private final boolean ownUpdates;

	/**
	 * create a new read status updates command
	 * 
	 * @param responder
	 *            client responder
	 * @param reader
	 *            reading user
	 * @param poster
	 *            owner of the stream targeted
	 * @param numItems
	 *            number of items to be read
	 * @param ownUpdates
	 *            single stream flag
	 */
	public ReadStatusUpdates(final ClientResponder responder,
			final Node reader, final Node poster, final int numItems,
			final boolean ownUpdates) {
		super(responder, reader);
		this.poster = poster;
		this.numItems = numItems;
		this.ownUpdates = ownUpdates;
	}

	@Override
	protected boolean execute(final SocialGraph graph) {
		try {
			final List<String> activities = graph.readStatusUpdates(
					this.poster, this.user, this.numItems, this.ownUpdates);

			// send stream to client
			int i = 0;
			int lastActivity = activities.size() - 1;
			this.responder.addLine("{\"items\":[");
			for (String activity : activities) {
				if (i != lastActivity) {
					this.responder.addLine(activity + ",");
				} else {
					this.responder.addLine(activity);
				}
				i += 1;
			}
			this.responder.addLine("]}");
		} catch (final RequestFailedException e) {
			this.responder.addLine(e.getMessage());
			this.responder.addLine(e.getSalvationDescription());
		}

		this.responder.finish();
		return false;
	}

	/**
	 * create the Activity stream containing the Activities specified
	 * 
	 * @param activities
	 *            Activities to be contained in the Activity stream
	 * @return Activity stream JSON<br>
	 *         (Activitystrea.ms)
	 */
	public static String getActivityStream(final List<String> activities) {
		final StringBuilder activityStream = new StringBuilder("{\"items\":[");

		final int numItems = activities.size();
		int i = 0;
		for (String activity : activities) {
			activityStream.append(activity);
			if (++i != numItems) {
				activityStream.append(",");
			}
		}

		activityStream.append("]}");
		return activityStream.toString();
	}

}