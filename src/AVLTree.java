import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

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

	
  /**
   * public AVLTree()
   *
   * constructs an empty AVLTree
   *
   */
  public AVLTree() {
	  this.root = null;
  }
	
	
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
    return root == null; // checking if the root is null
  }

  
 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	IAVLNode node = this.root;
	while (node.getKey() != -1) {
		if (k == node.getKey()) // if the key is found
			return node.getValue(); 
		else if (k < node.getKey()) // if the key is smaller than node's key
			node = node.getLeft();
		else // if the key is bigger than node's key
			node = node.getRight();
	}
	return null; // if key isn't in the tree
  }
  
  /**
   * private IAVLNode treePosition(int k)
   *
   * finds the place to insert the given key,
   * return the place's node parent.
   * if the key exists in tree, returns the existing node with the given key.
   **/
  private AVLNode treePosition(int k) {
	  AVLNode x = (AVLNode)this.getRoot();
	  AVLNode y = x;
	  while (x.getKey() != -1) { // until encounters a virtual leaf
		  y = x;
		  if (k == x.getKey())
			  return x; // returns the node with the given key if found in tree
		  else if (k < x.getKey())
			  x = (AVLNode)x.getLeft();
		  else 
			  x = (AVLNode)x.getRight();
	  }
	  return y; // return the parent
  }

  /**
   * private int insertBST(IAVLNode n)
   *
   * inserts node to the AVL tree in the right position according to BST invariants.
   * The method doesn't rebalance the tree.
   * The method return -1 if the key existed in tree before inserting, and 1 if the insertion succeeded.
   */
  private int insertBST(AVLNode n) {
	AVLNode y = treePosition(n.getKey()); // return the parent of the new node
	n.setParent(y); // set the new node's parent
	n.setRank(0); // making sure the rank of the new node is 0
	if (n.getKey() == y.getKey()) // the node is already in the tree
		return -1;
	if (n.getKey() < y.getKey()) { // set as the left child
		y.setLeft(n);
	}
	else if (n.getKey() > y.getKey()) { // set as the right child
		y.setRight(n);
	}
	return 1;
  }


  /**
   * private AVLNode successor(AVLNode n)
   *
   * gets a node and return its successor node.
   */
  private AVLNode successor(AVLNode n) {
	  AVLNode cur = n;
	  if (n.getRight().getKey() != -1) {// node has a real right child
		  cur = (AVLNode)n.getRight();
		  while (cur.getLeft().getKey() != -1)
			  cur = (AVLNode)cur.getLeft();
		  return cur;
	  }
	  else { // node doesn't have a real right child
		  while (cur.getParent().getRight() == cur) // cur is a right child
			  cur = (AVLNode)cur.getParent();
		  return (AVLNode)cur.getParent();
	  }
  }
  
  
  /**
   * private AVLNode otherChild(AVLNode p, AVLNode c)
   *
   * The method gets a parent node and a child node.
   * The method returns the parent's son which is not the given son.
   */
  private AVLNode otherChild(AVLNode p, AVLNode c) {
	  if (p.getLeft() == c)
		  return (AVLNode)p.getRight();
	  else
		  return (AVLNode)p.getLeft();
  }
  
  /**
   * AVLNode rankDiff(AVLNode p, AVLNode c)
   *
   * The method gets a parent node and a child node.
   * The method returns the rank differences between the two nodes.
   */
  private int rankDiff(AVLNode p, AVLNode c) {
	  return p.getRank()-c.getRank();
  }
  
  /**
   * private void updateSize(AVLNode n)
   *
   * The method gets a node and updates its and all its parents' size attribute. 
   */
  private void updateSize(AVLNode n) {
	  AVLNode cur = n;
	  while (cur.getParent() != null) {
		  AVLNode curLeft = (AVLNode)cur.getLeft();
		  AVLNode curRight = (AVLNode)cur.getRight();
		  cur.setSize(curLeft.getSize()+curRight.getSize()+1);
		  cur = (AVLNode)cur.getParent();
	  }
  }
  
  
  /**
   * private int promote(AVLNode n)
   *
   * The method gets a node and adds 1 to its rank.
   * The method returns 1.
   */
  private int promote(AVLNode n) {
	  n.setRank(n.getRank()+1);
	  return 1;
  }
  
  /**
   * private int demote(AVLNode n)
   *
   * The method gets a node and subs 1 to its rank.
   * The method returns 1.
   */
  private int demote(AVLNode n) {
	  n.setRank(n.getRank()-1);
	  return 1;
  }  

  /**
   * private int rightRotate(AVLNode z, AVLNode n)
   *
   * The method gets a parent node and its son, whom between is the edge to rotate.
   * The method updates the relevant references and makes the relevant rank changes.
   * The method returns 1 for rotation
   */  
  private int rightRotate(AVLNode z, AVLNode n) {
	  AVLNode p = (AVLNode)z.getParent();
	  
	  z.setLeft(n.getRight()); 
	  z.getLeft().setParent(z);
	  
	  n.setRight(z);
	  z.setParent(n);
	  
	  n.setParent(p);
	  if (p != null) { // updating n to be the parent's child instead of z
		  if (p.getLeft() == z) 
			  p.setLeft(n);
		  else
			  p.setRight(n);
		  }
	  else
		  this.root = n;
	  return 1; // one for rotating
  }
  
  /**
   * private int leftRotate(AVLNode z, AVLNode n)
   *
   * The method gets a parent node and its son, whom between is the edge to rotate.
   * The method updates the relevant references and makes the relevant rank changes.
   * The method returns 1 for rotation
   */
  private int leftRotate(AVLNode z, AVLNode n) {
	  AVLNode p = (AVLNode)z.getParent();
	  
	  z.setRight(n.getLeft());
	  z.getRight().setParent(z);
	  
	  n.setLeft(z);
	  z.setParent(n);
	  
	  n.setParent(p);
	  if (p != null) { // updating n to be the parent's child instead of z
		  if (p.getLeft() == z) 
			  p.setLeft(n);
		  else
			  p.setRight(n);
	  }
	  else
		  this.root = n; // the new root
	  return 1; // one for rotating
  }

  /**
   * private int leftRightRotate(AVLNode z, AVLNode n)
   *
   * The method gets a parent node and its son, whom between is the first edge to rotate.
   * The method calls left rotation and then right rotation for the second edge to rotate.
   * The method returns 2 for rotations
   */  
  private int leftRightRotate(AVLNode z, AVLNode n) {
	  int num = 0;
	  num += leftRotate(z, n);
	  num += rightRotate((AVLNode)n.getParent(), n);
	  return num; // 2 rotations
  }

  /**
   * private int rightLeftRotate(AVLNode z, AVLNode n)
   *
   * The method gets a parent node and its son, whom between is the first edge to rotate.
   * The method calls right rotation and then left rotation for the second edge to rotate.
   * The method returns 2 for rotations
   */  
  private int rightLeftRotate(AVLNode z, AVLNode n) {
	  int num = 0;
	  num += rightRotate(z, n);
	  num += leftRotate((AVLNode)n.getParent(), n);
	  return num; // 2 rotations
  }  

  /**
   * private int rebalanceInsert(AVLNode n)
   *
   * The method gets a node and rebalances the tree according to node's state.
   * The method calls promote and rotations if needed.
   * The method returns sum of rebalancing operations that were taken.
   */   
  private int rebalanceInsert(AVLNode n) {
	  AVLNode p = (AVLNode)n.getParent();
	  if (this.getRoot() == n)
		  return 0;
	  if (rankDiff(p, n) == 0) { // rank difference 0
		  if (rankDiff(p, otherChild(p, n)) == 1) // needs promotion
			  return promote(p) + rebalanceInsert(p);
		  else {
			  if (p.getLeft() == n) {
				  if (rankDiff(n, (AVLNode)n.getLeft()) == 1 
					&& rankDiff(n, (AVLNode)n.getRight()) == 2) // right rotation 
						  return rightRotate(p, n) + demote(p);
				  else if (rankDiff(n, (AVLNode)n.getLeft()) == 2 
					&& rankDiff(n, (AVLNode)n.getRight()) == 1) // leftRight rotation
						  return leftRightRotate(n, (AVLNode)n.getRight()) 
								  + demote(n) + demote(p) + promote((AVLNode)n.getRight());
			  }
			  else {
				  if (rankDiff(n, (AVLNode)n.getRight()) == 1 
					&& rankDiff(n, (AVLNode)n.getLeft()) == 2) {// left rotation
						  return leftRotate(p, n) + demote(p); }
				  else if (rankDiff(n, (AVLNode)n.getRight()) == 2 
					&& rankDiff(n, (AVLNode)n.getLeft()) == 1) // rightLeft rotation
					      return rightLeftRotate(n, (AVLNode)n.getLeft())
					    		  + demote(n) + demote(p) + promote((AVLNode)n.getLeft());
			  }			  
		  }
	  }
	  return 0; // no rebalancing operations were taken
  }
  
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * promotion/rotation - counted as one rebalance operation, double-rotation is counted as 2.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	   AVLNode n = new AVLNode(k, i);
	   if (this.getRoot() == null) { // if the tree is empty
		   this.root = n;
		   updateSize(n); // updating the size attribute of the relevant nodes
		   return 0;
	   }
	   int num = insertBST(n); //inserting n according to BST rules
	   if (num == -1) // if the key already exists in tree
		   return -1;
	   else {
		   num = rebalanceInsert(n); // rebalancing the tree
	   }
	   updateSize(n); // updating the size attribute of the relevant nodes
	   return num; // return number of rebalancing operations
   }

   /**
    * private void deleteBST(IAVLNode n)
    *
    * deletes node from the AVL tree according to BST invariants.
    * The method doesn't rebalance the tree.
    * The method returns the parent of the deleted node.
    */
   private AVLNode deleteBST(AVLNode n) {
 	AVLNode y = (AVLNode)n.getParent(); // return the parent of the node
 	if (n.getLeft().getKey() == -1 && n.getRight().getKey() == -1) { // deleting a leaf
 		if (y.getLeft() == n) { // node is a left leaf
 			y.setLeft(n.getLeft());
 			n.getLeft().setParent(y);
 			}
 		else { // node is a right leaf
 			y.setRight(n.getRight());
 			n.getRight().setParent(y);
 			}
 		return y;
 		}
 	else if (n.getLeft().getKey() == -1 || n.getRight().getKey() == -1) { // deleting an unary node
 			if (y.getLeft() == n) { // node is a left child
 				if (n.getLeft().getKey() == -1) {// wants to replace with right child
 					y.setLeft(n.getRight());
 					n.getRight().setParent(y);
 				}
 				else { // wants to replace with left child
 					y.setLeft(n.getLeft());
 					n.getLeft().setParent(y);
 				}
 			}
 			else { // node is a right child
 				if (n.getLeft().getKey() == -1) {// wants to replace with right child
 					y.setRight(n.getRight());
 					n.getRight().setParent(y);
 				}
 				else { // wants to replace with left child
 					y.setRight(n.getLeft());
 					n.getLeft().setParent(y);
 				}
 				
 			}
 			return y;
 		}
 	else { // deleting a node with two children
 		AVLNode m = successor(n);
 		AVLNode p = (AVLNode)m.getParent();
 		deleteBST(m); // deleting successor from tree
 		if (y != null) { // n is not the root
 			if (y.getLeft() == n) // adding successor instead of node
 				y.setLeft(m); 
 			else
 				y.setRight(m);
 		}
 		else
 			this.root = m;
 		
 		m.setParent(y);
 		m.setRank(n.getRank());
 		n.getLeft().setParent(m);
 		m.setLeft(n.getLeft());
 		n.getRight().setParent(m);
 		m.setRight(n.getRight());
 		return p;
 		}
   }
   
   
   /**
    * private int rebalanceDelete(AVLNode n)
    *
    * The method gets a node and rebalances the tree according to node's state.
    * The method calls promote and rotations if needed.
    * The method returns sum of rebalancing operations that were taken.
    */   
   private int rebalanceDelete(AVLNode p) {
	  if (p == null) // finished bottom up rebalancing
		  return 0;
	  
	  AVLNode leftChild = (AVLNode)p.getLeft();
	  AVLNode rightChild = (AVLNode)p.getRight();
	  
 	  if (rankDiff(p, leftChild) == 2 && rankDiff(p, rightChild) == 2) // rank differences 2,2
 			  return demote(p) + rebalanceDelete((AVLNode)p.getParent());
 	  else if (rankDiff(p, leftChild) == 3 && rankDiff(p, rightChild) == 1) {// rank differences 3,1
 		  if (rankDiff(rightChild, (AVLNode)rightChild.getLeft()) == 1 // 1,1
 				  && rankDiff(rightChild, (AVLNode)rightChild.getRight()) == 1) 
 			  return leftRotate(p, rightChild) + demote(p) + promote(rightChild);
 		  else if (rankDiff(rightChild, (AVLNode)rightChild.getLeft()) == 2 // 2,1
				  && rankDiff(rightChild, (AVLNode)rightChild.getRight()) == 1) 
			  return leftRotate(p, rightChild) + demote(p) + demote(p) + rebalanceDelete((AVLNode)p.getParent());
 		  else return rightLeftRotate(p, leftChild) // 1,2
 				  + demote(p) + demote(p) + demote((AVLNode)p.getRight()) + promote((AVLNode)p.getRight().getLeft()); 
 	  }
 	 else if (rankDiff(p, leftChild) == 1 && rankDiff(p, rightChild) == 3) {// rank differences 1,3
		  if (rankDiff(leftChild, (AVLNode)leftChild.getLeft()) == 1 // 1,1
				  && rankDiff(leftChild, (AVLNode)leftChild.getRight()) == 1) 
			  return rightRotate(p, leftChild) + demote(p) + promote(leftChild);
		  else if (rankDiff(leftChild, (AVLNode)leftChild.getLeft()) == 1 // 1,2
				  && rankDiff(leftChild, (AVLNode)leftChild.getRight()) == 2) 
			  return rightRotate(p, leftChild) + demote(p) + demote(p) + rebalanceDelete((AVLNode)p.getParent());
		  else return rightLeftRotate(p, leftChild) // 2,1
				  + demote(p) + demote(p) + demote((AVLNode)p.getLeft()) + promote((AVLNode)p.getLeft().getRight()); 
	  }
 	  return 0; // no rebalancing operation was taken
   }
    

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   AVLNode n = treePosition(k); // returns the wanted node
	   
	   if (n.getKey() != k) // not in tree
		   return -1;
	   
	   AVLNode p = deleteBST(n); // deleting n according to BST rules
	   int result = rebalanceDelete(p); // rebalancing the tree 
	   updateSize(n); // updating the size attribute of the relevant nodes
	   return result; 
   }


   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   IAVLNode node = this.root;
	   while (node.getLeft().getKey() != -1) { // stops on the virtual leaf's parent
		   node = node.getLeft();
	   }
	   return node.getValue();
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   IAVLNode node = this.root;
	   while (node.getRight().getKey() != -1) { // stops on the virtual leaf's parent
		   node = node.getRight();
	   }
	   return node.getValue();
   }
   
   /**
    * public IAVLNode[] nodesToArray()
    *
    * Returns a sorted array which contains all nodes in the tree,
    * sorted by their respective keys,
    * or an empty array if the tree is empty.
    */
   private IAVLNode[] nodesToArray()
   {
	   Stack<IAVLNode> s = new Stack<IAVLNode>(); // creating a stack to hold keys that we saw but didn't add to the array
	   IAVLNode[] arr = new IAVLNode[this.size()]; // creating the keys array that we will return
	   int index = 0; // index of the cur node in array
	   IAVLNode cur = this.root; // starting from root
	   while (cur.getKey() != -1 || !s.isEmpty()) { // until cur is a virtual leaf or the stack is empty
		   if (cur.getKey() != -1) {
			   s.push(cur); 
			   cur = cur.getLeft();
			   }
		   else { // got to a virtual leaf
			   IAVLNode n = s.pop(); // cur minimum
			   arr[index] = n; // adding to arr
			   index++;
			   cur = n.getRight(); // moving to the right
			   }
		   }
	   return arr;
   }
   

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  IAVLNode[] nodesArr = nodesToArray();
	  int[] arr = new int[this.size()];
	  for (int i=0; i<arr.length; i++)
		  arr[i] = nodesArr[i].getKey();
	  return arr;
  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  IAVLNode[] nodesArr = nodesToArray();
	  String[] arr = new String[this.size()];
	  for (int i=0; i<arr.length; i++)
		  arr[i] = nodesArr[i].getValue();
	  return arr;
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return ((AVLNode)this.root).getSize();
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   {
	   return this.root;
   }
   
   /**
    * public int getRank()
    *
    * Returns the rank of the tree.
    */
   public int getRank()
   {
	   if (this.root == null)
		   return -1;
	   else
		   return ((AVLNode)this.root).getRank();
   }
   
   
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    */   
   public AVLTree[] split(int x)
   {
	   AVLTree T1 = new AVLTree(); // tree with smaller keys
	   AVLTree T2 = new AVLTree(); // tree with bigger keys
	   AVLTree temp = new AVLTree();
	   
	   AVLNode n = treePosition(x); // finding x's node
	   T1.root = n.getLeft(); // Initialising the smaller tree
	   T2.root = n.getRight(); // Initialising the bigger tree
	   
	   AVLNode cur = n;
	   while (cur != this.root) {
		   if (cur.getParent().getRight() == cur) {// if cur is a right child			   
			   temp.root = cur.getParent().getLeft();
			   T1.join(cur.getParent(), temp);
		   }
		   else { // if cur is a left child
			   temp.root = cur.getParent().getRight();
			   T2.join(cur.getParent(), temp);
		   }
		   cur = (AVLNode)cur.getParent();
	   }
	   T1.rebalanceInsert((AVLNode)n.getLeft());
	   T2.rebalanceInsert((AVLNode)n.getRight());
	   AVLTree[] result = {T1,T2}; 
	   return result;
   }
   
   
   /**
    * private AVLNode findRankEquiv(AVLTree tree, int rank)
    *
    * gets a tree and a rank returns the first left node whose rank is less or equals to given rank.
    */
   private AVLNode findRankEquiv(AVLTree tree, int rank) {
	  AVLNode curr = (AVLNode)tree.getRoot();
	  while (curr.getRank() > rank)
		  curr = (AVLNode)curr.getLeft();
	  return curr;
   }
   
   
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    */   
   public int join(IAVLNode x, AVLTree t) {
	   if (t.getRoot() == null && this.getRoot() == null) { // both trees are empty
		   this.insert(x.getKey(), x.getValue());
	   }
	   if (t.getRoot() == null) { // t is empty
		   this.insert(x.getKey(), x.getValue());
	   }
	   else if (this.getRoot() == null) { // this tree if empty
		   t.insert(x.getKey(), x.getValue());
		   this.root = t.root;
	   }
	   
	   else { // none of the trees are empty
 		   AVLNode y = (AVLNode)x;
		   int thisKey = this.getRoot().getKey();
		   int otherKey = t.getRoot().getKey();
		   AVLTree leftTree;
		   AVLTree rightTree;
		   
		   if (otherKey < thisKey) {// joining from left side
			   leftTree = t;
			   rightTree = this;
		   }
		   else {// joining from right side
			   leftTree = this;
			   rightTree = t;
		   }
		   
		   AVLNode leftRoot = (AVLNode)leftTree.getRoot();
		   AVLNode rightRoot = (AVLNode)rightTree.getRoot();
		   
		   int leftRank = leftRoot.getRank();
		   int rightRank = rightRoot.getRank(); 
		   
		   if (leftRank <= rightRank) {
			   AVLNode b = findRankEquiv(rightTree, leftRank);
			   y.setRank(leftRank + 1);
			   
			   AVLNode c = (AVLNode)b.getParent();
			   y.setParent(c);
			   c.setLeft(y);
			   
			   y.setRight(b);
			   b.setParent(y);
			   
			   y.setLeft(leftRoot);
			   leftRoot.setParent(y);
			   
			   this.root = rightRoot;
			   this.root.setParent(null);
			   rebalanceInsert(y);
		   }
		   
		   else {
			   AVLNode b = findRankEquiv(leftTree, rightRank);
			   y.setRank(rightRank + 1);
			   
			   AVLNode c = (AVLNode)b.getParent();
			   y.setParent(c);
			   c.setRight(y);
			   
			   y.setLeft(b);
			   b.setParent(y);
			   
			   y.setRight(rightRoot);
			   rightRoot.setParent(y);
			   
			   this.root = leftRoot;
			   this.root.setParent(null);
			   rebalanceInsert(y);
		   }
	   }
	  updateSize((AVLNode)x); //updating the size attribute of the relevant nodes; 
	  return Math.abs(this.getRank() - t.getRank()) + 1;
   }

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
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
	  	private int key; // the key of the node
	  	private String info; // the value of the node
	  	private IAVLNode parent; // a reference to the node's parent
	  	private IAVLNode left; // a reference to the node's left son
	  	private IAVLNode right; // a reference to the node's right son
	  	private boolean isReal; // if the node is real or virtual 
	  	private int height; // keeps the node's height in the tree
	  	private int rank; // keeps the node's rank
	  	private int size; // keeps the node's subtree size
	  	
	  	public AVLNode(int key, String info) 
	  	{
	  		this.key = key;
	  		this.info = info;
	  		if (key != -1) {
	  			this.isReal = true;
	  			this.setLeft(new AVLNode(-1, "")); // creates by default the left child as a virtual leaf
	  			this.setRight(new AVLNode(-1, "")); // creates by default the right child as a virtual leaf
	  			this.size = 1;
	  		}
	  			else {
	  				this.rank = -1;
	  				this.size = 0;
	  			}
	  		
	  	}
	  
		public int getKey()
		{
			return this.key; 
		}
		public String getValue()
		{
			return this.info; 
		}
		public void setLeft(IAVLNode node)
		{
			this.left = node;
		}
		public IAVLNode getLeft()
		{
			return this.left;
		}
		public void setRight(IAVLNode node)
		{
			this.right = node;
		}
		public IAVLNode getRight()
		{
			return this.right;
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		public IAVLNode getParent()
		{
			return this.parent;
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			return this.isReal;
		}
	    public void setHeight(int height)
	    {
	    	this.height = height;
	    }
	    public int getHeight()
	    {
	    	return this.height;
	    }
	    public void setRank(int rank)
	    {
	    	this.rank = rank;
	    }
	    public int getRank()
	    {
	    	return this.rank;
	    }
	    public void setSize(int size)
	    {
	    	this.size = size;
	    }
	    public int getSize()
	    {
	    	return this.size;
	    }
  }

