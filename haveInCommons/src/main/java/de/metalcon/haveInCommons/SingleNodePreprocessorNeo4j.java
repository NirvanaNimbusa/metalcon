/**
 *
 */
package de.metalcon.haveInCommons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.kernel.Traversal;

/**
 * @author Jonas Kunze
 */
public class SingleNodePreprocessorNeo4j {
	private GraphDatabaseService graphDB;
	private Index<Node> ix;
	private Index<Relationship> relIndex;
	private final DynamicRelationshipType followRelationShip = DynamicRelationshipType
			.withName("follows");

	/*
	 * User i and concept j have k in common if
	 * commonsMap.get(i).get(j).contains(k) is true
	 */
	private HashMap<Long, HashMap<Long, HashSet<Long>>> commonsMap;

	/**
     *
     */
	public SingleNodePreprocessorNeo4j(String dbPath) {
		graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(dbPath);
		ix = graphDB.index().forNodes("nodes");
		relIndex = graphDB.index().forRelationships("edges");
		commonsMap = new HashMap<Long, HashMap<Long, HashSet<Long>>>();
	}

	/*
	 * Processes a depth-first traversal from the main node (1) with the given
	 * uuid. In all paths (to the nodes 2 hops away) the end node (3) is a
	 * concept that has the node in between (2) in common with the main node.
	 * 1->2->3, 1 and 3 have 2 in common
	 * 
	 * This will be stored in the main Map: commonsMap.get(1).get(3).add(2);
	 */
	public void generateIndex(long uuid) {
		Node mainNode = ix.get("id", uuid).getSingle();

		HashMap<Long, HashSet<Long>> thisUserMap = commonsMap.get(uuid);
		if (thisUserMap == null) {
			thisUserMap = new HashMap<Long, HashSet<Long>>();
			commonsMap.put(uuid, thisUserMap);
		}

		for (Path position : Traversal.description().depthFirst()
				.relationships(followRelationShip, Direction.OUTGOING)
				.evaluator(Evaluators.toDepth(2)).traverse(mainNode)) {

			Iterator<Node> it = position.nodes().iterator();

			long neighbourID = (long) it.next().getProperty("id");
			long conceptID = (long) position.endNode().getProperty("id");
			HashSet<Long> conceptFollowers = thisUserMap.get(conceptID);
			if (conceptFollowers == null) {
				conceptFollowers = new HashSet<Long>();
				thisUserMap.put(conceptID, conceptFollowers);
			}
			conceptFollowers.add(neighbourID);
		}
	}

	public long[] getCommonNodes(long uuid1, long uuid2) {
		return toPrimitive(commonsMap.get(uuid1).get(uuid2));
	}

	/*
	 * Converts a Set<Long> to an array of primitive longs
	 */
	protected long[] toPrimitive(Set<Long> list) {
		long[] ints = new long[list.size()];
		int i = 0;
		for (Long n : list) {
			ints[i++] = n;
		}
		return ints;
	}

	public void print(long uuid) {
		HashMap<Long, HashSet<Long>> conceptMap = commonsMap.get(uuid);
		System.out.println(commonsMap.size() + " User cached");
		System.out.println(conceptMap.size() + " concepts for this user");

		for (Long concept : conceptMap.keySet()) {
			System.out.println("# of in common users with " + concept + ": "
					+ conceptMap.get(concept).size());
		}
	}
}