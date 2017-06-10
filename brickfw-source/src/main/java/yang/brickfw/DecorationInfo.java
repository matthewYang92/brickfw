package yang.brickfw;

/**
 * 简单分隔线修饰信息
 */
public class DecorationInfo {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public int outLeft;
    public int outTop;
    public int outRight;
    public int outBottom;

    public int leftMargin;
    public int rightMargin;
    public int topMargin;
    public int bottomMargin;

    public int drawableColor = 0;

    public BrickPositionInfo position;

    public DecorationInfo() {
    }

    public void reset() {
        left = top = right = bottom = 0;
        outLeft = outTop = outRight = outBottom = 0;
        leftMargin = rightMargin = topMargin = bottomMargin = 0;
        drawableColor = 0;
    }

}