public static void main(String args[]) {
	boolean isRand = false;
	boolean insert = false;
	if (isRand) {
		int n = 50;
		printableTree tree = new printableTree();
		Random rand = new Random();
		for (int i=0; i<10; i++) {
			int val = rand.nextInt(n);
			System.out.println("number is : " + val);
			String info = Integer.toString(val);
			tree.insert(val, info);
			tree.printTree();
			System.out.println();
			}
	}
	else if (insert) {
		printableTree tree = new printableTree();
		int[] arr = {8,4,9,3,10,11,13};
		for (int val : arr) {
			//System.out.println("number is : " + val);
			String info = Integer.toString(val);
			tree.insert(val, info);
			tree.printTree();
			//System.out.println();
			}
		/*System.out.println("deleting 8:");
		tree.delete(8);
		tree.printTree();
		System.out.println("deleting 9:");
		tree.delete(9);
		tree.printTree();*/
		String[] array = tree.infoToArray();
		System.out.println(Arrays.toString(array));
	}
	
	boolean join = false;
	if (join) {
		printableTree tree = new printableTree();
		int[] arr = {1};
		for (int val : arr) {
			//System.out.println("number is : " + val);
			String info = Integer.toString(val);
			tree.insert(val, info);
			//tree.printTree();
			//System.out.println();
			}
		System.out.println();
		tree.printTree();
			
		printableTree tree2 = new printableTree();
			int[] arr2 = {3,4,5,6,7,8,9};
			for (int val2 : arr2) {
				//System.out.println("number is : " + val2);
				String info2 = Integer.toString(val2);
				tree2.insert(val2, info2);
				//tree2.printTree();
				//System.out.println();		
			}
			System.out.println();
			tree2.printTree();
			
			AVLTree dummy = new AVLTree();
			dummy.insert(2, "2");
			AVLTree tree2new = (AVLTree)tree2;
			tree2new.join(dummy.getRoot(), (AVLTree)tree);
			
			tree2.printTree();
	}
	
	boolean split = true;
	if (split) {
		printableTree tree2 = new printableTree();
		int[] arr2 = {3,4,5,6,7,8,9};
		for (int val2 : arr2) {
			//System.out.println("number is : " + val2);
			String info2 = Integer.toString(val2);
			tree2.insert(val2, info2);
			//tree2.printTree();
			//System.out.println();		
		}
		System.out.println();
		tree2.printTree();
		
		AVLTree[] arr = tree2.split(5);
		printableTree t1 = (printableTree)arr[0];
		t1.printTree();
		printableTree t2 = (printableTree)arr[1];
		t2.printTree();
	}
	
	
  
}
}