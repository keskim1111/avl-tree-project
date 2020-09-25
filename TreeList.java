/**
 *
 * Tree list
 *
 * An implementation of a Tree list with key and info
 *
 */
public class TreeList {
	private AVLTree avlTree;

	/**
	 * public TreeList()
	 * constructor.
	 * Complexity: O(1)
	 */
	public TreeList() {
		this.avlTree = new AVLTree();
	}

	/**
	 * public Item retrieve(int i)
	 * returns the item in the ith position if it exists in the list. otherwise,
	 * returns null
	 * Complexity: O(logn)
	 */
	public Item retrieve(int i) {
		if (i < 0 || i > this.avlTree.size() - 1)
			return null;
		AVLTree.IAVLNode node = this.avlTree.treeSelect(i + 1);
		return new Item(node.getKey(), node.getValue());
	}

	/**
	 * public int insert(int i, int k, String s)
	 * inserts an item to the ith position in list with key k and info s. returns -1
	 * if i<0 or i>n otherwise return 0.
	 * Complexity: O(logn)
	 */
	public int insert(int i, int k, String s) {
		if (i < 0 || i > this.avlTree.size())
			return -1;
		this.avlTree.insertByRank(i, k, s);
		return 0;
	}

	/**
	 * public int delete(int i)
	 * deletes an item in the ith posittion from the list. returns -1 if i<0 or
	 * i>n-1 otherwise returns 0.
	 * Complexity: O(logn)
	 */
	public int delete(int i) {
		if (i < 0 || i > this.avlTree.size() - 1)
			return -1;
		this.avlTree.deleteByRank(i);
		return 0;
	}
}
