package h4131.observer;

import java.util.ArrayList;
import java.util.Collection;

public class Observable {
	private Collection<Observer> obs;
	public Observable(){
		obs = new ArrayList<Observer>();
	}
	public void addObserver(Observer o){
		if (!obs.contains(o)) obs.add(o);
	}
	public void notifyObservers(Object arg){
		for (Observer o : obs) 
			o.update(this, arg);
	}
	public void notifyObservers(){
		notifyObservers(null);
	}
}
