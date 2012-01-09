package com.gmi.rnaseqwebapp.client.event;

import com.gmi.rnaseqwebapp.client.event.LoadingIndicatorEvent.LoadingIndicatorHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class LoadingIndicatorEvent  extends GwtEvent<LoadingIndicatorHandler> {
	
	private static final Type<LoadingIndicatorHandler> TYPE = new Type<LoadingIndicatorHandler>();
	protected boolean show;
	
	public interface LoadingIndicatorHandler extends EventHandler {
		  void onProcessLoadingIndicator(LoadingIndicatorEvent event);
	}
	
	public static Type<LoadingIndicatorHandler> getType() {
		return TYPE;
	}

	 public static void fire(HasHandlers source,boolean show) {
		 source.fireEvent(new LoadingIndicatorEvent(show));
	 }
		  
	 public LoadingIndicatorEvent(boolean show) {
		 this.show = show;
	 }

	  @Override
	  protected void dispatch(LoadingIndicatorHandler handler) {
	    handler.onProcessLoadingIndicator(this);
	  }

	  @Override
	  public Type<LoadingIndicatorHandler> getAssociatedType() {
	    return getType();
	  }
	  
	  public boolean getShow()
	  {
		  return this.show;
	  }
}

