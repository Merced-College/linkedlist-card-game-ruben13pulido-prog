//package linkedLists;
//linkList2.java
//demonstrates linked list
//to run this program: C>java LinkList2App
////////////////////////////////////////////////////////////////
public class Link
{
	public Card cardLink;             // next link in list
	public Link next;
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
