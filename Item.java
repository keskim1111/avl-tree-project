//Name: Kim Kestenboum, username: kimk, id: 315532226, Name: Roni Hamri, username: ronihamri, id: 313373599
public class Item{
	
	private int key;
	private String info;
	/**
	 *  Complexity: O(1)
	 */
	public Item (int key, String info){
		this.key = key;
		this.info = info;
	}
	/**
	 *  Complexity: O(1)
	 */
	public int getKey(){
		return key;
	}
	/**
	 *  Complexity: O(1)
	 */
	public String getInfo(){
		return info;
	}
}