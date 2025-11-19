//package linkedLists;
//linkList2.java
//demonstrates linked list
//to run this program: C>java LinkList2App
////////////////////////////////////////////////////////////////
public class Link
{
	public Card cardLink;             
	public Link next;               // next link in list
	//-------------------------------------------------------------
	public Link(Card card) // constructor
	{
		cardLink = card;
	}
	//-------------------------------------------------------------
	public void displayLink()      // display ourself
	{
		System.out.println(cardLink);
	}
}  // end class Link
////////////////////////////////////////////////////////////////
