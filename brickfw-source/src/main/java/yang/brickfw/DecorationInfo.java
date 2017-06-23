package yang.brickfw;

import java.util.Arrays;

/**
 * 简单分隔线修饰信息
 */
public class DecorationInfo {

    private BrickPositionInfo position;
    private int itemLeft;
    private int itemRight;
    private int itemTop;
    private int itemBottom;

    private int leftPadding;
    private int topPadding;
    private int rightPadding;
    private int bottomPadding;

    private  DecorationItemInfo[] decorationItems = { new DecorationItemInfo(), new DecorationItemInfo()
            , new DecorationItemInfo(), new DecorationItemInfo() };

    DecorationInfo(int itemLeft, int itemRight, int itemTop, int itemBottom, BrickPositionInfo position) {
        this.itemLeft = itemLeft;
        this.itemRight = itemRight;
        this.itemTop = itemTop;
        this.itemBottom = itemBottom;
        this.position = position;
    }

    DecorationInfo(BrickPositionInfo position) {
        this.position = position;
    }

    public void setDividerLeft(int color, int width) {
        decorationItems[0].setDrawableColor(color);
        decorationItems[0].setLeft(itemLeft - width);
        decorationItems[0].setRight(itemLeft);
        decorationItems[0].setTop(itemTop);
        decorationItems[0].setBottom(itemBottom);
        setLeftPadding(width);
        itemLeft = itemLeft - width;
    }

    public void setDividerTop(int color, int height) {
        decorationItems[1].setDrawableColor(color);
        decorationItems[1].setLeft(itemLeft);
        decorationItems[1].setRight(itemRight);
        decorationItems[1].setTop(itemTop - height);
        decorationItems[1].setBottom(itemTop);
        setTopPadding(height);
        itemTop = itemTop - height;
    }

    public void setDividerRight(int color, int width) {
        decorationItems[2].setDrawableColor(color);
        decorationItems[2].setLeft(itemRight);
        decorationItems[2].setRight(itemRight + width);
        decorationItems[2].setTop(itemTop);
        decorationItems[2].setBottom(itemBottom);
        setRightPadding(width);
        itemRight = itemRight + width;
    }

    public void setDividerBottom(int color, int height) {
        decorationItems[3].setDrawableColor(color);
        decorationItems[3].setLeft(itemLeft);
        decorationItems[3].setRight(itemRight);
        decorationItems[3].setTop(itemBottom);
        decorationItems[3].setBottom(itemBottom + height);
        setBottomPadding(height);
        itemBottom = itemBottom + height;
    }

    public BrickPositionInfo getPosition() {
        return position;
    }

    public int getItemLeft() {
        return itemLeft;
    }

    public int getItemRight() {
        return itemRight;
    }

    public int getItemTop() {
        return itemTop;
    }

    public int getItemBottom() {
        return itemBottom;
    }

    public void setItemBottom(int itemBottom) {
        this.itemBottom = itemBottom;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public int getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public DecorationItemInfo getLeftDecorationItem() {
        return decorationItems[0];
    }

    public DecorationItemInfo getTopDecorationItem() {
        return decorationItems[1];
    }

    public DecorationItemInfo getRightDecorationItem() {
        return decorationItems[2];
    }

    public DecorationItemInfo getBottomDecorationItem() {
        return decorationItems[3];
    }

    public DecorationItemInfo[] getDecorationItems() {
        return decorationItems;
    }

    @Override public String toString() {
        return "DecorationInfo{"
                + "position="
                + position
                + ", itemLeft="
                + itemLeft
                + ", itemRight="
                + itemRight
                + ", itemTop="
                + itemTop
                + ", itemBottom="
                + itemBottom
                + ", leftPadding="
                + leftPadding
                + ", topPadding="
                + topPadding
                + ", rightPadding="
                + rightPadding
                + ", bottomPadding="
                + bottomPadding
                + ", decorationItems="
                + Arrays.toString(decorationItems)
                + '}';
    }

    public static class DecorationItemInfo {
        private int left;
        private int top;
        private int right;
        private int bottom;

        private int leftMargin;
        private int rightMargin;
        private int topMargin;
        private int bottomMargin;

        private int drawableColor;


        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getRight() {
            return right;
        }

        public void setRight(int right) {
            this.right = right;
        }

        public int getBottom() {
            return bottom;
        }

        public void setBottom(int bottom) {
            this.bottom = bottom;
        }

        public int getLeftMargin() {
            return leftMargin;
        }

        public void setLeftMargin(int leftMargin) {
            this.leftMargin = leftMargin;
        }

        public int getRightMargin() {
            return rightMargin;
        }

        public void setRightMargin(int rightMargin) {
            this.rightMargin = rightMargin;
        }

        public int getTopMargin() {
            return topMargin;
        }

        public void setTopMargin(int topMargin) {
            this.topMargin = topMargin;
        }

        public int getBottomMargin() {
            return bottomMargin;
        }

        public void setBottomMargin(int bottomMargin) {
            this.bottomMargin = bottomMargin;
        }

        public int getDrawableColor() {
            return drawableColor;
        }

        public void setDrawableColor(int drawableColor) {
            this.drawableColor = drawableColor;
        }

        @Override public String toString() {
            return "DecorationItemInfo{"
                    + "left="
                    + left
                    + ", top="
                    + top
                    + ", right="
                    + right
                    + ", bottom="
                    + bottom
                    + ", leftMargin="
                    + leftMargin
                    + ", rightMargin="
                    + rightMargin
                    + ", topMargin="
                    + topMargin
                    + ", bottomMargin="
                    + bottomMargin
                    + ", drawableColor="
                    + drawableColor
                    + '}';
        }
    }
}
