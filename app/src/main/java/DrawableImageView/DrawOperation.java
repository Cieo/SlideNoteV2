package DrawableImageView;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by womo on 2017/2/2.
 */
public class DrawOperation {
    /**
     * The enum Draw mode.
     */
    public enum DrawingMode {
        DRAW,
        ERASE
    }

    /**
     * The enum Pen type.
     */
    public enum PenType {
        NORMAL,
        LINE,
        POINT
    }

    private float mStartX, mStartY, mStopX, mStopY;
    private Path mPath ;
    private DrawingMode mDrawingMode;
    private PenType mPenType;
    private Paint mPaint;
    private boolean mCompleted = false;




    // getters and setters

    /**
     * Gets start x.
     *
     * @return the start x
     */
    public float getStartX() {
        return mStartX;
    }

    /**
     * Sets start x.
     *
     * @param startX the start x
     * @return the start x
     */
    public DrawOperation setStartX(float startX) {
        mStartX = startX;
        return this;
    }

    /**
     * Gets start y.
     *
     * @return the start y
     */
    public float getStartY() {
        return mStartY;
    }

    /**
     * Sets start y.
     *
     * @param startY the start y
     * @return the start y
     */
    public DrawOperation setStartY(float startY) {
        mStartY = startY;
        return this;
    }

    /**
     * Gets stop x.
     *
     * @return the stop x
     */
    public float getStopX() {
        return mStopX;
    }

    /**
     * Sets stop x.
     *
     * @param stopX the stop x
     * @return the stop x
     */
    public DrawOperation setStopX(float stopX) {
        mStopX = stopX;
        return this;
    }

    /**
     * Gets stop y.
     *
     * @return the stop y
     */
    public float getStopY() {
        return mStopY;
    }

    /**
     * Sets stop y.
     *
     * @param stopY the stop y
     * @return the stop y
     */
    public DrawOperation setStopY(float stopY) {
        mStopY = stopY;
        return this;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public Path getPath() {
        return mPath;
    }

    /**
     * Sets path.
     *
     * @param path the path
     * @return the path
     */
    public DrawOperation setPath(Path path) {
        mPath = path;
        return this;
    }

    /**
     * Gets draw mode.
     *
     * @return the draw mode
     */
    public DrawingMode getDrawingMode() {
        return mDrawingMode;
    }

    /**
     * Sets draw mode.
     *
     * @param drawingMode the draw mode
     * @return the draw mode
     */
    public DrawOperation setDrawingMode(DrawingMode drawingMode) {
        mDrawingMode = drawingMode;
        return this;
    }

    /**
     * Gets pen type.
     *
     * @return the pen type
     */
    public PenType getPenType() {
        return mPenType;
    }

    /**
     * Sets pen type.
     *
     * @param penType the pen type
     * @return the pen type
     */
    public DrawOperation setPenType(PenType penType) {
        mPenType = penType;
        return this;
    }

    /**
     * Gets paint.
     *
     * @return the paint
     */
    public Paint getPaint() {
        return mPaint;
    }

    /**
     * Sets paint.
     *
     * @param paint the paint
     * @return the paint
     */
    public DrawOperation setPaint(Paint paint) {
        mPaint = paint;
        return this;
    }

    /**
     * Is completed boolean.
     *
     * @return the boolean
     */
    public boolean isCompleted() {
        return mCompleted;
    }

    /**
     * Sets completed.
     *
     * @param completed the completed
     * @return the completed
     */
    public DrawOperation setCompleted(boolean completed) {
        mCompleted = completed;
        return this;
    }
}
