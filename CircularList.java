/**
 *
 * Circular list
 *
 * An implementation of a circular list with  key and info
 *
 */
 
 public class CircularList{
	 private int maxLen;
	 private int length;
	 private int start;
	 private Item [] array;

	/**
	 *  Complexity: O(1)
	 */
	public CircularList (int maxLen){
		this.maxLen = maxLen;
		this.array = new Item[this.maxLen];
		this.start = 0;
		this.length = 0;
	}
	 /**
   * public Item retrieve(int i)
   * returns the item in the ith position if it exists in the list.
   * otherwise, returns null
   *  Complexity: O(1)
   */
  public Item retrieve(int i)
  {
	 if (i < 0 || i >= this.length) return null;
	 int index = (this.start + i) % this.maxLen;
	 return this.array[index];
  }
  /**
   * public int insert(int i, int k, String s) 
   *
   * inserts an item to the ith position in list  with key k and  info s.
   * returns -1 if i<0 or i>n  or n=maxLen otherwise return 0.
   * Complexity: O(min{i+1,n-i+1})
   */
   public int insert(int i, int k, String s) {
	  if (i < 0 || i > this.length || this.length == this.maxLen) return -1;
	  if (i == 0) insertFirst(k,s);
	  else if (i == this.length) insertLast(k, s);
	  else {
		  if (i > this.length - i) {
			  shiftRight(i - 1, this.length - 1);
			  this.array[(this.start + i) % this.maxLen] = new Item(k, s);
		  } else {
			  shiftLeft(0, i);
			  this.start = Math.floorMod(this.start - 1, this.maxLen);
			  this.array[(this.start + i) % this.maxLen] = new Item(k, s);
		  }
	  }
	  this.length++;
	  return 0;
   }

   /**
    * public int delete(int i)
    *
    * deletes an item in the ith posittion from the list.
 	* returns -1 if i<0 or i>n-1 otherwise returns 0.
    * Complexity: O(min{i+1,n-i+1})
    */
    public int delete(int i){
 	   if (i < 0 || i > this.length-1) return -1;
 	   if (i == 0) deleteFirst();
 	   else if (i != this.length-1) {
 		   if (i > this.length - i) {
 				  shiftLeft(i + 1, this.length - 1);
 			  } else {
 				  shiftRight(0, i-1);
 				  this.start = (this.start + 1) % this.maxLen;
 			  }
 	   }
 	   this.length--;
 	   return 0;
    }
  /**
   * private void insertFirst(int k, String s)
   * inserts Item in first position
   *  Complexity: O(1)
   */
  private void insertFirst(int k, String s){
	   if (this.length == 0) {
		   this.array[this.start] = new Item(k,s);
		   
	   } else {
		   int newStart = Math.floorMod(this.start - 1, this.maxLen);
		   this.array[newStart] = new Item(k,s);
		   this.start = newStart;
	   }
  }
  /**
   * private void insertLast(int k, String s)
   * inserts Item in end of list position
   *  Complexity: O(1)
   */
  private void insertLast(int k, String s) {
	   this.array[(this.start + this.length) % this.maxLen] = new Item(k,s);
  }
  /**
   * private void shiftRight(int start, int end)
   * moves all items between positions start and end(including)
   * one index right in list 
   * Complexity: O(min{i+1,n-i+1})
   */
  private void shiftRight(int start, int end) {
	   for (int j = end; j >= start; j--) {
			  this.array[(this.start + j + 1) % this.maxLen] = this.array[(this.start + j) % this.maxLen];
		  }
  }
  /**
   * private void shiftLeft(int start, int end) 
   * * moves all items between positions start and end(including)
   * one index left in list 
   * Complexity: O(min{i+1,n-i+1})
   */
  private void shiftLeft(int start, int end) {
		  for(int j = start; j < end; j++) {
			  this.array[Math.floorMod(this.start + j -1, this.maxLen)] = this.array[Math.floorMod(this.start + j, this.maxLen)]; 
		  }
  }
  /**
   * private void deleteFirst()
   * deletes first item in list
   * Complexity: O(1)
   */  
   private void deleteFirst() {
	   this.start = (this.start + 1) % this.maxLen;
   }
 }
