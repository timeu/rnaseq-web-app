package com.gmi.rnaseqwebapp.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

public interface MyResources extends ClientBundle {
	
	 public interface MainStyle extends CssResource {
		 String searchbox_container();
		 String searchbox();
		 String searchbox_white();
		 String spacer();
		 String infoBox();
		 String formbox();
		 String searchbox_apple();
		 String searchbox_dark();
		 String small();
		 String clearfix();
		 String box_shadow();
		 String box_container();
		 String layout_box_container();
		 String pager_container();
		 String pager_controls();
		 String filterbox_container();
		 String filterbox_title();
		 String filterbox();
		 String filterbox_header_row();
		 String filterbox_grey_row();
		 String filterbox_search_criterias();
		 String title();
		 String cellTable();
		 String round_button();
		 String round_button_selected();
		 String tree_container();
		 String tree_header();
		 String content_container();
		 String help_container();
		 String help_section();
		 String help_image();
		 String help_image_container();
		 String help_image_close_link();
		 String header();
		 String indicator_small_icon();
		 String nav_item_selected();
		 String nav_container();
		 String nav_list();
		 String nav_item();
	 }
	 
	 
	 @Source("logo.png")
	 ImageResource logo();
	 
	 @Source("search_white.png")
	 ImageResource search_white();
	 
	 @Source("search_dark.png")
	 ImageResource search_black();
	 
	
	 @Source("indicator_small.png")
	 ImageResource indicator_small();
	 
	 @Source("style.css")
	 MainStyle style();
	 
}
