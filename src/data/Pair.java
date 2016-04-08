package data;

public class Pair<A, B> implements Comparable<Pair<A, B>>{
	public A key;
	public B value;
	
	public Pair(A k, B v) {
		key = k;
		value = v;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode() + value.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Pair) {			
			Pair<A, B> p = (Pair<A, B>) o;
			if (key.equals(p.key) && value.equals(p.value)) return true;
			else return false;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(Pair<A, B> o) {
		if (key instanceof Comparable && value instanceof Comparable) {
			Comparable<A> k = (Comparable<A>) key;
			Comparable<B> v = (Comparable<B>) value;
			if (!k.equals(o.key)) {
				return k.compareTo(o.key);
			}
			return v.compareTo(o.value);
		}
		throw new RuntimeException("Unable to compare the Pair");
	}
}
