package org.graphstream.ui.android.renderer;

import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicSprite;
import org.graphstream.ui.graphicGraph.StyleGroup;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.AndroidFullGraphRenderer;
import org.graphstream.ui.android.renderer.shape.Shape;

public class SpriteRenderer extends StyleRenderer {
	
	private Shape shape = null;
			
	public SpriteRenderer(StyleGroup style) {
		super(style);
	}
	
	public static StyleRenderer apply(StyleGroup style, AndroidFullGraphRenderer renderer) {
    	return new SpriteRenderer( style );
	}

	@Override
	public void setupRenderingPass(Backend bck, DefaultCamera2D camera, boolean forShadow) {
		shape = bck.chooseSpriteShape(shape, group);
	}

	@Override
	public void pushStyle(Backend bck, DefaultCamera2D camera, boolean forShadow) {
		shape.configureForGroup(bck, group, camera);
	}

	@Override
	public void pushDynStyle(Backend bck, DefaultCamera2D camera, GraphicElement element) {}

	@Override
	public void renderElement(Backend bck, DefaultCamera2D camera, GraphicElement element) {
		GraphicSprite sprite = (GraphicSprite)element;
		AreaSkeleton skel = getOrSetAreaSkeleton(element);

		shape.configureForElement(bck, element, skel, camera);
		shape.render(bck, camera, element, skel);
	}

	@Override
	public void renderShadow(Backend bck, DefaultCamera2D camera, GraphicElement element) {
		GraphicSprite sprite = (GraphicSprite)element;
		Point3 pos = camera.getSpritePosition(sprite, new Point3(), StyleConstants.Units.GU);
		AreaSkeleton skel = getOrSetAreaSkeleton(element);
		
		shape.configureForElement(bck, element, skel, camera);
		shape.renderShadow(bck, camera, element, skel);
	}

	@Override
	public void elementInvisible(Backend bck, DefaultCamera2D camera, GraphicElement element) {}

	private AreaSkeleton getOrSetAreaSkeleton(GraphicElement element) {
		if(element instanceof GraphicSprite) {
			AreaSkeleton info = (AreaSkeleton)element.getAttribute(Skeleton.attributeName);
			
			if(info == null) {
				info = new AreaSkeleton();
				element.setAttribute(Skeleton.attributeName, info);
			}
			
			return info;
		} 
		else {
			throw new RuntimeException("Trying to get NodeInfo on non-node ...");
		}
	}
	
	@Override
	public void endRenderingPass(Backend bck, DefaultCamera2D camera, boolean forShadow) {
		// TODO Auto-generated method stub
		
	}
	
	

}
