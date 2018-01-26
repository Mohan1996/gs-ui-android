package org.graphstream.ui.android.renderer.shape.android.basicShapes;

import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Rectangle2D;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.RectangularAreaShape;

public class RoundedSquareShape extends RectangularAreaShape {
	Rectangle2D theShape = new Rectangle2D();
	
	@Override
	public void make(Backend backend, DefaultCamera2D camera) {
		double w = area.theSize.x ;
		double h = area.theSize.x ;
		double r = h/8 ;
		if( h/8 > w/8 )
			r = w/8 ;
		((Rectangle2D) theShape()).setRoundRect( area.theCenter.x-w/2, area.theCenter.y-h/2, w, h, r, r ) ;
	}
	
	@Override
	public void makeShadow(Backend backend, DefaultCamera2D camera) {
		double x = area.theCenter.x + shadowable.theShadowOff.x;
		double y = area.theCenter.y + shadowable.theShadowOff.y;
		double w = area.theSize.x + shadowable.theShadowWidth.x * 2;
		double h = area.theSize.y + shadowable.theShadowWidth.y * 2;
		double r = h/8 ;
		if( h/8 > w/8 ) 
			r = w/8;
				
		((Rectangle2D) theShape()).setRoundRect( x-w/2, y-h/2, w, h, r, r );
	}
	
	@Override
	public Form theShape() {
		return theShape;
	}
}