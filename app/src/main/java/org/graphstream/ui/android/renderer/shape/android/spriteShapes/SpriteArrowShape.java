package org.graphstream.ui.android.renderer.shape.android.spriteShapes;

import org.graphstream.ui.geom.Vector2;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicSprite;
import org.graphstream.ui.graphicGraph.stylesheet.Style;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.Skeleton;
import org.graphstream.ui.android.renderer.shape.Orientable;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.PolygonalShape;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Path2D;

public class SpriteArrowShape extends PolygonalShape {
	Orientable orientable ;
	
	public SpriteArrowShape() {
		this.orientable = new Orientable() ;
	}
	
	@Override
	public void configureForGroup(Backend bck, Style style, DefaultCamera2D camera) {
		super.configureForGroup(bck, style, camera);
		orientable.configureOrientableForGroup(style, camera);
	}
	
	@Override
	public void configureForElement(Backend bck, GraphicElement element, Skeleton skel, DefaultCamera2D camera) {
		super.configureForElement(bck, element, skel, camera);
		orientable.configureOrientableForElement(camera, (GraphicSprite)element);
	}

	@Override
	public void make(Backend backend, DefaultCamera2D camera) {
		double x = area.theCenter.x;
		double y = area.theCenter.y;
		Vector2 dir = new Vector2(  orientable.target.x - x, orientable.target.y - y ); 

		dir.normalize();
		Vector2 per = new Vector2( dir.y(), -dir.x() );
		
		dir.scalarMult( area.theSize.x );
		per.scalarMult( area.theSize.y / 2 );

		theShape = new Path2D(5, true);
		theShape().moveTo( x + per.x(), y + per.y() );
		theShape().lineTo( x + dir.x(), y + dir.y() );
		theShape().lineTo( x - per.x(), y - per.y() );
		theShape.closePath();
	}

	@Override
	public void makeShadow(Backend backend, DefaultCamera2D camera) {
		double x = area.theCenter.x + shadowable.theShadowOff.x;
		double y = area.theCenter.y + shadowable.theShadowOff.y;
		Vector2 dir = new Vector2( orientable.target.x - x, orientable.target.y - y );
		dir.normalize();
		Vector2 per = new Vector2( dir.y(), -dir.x() );
		
		dir.scalarMult( area.theSize.x + shadowable.theShadowWidth.x );
		per.scalarMult( ( area.theSize.y + shadowable.theShadowWidth.y ) / 2 );

		theShape = new Path2D(5, true);
		theShape().moveTo( x + per.x(), y + per.y() );
		theShape().lineTo( x + dir.x(), y + dir.y() );
		theShape().lineTo( x - per.x(), y - per.y() );
		theShape.closePath();
	}
}