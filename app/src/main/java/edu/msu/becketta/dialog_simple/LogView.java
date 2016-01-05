package edu.msu.becketta.dialog_simple;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aaron Beckett on 1/5/2016.
 */
public class LogView extends View {

    /************************** MEMBERS *****************************/

    /**
     * Key that is used to save the state of our parameters in a bundle
     */
    private static final String STATE_PARAMETERS = "state_parameters";

    /**
     * {@link FreeDrawView} that is used to make new annotations
     */
    private FreeDrawView freeDrawView;

    /**
     * The annotations that have been made on the current page
     */
    private ArrayList<Annotation> annotations = new ArrayList<>();

    /**
     * The current parameters
     */
    private Parameters params = new Parameters();

    /**
     * The image bitmap. None initially.
     */
    private Bitmap imageBitmap = null;

    /**
     * First touch status
     */
    private Touch touch1 = new Touch();

    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();

    /************************** CONSTRUCTION *****************************/

    public LogView(Context context) {
        super(context);
        init(null, 0);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

    public void saveState(Bundle bundle) {

        bundle.putSerializable(STATE_PARAMETERS, params);

        if (freeDrawView != null) {
            freeDrawView.saveState(bundle);
        }
    }

    public void loadState(Bundle savedInstanceState) {
        params = (Parameters)savedInstanceState.getSerializable(STATE_PARAMETERS);

        if (freeDrawView != null) {
            freeDrawView.loadState(savedInstanceState);
        }
    }

    /************************** METHODS *****************************/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // If there is no image to draw, we do nothing
        if(imageBitmap == null) {
            return;
        }

        if (params.imageScale == -1f) {
            /*
         * Determine the margins and scale to draw the image
         * centered and scaled to maximum size on any display
         */
            // Get the canvas size
            float wid = canvas.getWidth();
            float hit = canvas.getHeight();

            // What would be the scale to draw the where it fits both
            // horizontally and vertically?
            float scaleH = wid / imageBitmap.getWidth();
            float scaleV = hit / imageBitmap.getHeight();

            // Use the lesser of the two
            params.imageScale = scaleH < scaleV ? scaleH : scaleV;

            // What is the scaled image size?
            float iWid = params.imageScale * imageBitmap.getWidth();
            float iHit = params.imageScale * imageBitmap.getHeight();

            // Determine the top and left margins to center
            params.marginLeft = (wid - iWid) / 2;
            params.marginTop = (hit - iHit) / 2;
        }

        /*
         * Draw the image bitmap
         */
        canvas.save();
        canvas.translate(params.marginLeft, params.marginTop);
        canvas.scale(params.imageScale, params.imageScale);
        canvas.drawBitmap(imageBitmap, 0, 0, null);
        canvas.restore();
    }

    public void startAnnotation() {
        freeDrawView.enable();
    }

    public void finishAnnotation(boolean discard) {
        Annotation newAnnot = freeDrawView.disable();
        if (!discard && !newAnnot.isEmpty()) {
            annotations.add(newAnnot);
        }
    }

    /********************** GETTERS AND SETTERS **********************/

    /**
     * Set an image path
     * @param imagePath path to image file
     */
    public void setImagePath(String imagePath) {
        params.imagePath = imagePath;

        imageBitmap = BitmapFactory.decodeFile(imagePath);
        invalidate();
    }

    public void setFreeDrawView(FreeDrawView freeDrawView) {
        this.freeDrawView = freeDrawView;
    }

    /************************ NESTED CLASSES ***************************/

    private static class Parameters implements Serializable {
        /**
         * Path to the image file if one exists
         */
        public String imagePath = null;

        /**
         * Image drawing scale
         */
        private float imageScale = -1f;

        /**
         * Image left margin in pixels
         */
        private float marginLeft = 0;

        /**
         * Image top margin in pixels
         */
        private float marginTop = 0;
    }

    /**
     * Local class to handle the touch status for one touch.
     * We will have one object of this type for each of the
     * two possible touches.
     */
    private class Touch {
        /**
         * Touch id
         */
        public int id = -1;

        /**
         * Current x location
         */
        public float x = 0;

        /**
         * Current y location
         */
        public float y = 0;

        /**
         * Previous x location
         */
        public float lastX = 0;

        /**
         * Previous y location
         */
        public float lastY = 0;

        /**
         * Change in x value from previous
         */
        public float dX = 0;

        /**
         * Change in y value from previous
         */
        public float dY = 0;

        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }

        /**
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }
    }
}
