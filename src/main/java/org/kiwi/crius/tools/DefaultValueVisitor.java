package org.kiwi.crius.tools;

import org.hibernate.mapping.*;

/**
 * Default ValueVisitor which throws UnsupportedOperationException on all accepts.
 * Can be changed by passing true to the constructor.
 * 
 * @author max
 *
 */
public class DefaultValueVisitor implements ValueVisitor {

	boolean throwException = true;

	/**
	 * 
	 * @param throwException if true exception will be thrown, otherwise return null in accept calls.
	 */
	protected DefaultValueVisitor(boolean throwException) {
		this.throwException = throwException;
	}

	protected Object handle(Value o) {
		if (throwException) { 
			
			throw new UnsupportedOperationException("accept on " + o); 
		} 
		else { return null; }
	}
	
	public Object accept(Bag o){

		return handle(o);
	}

	public Object accept(IdentifierBag o){

		return handle(o);
	}

	public Object accept(List o){

		return handle(o);
	}

	public Object accept(PrimitiveArray o){

		return handle(o);
	}

	public Object accept(Array o){

		return handle(o);
	}

	public Object accept(Map o){

		return handle(o);
	}

	public Object accept(OneToMany o){

		return handle(o);
	}

	public Object accept(Set o){

		return handle(o);
	}

	public Object accept(Any o){

		return handle(o);
	}

	public Object accept(SimpleValue o){

		return handle(o);
	}

	public Object accept(DependantValue o){

		return handle(o);
	}

	public Object accept(Component o){

		return handle(o);
	}

	public Object accept(ManyToOne o){

		return handle(o);
	}

	public Object accept(OneToOne o){

		return handle(o);
	}

}
