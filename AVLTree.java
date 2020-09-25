/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	private IAVLNode root;
	private IAVLNode min;
	private IAVLNode max;

	/**
	 *  Complexity: O(1)
	 */
	public AVLTree() {
		this.root = null;
		this.min = null;
		this.max = null;
	}

	/**
	 * public boolean empty()
	 * returns true if and only if the tree is empty
	 * Complexity: O(1)
	 */
	public boolean empty() {
		return (this.root == null);
	}
	
	/**
	 * public String search(int k)
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 * Complexity: O(logn)
	 */
	public String search(int k)
	{
		IAVLNode n = findNode(k);
		String toReturn = n == null ? null : n.getValue();
		return toReturn;
	}
	
	/**
	 * public String min()
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 * Complexity: O(1)
	 */
	public String min()
	{
		if(this.empty()) return null;
		return this.min.getValue();
	}

	/**
	 * public String max()
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 * Complexity: O(1)
	 */
	public String max()
	{
		if(this.empty()) return null;
		return this.max.getValue();
	}
	
	/**
	 * public int[] keysToArray()
	 * public int[] keysToArray()
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 * Complexity: O(n)
	 */
	public int[] keysToArray()
	{
		int[] keysArr = new int[this.size()];
		recToArray(this.root, keysArr, null, 0, "k");
		return keysArr;
	}
	
	/**
	 * public String[] infoToArray()
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 * Complexity: O(n)
	 */
	public String[] infoToArray()
	{
		String[] infoArr = new String[this.size()];
		recToArray(this.root, null, infoArr, 0, "i");
		return infoArr;
	}
	
	/** 
	 * private int recToArray(IAVLNode node, int[] keysArr, String[] infoArr, int index, String mode)
	 *  node - the node to start the inorder travel
	 *  keysArr / infoArr - the array to fill
	 *  index - the index from which to start inserting items to the array
	 *  mode - "i" for info or "k" for keys
	 *  returns the first index that is free in the array.
	 *  Complexity: O(n)
	 */
	private int recToArray(IAVLNode node, int[] keysArr, String[] infoArr, int index, String mode) {
		if (node == null) {
			return index;
		} else {
			index = recToArray(node.getLeft(), keysArr, infoArr, index, mode);
			if (mode.equals("k")) {
				keysArr[index++] = node.getKey();
			} else {
				infoArr[index++] = node.getValue();
			}
			index = recToArray(node.getRight(), keysArr, infoArr, index, mode);
		}
		return index;
	}

	/**
	 * public int size()
	 * Returns the number of nodes in the tree.
	 * Complexity: O(1)
	 */
	public int size()
	{
		if (this.empty()) return 0;
		return (int)((AVLNode) this.root).getSize();
	}

	/**
	 * public IAVLNode getRoot()
	 * Returns the root AVL node, or null if the tree is empty
	 * Complexity: O(1)
	 */
	public IAVLNode getRoot()
	{
		return this.root;
	}
	
	/**
	 * public int insert(int k, String i)
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * returns -1 if an item with key k already exists in the tree.
	 * Complexity: O(logn)
	 */
	public int insert(int k, String i) {
		IAVLNode y = null;
		IAVLNode x = this.root;
		while (x != null) {
			y = x;
			if (k == x.getKey()) {
				return -1;
			}
			else if (k < x.getKey()) x = x.getLeft();
			else x = x.getRight();	
		}
		IAVLNode newNode = new AVLNode (k,i);
		updateMinMax(newNode, "i"); // checks if new node is min or max
		newNode.setParent(y);
		//empty tree
		if (this.empty()) {
			this.root = newNode;
		}
		else {
		if(newNode.getKey()<y.getKey()){
			y.setLeft(newNode);
		}
		else {
			y.setRight(newNode);
		}
		}
		updateSizeOnPathToRoot(y, "i");
		int numOfRotations = fixInsert(y);
		return numOfRotations;
	}

	
	/**
	 * private int fixInsert(IAVLNode parent)
	 * go up from the parent of the inserted node
	 * to the root and look for a criminal.
	 * fix it if found.
	 * returns the num of rotations needed to fix the criminal.
	 * Complexity: O(logn) 
	 */
	private int fixInsert(IAVLNode parent) {
		int numOfRotations = 0;
		while (parent != null) {
			int prevHeight = parent.getHeight();
			int newHeight = updateHeight(parent);
			boolean heightChanged = (prevHeight != newHeight);
			int bf = BF(parent);
			
			if (Math.abs(bf) < 2 && !heightChanged) {
				break;
			} else if (Math.abs(bf) < 2 && heightChanged) {
				parent = parent.getParent();
			} else if (Math.abs(bf) == 2) {
				numOfRotations = fixCriminal(parent, bf);
				break;
			}
		}
		return numOfRotations;
	}
	
	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 * Complexity: O(logn)
	 */
	public int delete(int k) {
		IAVLNode nodeToDelete = findNode(k);
		if (nodeToDelete == null) return -1;
		updateMinMax(nodeToDelete, "d");
		
		return deleteNode(nodeToDelete);
	}
	
	/**
	 * private IAVLNode deleteLeaf(IAVLNode node)
	 * deletes a leaf and returns the deleted node's father
	 * Complexity: O(1)
	 */
	private IAVLNode deleteLeaf(IAVLNode node) {
		IAVLNode y = node.getParent();
		if (y == null) {
			// the leaf is also the root
			this.root = null;	
		}
		else {
			// disconnect parent from deleted node
			if (node == y.getRight()) y.setRight(null);
			else y.setLeft(null);
			// disconnect deleted node from it's parent
			node.setParent(null);
		}
		return y;
	}
	
	/**
	 * private IAVLNode deleteNodeWithLeftSon(IAVLNode node)
	 * deletes a Node With Left Son and returns the deleted node's father
	 *  Complexity: O(1)
	 */
	private IAVLNode deleteNodeWithLeftSon(IAVLNode node) {
		IAVLNode y = node.getParent();
		IAVLNode leftSon = node.getLeft();
		
		// deleted node is the root
		if (y == null) {
			this.root = leftSon;
		} else {
			// connect deleted node's father to deleted node's son
			if (y.getRight() == node) y.setRight(leftSon);
			else y.setLeft(leftSon);
		}
	
		leftSon.setParent(y);
		// disconnect deleted node from parent and son
		node.setParent(null);
		node.setLeft(null);
		
		return y;
	}
	
	/**
	 * private IAVLNode deleteNodeWithRightSon(IAVLNode node)
	 * deletes a Node With Right son and returns the deleted node's father
	 *  Complexity: O(1)
	 */
	private IAVLNode deleteNodeWithRightSon(IAVLNode node) {
		IAVLNode y = node.getParent();
		IAVLNode rightSon = node.getRight();
		
		// deleted node is the root
		if (y == null) {
			this.root = rightSon;
		} else {
			// connect deleted node's father to deleted node's son
			if (y.getRight() == node) y.setRight(rightSon);
			else y.setLeft(rightSon);
		}
	
		rightSon.setParent(y);
		// disconnect deleted node from parent and son
		node.setParent(null);
		node.setRight(null);
		
		return y;
	}
	
	/**
	 * private void replaceNode(IAVLNode oldNode, IAVLNode newNode)
	 * function replaces between oldNode to newNode in tree by updating
	 * all relevant fields in tree
	 *  Complexity: O(1)
	 */
	private void replaceNode(IAVLNode oldNode, IAVLNode newNode) {
		// upadte new node
		newNode.setParent(oldNode.getParent());
		newNode.setRight(oldNode.getRight());
		newNode.setLeft(oldNode.getLeft());
		newNode.setHeight(oldNode.getHeight());
		((AVLNode)newNode).setSize(getSizeNull(oldNode));
		
		// update oldNode's relatives
		if (oldNode.getLeft() != null) oldNode.getLeft().setParent(newNode);
		if (oldNode.getRight() != null) oldNode.getRight().setParent(newNode);
		
		// if replaceing root
		if (this.root == oldNode) this.root = newNode;
		else {
			// check if old node is a left or right son
			if (oldNode.getParent().getRight() == oldNode) oldNode.getParent().setRight(newNode);
			else  oldNode.getParent().setLeft(newNode);
		}

		
		// disconnect old node
		oldNode.setParent(null);
		oldNode.setRight(null);
		oldNode.setLeft(null);
	}
	
	/**
	 * private IAVLNode deleteNodeWithTwoSons(IAVLNode node)
	 * deletes a Node With two sons and returns the physical deleted node's father
	 *  Complexity: O(logn)
	 */
	private IAVLNode deleteNodeWithTwoSons(IAVLNode node) {
		IAVLNode suc = findSuccessor(node);
		IAVLNode startFix;
		// check if suc is a leaf or has right son (suc cannot have a left son)
		if (suc.getRight() == null) {
			startFix= deleteLeaf(suc);
		} else {
			startFix = deleteNodeWithRightSon(suc);
		}
		// if the successor is the deleted node's son,
		// we don't want to return it's father as startfix because
		// it is deleted, instead we return the suc itself
		if (startFix == node) startFix = suc;
		// replace deleted node by suc
		replaceNode(node,suc);
		return startFix;
	}
	
	/**
	 * private int fixDelete(IAVLNode y)
	 * Balances the tree to be a valid AVL Tree
	 *  Complexity: O(logn)
	 */
	private int fixDelete(IAVLNode y) {
		int numOfRotations = 0;
		IAVLNode originalParent;
		while (y != null) {
			originalParent = y.getParent();
			int prevHeight = y.getHeight();
			int newHeight = updateHeight(y);
			boolean heightChanged = (prevHeight != newHeight);
			int bf = BF(y);
			
			if (Math.abs(bf) < 2 && !heightChanged) {
				break;
			} else if (Math.abs(bf) < 2 && heightChanged) {
				y = originalParent;
			} else if (Math.abs(bf) == 2) {
				numOfRotations += fixCriminal(y, bf);
				y = originalParent;
			}
		}
		return numOfRotations; 
	}
	
	/**
	 * private int deleteNode(IAVLNode nodeToDelete)
	 *deletes node to delete with the suitable delete function
	 *updates size field of nodes after delete
	 *fixes tree to be avl tree with fixDlete function
	 * Complexity: O(logn)
	 */
	private int deleteNode(IAVLNode nodeToDelete) {
		IAVLNode leftSon = nodeToDelete.getLeft();
		IAVLNode rightSon = nodeToDelete.getRight();
		// startFix is the father of the node that was physically deleted  
		IAVLNode startFix;
		
		// check if leaf
		if (leftSon == null && rightSon == null) {
			startFix = deleteLeaf(nodeToDelete);
		} else if (leftSon != null && rightSon != null) {
			startFix = deleteNodeWithTwoSons(nodeToDelete);
		} else {
			if (leftSon != null) startFix = deleteNodeWithLeftSon(nodeToDelete);
			else startFix = deleteNodeWithRightSon(nodeToDelete);
		}
		updateSizeOnPathToRoot(startFix, "d");
		int numOfRotations = fixDelete(startFix);
		return numOfRotations;
	}
	
	/**
	 * private void rotateLeft(IAVLNode x)
	 * x is parent and y is its right son
	 * @pre x!=null && y!=null
	 * @pre x.getRight() == y
	 * @post y.getleft() == x
	 *	     y                                x
	 * 	    / \                             /  \
	 *	   x   T3                          T1   y 
	 * 	  / \       < - - - - - - -            / \
	 *	 T1  T2     Left Rotation            T2  T3 
	 * Complexity: O(1)
	 */
	private void rotateLeft(IAVLNode x) {
		IAVLNode y = x.getRight();
		x.setRight(y.getLeft());
		if(y.getLeft() != null) {
			y.getLeft().setParent(x);
		}
		y.setParent(x.getParent());
		if (x.getParent() == null) {
			this.root = y;
		}
		else if(x == x.getParent().getLeft()) {
			x.getParent().setLeft(y);
		}
		else {
			x.getParent().setRight(y);
		}
		y.setLeft(x);
		x.setParent(y);
		
		((AVLNode)y).setSize(((AVLNode)x).getSize());
	
		((AVLNode)x).setSize(getSizeNull(x.getLeft()) + getSizeNull(x.getRight()) + 1);
		updateHeight(x);
		updateHeight(y);
	}
	

	/**
	 * private void rotateRight(IAVLNode x)
	 * x is parent and y is its left son
	 * @pre x!=null && y!=null
	 * @pre x.getleft() == y
	 * @post y.getRight() == x
	 *			 x                               y
	 *	 	    / \     Right Rotation          /  \
	 *		   y   T3   - - - - - - - >        T1   x 
	 *		  / \                                  / \
	 *		 T1  T2                               T2  T3
	 * Complexity: O(1)
	 */
	private void rotateRight(IAVLNode x) {
		IAVLNode y = x.getLeft();
		x.setLeft(y.getRight());
		if(y.getRight() != null) {
			y.getRight().setParent(x);
		}
		y.setParent(x.getParent());
		if (x.getParent() == null) {
			this.root = y;
		}
		else if(x == x.getParent().getRight()) {
			x.getParent().setRight(y);
		}
		else {
			x.getParent().setLeft(y);
		}
		y.setRight(x);
		x.setParent(y);
		
		((AVLNode)y).setSize(((AVLNode)x).getSize());
		((AVLNode)x).setSize(getSizeNull(x.getLeft()) + getSizeNull(x.getRight()) + 1);
		
		updateHeight(x);
		updateHeight(y);
	}
	
	/**
	* private void rotateLeftRight(IAVLNode x)
	* rotate x's left son left rotation, and then on x right rotation
	*	     x                               x                           z
	*	    / \                            /   \                        /  \ 
	*	   y   T4  Left Rotate (y)        z    T4  Right Rotate(x)    y      x
	*	  / \      - - - - - - - - ->    /  \      - - - - - - - ->  / \    / \
	*	T1   z                          y    T3                    T1  T2 T3  T4
	*	    / \                        / \
	*	  T2   T3                    T1   T2
	* Complexity: O(1)
	*/
	private void rotateLeftRight(IAVLNode x) {
		rotateLeft(x.getLeft());
		rotateRight(x);
	}
	
	/**
	* private void rotateRightLeft(IAVLNode x)
	* rotate x's right son right rotation, and then on x left rotation
	*		   x                            x                            z
	*		  / \                          / \                          /  \ 
	*		T1   y   Right Rotate (y)    T1   z      Left Rotate(x)   x      y
	*		    / \  - - - - - - - - ->     /  \   - - - - - - - ->  / \    / \
	*		   z   T4                      T2   y                  T1  T2  T3  T4
	*		  / \                              /  \
	*		T2   T3                           T3   T4
	* Complexity: O(1)
	*/
	private void rotateRightLeft(IAVLNode x) {
		rotateRight(x.getRight());
		rotateLeft(x);
	}
	
	/**
	 * private int fixCriminal(IAVLNode criminal, int criminalBf)
	 * decides which rotation is needed in order to fix the criminal
	 * returns the number of rotations
	 * Complexity: O(1)
	 */
	private int fixCriminal(IAVLNode criminal, int criminalBf) {
		switch (criminalBf) {
		case 2:
			int leftBf = BF(criminal.getLeft());
			switch (leftBf) {
			case 1:
			case 0:	
				// right rotation
				rotateRight(criminal);
				return 1;
			case -1:
				// left right rotation
				rotateLeftRight(criminal);
				return 2;
			}
			break;
		case -2:
			int rightBf = BF(criminal.getRight());
			switch (rightBf) {
			case 1:
				// right left rotation
				rotateRightLeft(criminal);
				return 2;
			case -1:
			case 0:	
				// left rotation
				rotateLeft(criminal);
				return 1;
			}
		}
		return 0;
	}
	
	/** 
	 * private IAVLNode findMin(IAVLNode n)
	 * returns the min of a subtree with root n
	 * Complexity: O(logn)
	 */
	private IAVLNode findMin(IAVLNode n) {
		IAVLNode y = n;
		while (y.getLeft() != null) {
			y = y.getLeft();
		}
		return y;
	}
	
	/** 
	 * private IAVLNode findMax(IAVLNode n)
	 * returns the max of a subtree with root n
	 * Complexity: O(logn)
	 */
	private IAVLNode findMax(IAVLNode n) {
		IAVLNode y = n;
		while (y.getRight() != null) {
			y = y.getRight();
		}
		return y;
	}
	
	/**
	 * public IAVLNode findSuccessor(IAVLNode n)
	 * returns the successor of a given node
	 * Complexity: O(logn)
	 */
	public IAVLNode findSuccessor(IAVLNode n) {
		if (n.getRight() != null) {
			return findMin(n.getRight());
		}
		IAVLNode x = n;
		IAVLNode y = x.getParent();
		while (y != null && x == y.getRight()) {
			x = y;
			y = x.getParent();
		}
		return y;
	}
	
	/**
	 * public IAVLNode findPredecessor(IAVLNode n)
	 * returns the predecessor of a given node
	 * Complexity: O(logn)
	 */
	public IAVLNode findPredecessor(IAVLNode n) {
		if (n.getLeft() != null) {
			return findMax(n.getLeft());
		}
		IAVLNode x = n;
		IAVLNode y = x.getParent();
		while (y != null && x == y.getLeft()) {
			x = y;
			y = x.getParent();
		}
		return y;
	}
	
	/**
	 * public IAVLNode findNode (int k)
	 * returns the node with key k if it exists in the tree
	 * otherwise, returns null
	 * Complexity: O(logn)
	 */
	public IAVLNode findNode (int k) {
		if (this.empty()) return null;
		IAVLNode x = this.root;
		while (x != null) {
			if (k == x.getKey()) {
				return x;
			}
			else if (k < x.getKey()) x = x.getLeft();
			else x = x.getRight();	
		}
		return null;
	}


	
	/**
	 * private int getSizeNull(IAVLNode n)
	 * returns a node's size or 0 if null
	 * Complexity: O(1)
	 */
	private int getSizeNull(IAVLNode n) {
		if (n == null) return 0;
		return ((AVLNode)n).getSize();
	}
	
	/**
	 * private int updateHeight(IAVLNode n)
	 * updates a node's height and returns it
	 * Complexity: O(1)
	 */
	private int updateHeight(IAVLNode n) {
		int leftHeight = getHeightWithNull(n.getLeft());
		int rightHeight = getHeightWithNull(n.getRight());
		
		int newHeight = 1 + Math.max(leftHeight, rightHeight);
		n.setHeight(newHeight);
		return newHeight;
	}
	
	/**
	 * private int getHeightWithNull(IAVLNode n)
	 * if a node is null, returns -1 as height
	 * else returns height
	 * Complexity: O(1)
	 */
	private int getHeightWithNull(IAVLNode n) {
		if (n == null) return -1;
		return n.getHeight();
	}
	
	/**
	 * private int BF(IAVLNode n)
	 * gets node n
	 * returns the balance factor of a given node
	 * Complexity: O(1)
	 */
	private int BF(IAVLNode n) {
		int leftHeight = getHeightWithNull(n.getLeft());
		int rightHeight = getHeightWithNull(n.getRight());
		return leftHeight - rightHeight;
	}

	/**
	 * private void updateSizeOnPathToRoot(IAVLNode n, String mode)
	 * updates size of all nodes in the path from the inserted node
	 *  to the root
	 *  n is the father of the inserted node
	 *  mode is a string representing insert or delet mode
	 *  Complexity: O(logn)
	 */
	private void updateSizeOnPathToRoot(IAVLNode n, String mode) {
		IAVLNode y = (AVLNode)n;
		// if insert mode, add 1 to all nodes in path to root, 
		// else subtract 1
		int toAdd = (mode.equals("i")) ? 1 : -1;
		while (y != null) {
			((AVLNode)y).setSize(((AVLNode)y).getSize() + toAdd);
			y = y.getParent();
		}
	}
	
	
	/**
	 * private void updateMinMax(IAVLNode node, String mode)
	 * updates min and max on insertion and deletion.
	 * node is the node that we insert/ delete
	 * mode is "i" for insert or "d" for deletion
	 * if we delete the maximum, we replace it with it's successor
	 * if we delete the minimum, we use predecessor
	 * if we insert a node, we check if it's min or max
	 * Complexity: O(logn)
	 */
	private void updateMinMax(IAVLNode node, String mode) {
		if (mode.equals("i")) {
			if (this.empty()) {
				this.min = node;
				this.max = node;
			}
			else {
				if (node.getKey()>this.max.getKey()) {
					this.max = node;
				}
				if (node.getKey()<this.min.getKey()) {
					this.min = node;
				}
			}
		} else if (mode.equals("d")) {
			if (node == this.min) {
				this.min = findSuccessor(node);
			}
			if (node == this.max) {
				this.max = findPredecessor(node);
			}
		}
	}
	
	// ---------------------- TreeList --------------------------
	// insert and delete from tree by rank (Tree list)
	// min and max are the first and last nodes in list
	// and not by key
	
	/**
	 * public IAVLNode treeSelect(int rank)
	 * returns the node with the given rank
	 * Complexity: O(logn)
	 */
	public IAVLNode treeSelect(int rank) {
		return recTreeSelect(this.root, rank);
	}
	
	/**
	 * private IAVLNode recTreeSelect(IAVLNode node, int rank)
	 * recursive function used to find a node in the tree with
	 * the given rank.
	 * Complexity: O(logn)
	 */
	private IAVLNode recTreeSelect(IAVLNode node, int rank) {
		int counter = getSizeNull(node.getLeft()) + 1;
		if (rank == counter) return node;
		else {
			if (rank < counter) return recTreeSelect(node.getLeft(), rank);
			else return recTreeSelect(node.getRight(), rank - counter);
		}
	}
	
	/**
	 * public int insertByRank(int i, int k, String s)
	 * insert a node to the tree with key k and value s.
	 * the node will be inserted to rank i+1
	 * returns the num of rotations that was needed in order to fix the tree
	 * Complexity: O(logn)
	 */
	public int insertByRank(int i, int k, String s) {
		IAVLNode newNode = new AVLNode(k,s);
		if (this.empty()) {
			this.root = newNode;
			this.max = newNode;
			this.min = newNode;
		} else {
			// insert last i = n
			if (i == this.size()) {
				this.max.setRight(newNode);
				newNode.setParent(this.max);
				this.max = newNode;
			} else if (i == 0) { // insert first
				this.min.setLeft(newNode);
				newNode.setParent(this.min);
				this.min = newNode;
			} else {
				IAVLNode fatherOne = this.treeSelect(i + 1);
				if (fatherOne.getLeft() == null) {
					fatherOne.setLeft(newNode);
					newNode.setParent(fatherOne);
				} else {
					IAVLNode predecessor = findPredecessor(fatherOne);
					predecessor.setRight(newNode);
					newNode.setParent(predecessor);
				}
			}
		}
		
		updateSizeOnPathToRoot(newNode.getParent(), "i");
		return fixInsert(newNode.getParent());
	}
	
	/**
	 * public int deleteByRank(int i)
	 * deletes the node with rank i + 1 from the tree.
	 * returns the num of rotations that was needed in order to fix the tree 
	 * Complexity: O(logn)
	 */
	public int deleteByRank(int i) {
		IAVLNode nodeToDelete = this.treeSelect(i + 1);
		// update min max if node to delete is last or first 
		if (i == 0) {
			this.min = findSuccessor(nodeToDelete);
		} if (i == this.size() - 1) {
			this.max = findPredecessor(nodeToDelete);
		}
		
		return deleteNode(nodeToDelete);
	}

	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{	
		public int getKey(); //returns node's key 
		public String getValue(); //returns node's value [info]
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public void setHeight(int height); // sets the height of the node
		public int getHeight(); // Returns the height of the node 
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in 
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode{
		private int key;
		private String value;
		private IAVLNode parent;
		private IAVLNode leftSon;
		private IAVLNode rightSon;
		private int height;
		private int size;
		
		/**
		 * public AVLNode(int key,String value)
		 * constructor, gets key and val and creates new node
		 * Complexity: O(1) 
		 */
		public AVLNode(int key,String value) {
			this.key = key;
			this.value = value;
			this.height = 0;
			this.size = 1;
		}
		
		/**
		 * public int getKey()
		 * returns the node's key
		 * Complexity: O(1) 
		 */
		public int getKey()
		{
			return this.key;
		}
		
		/**
		 * public String getValue()
		 * returns the node's value
		 * Complexity: O(1) 
		 */
		public String getValue()
		{
			return this.value;
		}
		
		/**
		 * public void setLeft(IAVLNode node)
		 * updates the node's left son to be the given node
		 * Complexity: O(1) 
		 */
		public void setLeft(IAVLNode node)
		{
			this.leftSon = node;
		}
		
		/**
		 * public IAVLNode getLeft()
		 * returns the node's left son or null if there is no son
		 * Complexity: O(1) 
		 */
		public IAVLNode getLeft()
		{
			if(this.leftSon == null) {
				return null;
			}
			return this.leftSon;
		}
		
		/**
		 * public void setRight(IAVLNode node)
		 * updates the node's right son to be the given node
		 * Complexity: O(1) 
		 */
		public void setRight(IAVLNode node)
		{
			this.rightSon = node;
		}
		
		/**
		 * public IAVLNode getRight()
		 * returns the node's right son or null if there is no son
		 * Complexity: O(1) 
		 */
		public IAVLNode getRight()
		{
			if(this.rightSon == null) {
				return null;
			}
			return this.rightSon;
		}
		
		/**
		 * public void setParent(IAVLNode node)
		 * updates the node's parent to be the given node
		 * Complexity: O(1) 
		 */
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		
		/**
		 * public IAVLNode getParent()
		 * returns the node's parent
		 * Complexity: O(1) 
		 */
		public IAVLNode getParent()
		{
			return this.parent;
		}

		/**
		 * public void setHeight(int height)
		 * updates the node's height to be the given height
		 * Complexity: O(1) 
		 */
		public void setHeight(int height)
		{
			this.height = height;
		}
		
		/**
		 * public int getHeight()
		 * returns the node's height
		 * Complexity: O(1) 
		 */
		public int getHeight()
		{
			return this.height;
		}

		/**
		 * public void setSize(int size)
		 * updates the node's size to be the given size
		 * Complexity: O(1) 
		 */
		public void setSize(int size)
		{
			this.size = size;
		}

		/**
		 * public int getSize()
		 * returns the node's size
		 * Complexity: O(1) 
		 */
		public int getSize()
		{
			return this.size;
		}
	}
}




